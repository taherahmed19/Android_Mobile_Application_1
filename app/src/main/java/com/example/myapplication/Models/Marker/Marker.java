package com.example.myapplication.Models.Marker;

public class Marker {

    private int marker;
    private String description;
    private String lat;
    private String lng;
    private String image;
    private int id;

    public Marker(int marker, String description, String lat, String lng, String image, int id) {
        this.marker = marker;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.image = image;
        this.id = id;
    }

    public int getMarker() {
        return marker;
    }

    public void setMarker(int marker) {
        this.marker = marker;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
