package com.example.myapplication.Refactor;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.R;
import com.google.common.base.Optional;
import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.map.CameraPosition;
import com.tomtom.online.sdk.map.Icon;
import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.MapFragment;
import com.tomtom.online.sdk.map.Marker;
import com.tomtom.online.sdk.map.MarkerBuilder;
import com.tomtom.online.sdk.map.OnMapReadyCallback;
import com.tomtom.online.sdk.map.Route;
import com.tomtom.online.sdk.map.RouteBuilder;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.map.TomtomMapCallback;
import com.tomtom.online.sdk.routing.OnlineRoutingApi;
import com.tomtom.online.sdk.routing.RoutingApi;
import com.tomtom.online.sdk.routing.data.FullRoute;
import com.tomtom.online.sdk.routing.data.RouteQuery;
import com.tomtom.online.sdk.routing.data.RouteQueryBuilder;
import com.tomtom.online.sdk.routing.data.RouteResponse;
import com.tomtom.online.sdk.routing.data.RouteType;
import com.tomtom.online.sdk.search.OnlineSearchApi;
import com.tomtom.online.sdk.search.SearchApi;
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderSearchQueryBuilder;
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderSearchResponse;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.tomtom.online.sdk.map.MapConstants.DEFAULT_ZOOM_LEVEL;

public class RoutingMap implements OnMapReadyCallback,
        TomtomMapCallback.OnMapClickListener {

    private TomtomMap tomtomMap;
    private SearchApi searchApi;
    private RoutingApi routingApi;
    private Route route;
    private LatLng departurePosition;
    private LatLng destinationPosition;
    private LatLng wayPointPosition;
    private Icon departureIcon;
    private Icon destinationIcon;

    Context context;
    FragmentManager fragmentManager;

    public RoutingMap(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;

        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.routingMapFragment);
        mapFragment.getAsyncMap(this);

        searchApi = OnlineSearchApi.create(context);
        routingApi = OnlineRoutingApi.create(context);
        departureIcon = Icon.Factory.fromResources(context, R.drawable.ic_map_route_departure);
        destinationIcon = Icon.Factory.fromResources(context, R.drawable.ic_map_route_destination);
    }

    @Override
    public void onMapReady(@NonNull TomtomMap tomtomMap) {
        this.tomtomMap = tomtomMap;
        this.tomtomMap.setMyLocationEnabled(true);
        this.tomtomMap.addOnMapClickListener(this);
        this.tomtomMap.getMarkerSettings().setMarkersClustering(true);
        this.initialMapCentering();
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        if (isDeparturePositionSet() && isDestinationPositionSet()) {
            clearMap();
        } else {
            handleLongClick(latLng);
        }
    }

    public void drawRoute(LatLng start, LatLng stop) {
        wayPointPosition = null;
        drawRouteWithWayPoints(start, stop, null);
    }

    public void setMapFocus(LatLng start){
        this.tomtomMap.centerOn(CameraPosition.builder()
                .focusPosition(new LatLng(start.getLatitude(), start.getLongitude()))
                .zoom(DEFAULT_ZOOM_LEVEL)
                .bearing(MapConstants.ORIENTATION_NORTH)
                .build());
    }

    private void initialMapCentering(){
        this.tomtomMap.centerOn(CameraPosition.builder()
                .focusPosition(new LatLng(51.5073899, -0.1364547))
                .zoom(DEFAULT_ZOOM_LEVEL)
                .bearing(MapConstants.ORIENTATION_NORTH)
                .build());
    }

    private void handleLongClick(@NonNull LatLng latLng) {
        searchApi.reverseGeocoding(new ReverseGeocoderSearchQueryBuilder(latLng.getLatitude(), latLng.getLongitude()).build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<ReverseGeocoderSearchResponse>() {
                    @Override
                    public void onSuccess(ReverseGeocoderSearchResponse response) {
                        processResponse(response);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    private void processResponse(ReverseGeocoderSearchResponse response) {
                        if (response.hasResults()) {
                            processFirstResult(response.getAddresses().get(0).getPosition());
                        }
                    }

                    private void processFirstResult(LatLng geocodedPosition) {
                        if (!isDeparturePositionSet()) {
                            setAndDisplayDeparturePosition(geocodedPosition);
                        } else {
                            destinationPosition = geocodedPosition;
                            tomtomMap.removeMarkers();
                            drawRoute(departurePosition, destinationPosition);
                        }
                    }

                    private void setAndDisplayDeparturePosition(LatLng geocodedPosition) {
                        departurePosition = geocodedPosition;
                        createMarkerIfNotPresent(departurePosition, departureIcon);
                    }
                });
    }

    private boolean isDestinationPositionSet() {
        return destinationPosition != null;
    }

    private boolean isDeparturePositionSet() {
        return departurePosition != null;
    }

    private RouteQuery createRouteQuery(LatLng start, LatLng stop, LatLng[] wayPoints) {
        return (wayPoints != null) ?
                new RouteQueryBuilder(start, stop).withWayPoints(wayPoints).withRouteType(RouteType.FASTEST).build() :
                new RouteQueryBuilder(start, stop).withRouteType(RouteType.FASTEST).build();
    }


    private void drawRouteWithWayPoints(LatLng start, LatLng stop, LatLng[] wayPoints) {
        RouteQuery routeQuery = createRouteQuery(start, stop, wayPoints);
        routingApi.planRoute(routeQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<RouteResponse>() {

                    @Override
                    public void onSuccess(RouteResponse routeResponse) {
                        displayRoutes(routeResponse.getRoutes());
                        tomtomMap.displayRoutesOverview();
                    }

                    private void displayRoutes(List<FullRoute> routes) {
                        for (FullRoute fullRoute : routes) {
                            route = tomtomMap.addRoute(new RouteBuilder(
                                    fullRoute.getCoordinates()).startIcon(departureIcon).endIcon(destinationIcon));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        clearMap();
                    }
                });
    }

    private void createMarkerIfNotPresent(LatLng position, Icon icon) {
        Optional<Marker> optionalMarker = tomtomMap.findMarkerByPosition(position);
        if (!optionalMarker.isPresent()) {
            tomtomMap.addMarker(new MarkerBuilder(position)
                    .icon(icon));
        }
    }

    private void clearMap() {
        tomtomMap.clear();
        departurePosition = null;
        destinationPosition = null;
        route = null;
    }

    public TomtomMap getTomtomMap() {
        return tomtomMap;
    }
}
