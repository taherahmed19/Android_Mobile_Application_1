package com.example.myapplication.Models.Marker;

public class Marker {

    private String category;
    private String description;
    private String lat;
    private String lng;
    private String encodedImage;
    private int userId;
    private int id;
    private int rating;

    public Marker(int userId, String category, String description, String lat, String lng, String encodedImage, int id, int rating) {
        this.category = category;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.encodedImage = encodedImage;
        this.id = id;
        this.userId = userId;
        this.rating = rating;
    }

    public static int CategorySwitchCase(String category){
        int categoryItem = -1;

        switch (category.toLowerCase()){
            case "environment":
                categoryItem = 1;
                break;
            case "weather":
                categoryItem = 2;
                break;
            case "people":
                categoryItem = 3;
                break;
            default:
                categoryItem = 0;
                break;
        }

        return categoryItem;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getEncodedImage() {
        return encodedImage;
    }

    public void setEncodedImage(String encodedImage) {
        this.encodedImage = encodedImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
