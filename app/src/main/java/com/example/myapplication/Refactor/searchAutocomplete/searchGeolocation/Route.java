package com.example.myapplication.Refactor.searchAutocomplete.searchGeolocation;

public class Route {

    RouteGeolocation source;
    RouteGeolocation destination;

    public Route(RouteGeolocation source, RouteGeolocation destination) {
        this.source = source;
        this.destination = destination;
    }

    public RouteGeolocation getSource() {
        return source;
    }

    public void setSource(RouteGeolocation source) {
        this.source = source;
    }

    public RouteGeolocation getDestination() {
        return destination;
    }

    public void setDestination(RouteGeolocation destination) {
        this.destination = destination;
    }
}
