package com.example.moop_finalproject;

public class Movies {
    private String imdbID;
    private String title;
    private String year;
    private String type;
    private String posterUrl;

    public Movies(String imdbID, String title, String year, String type, String posterUrl) {
        this.imdbID = imdbID;
        this.title = title;
        this.year = year;
        this.type = type;
        this.posterUrl = posterUrl;
    }

    @Override
    public String toString() {
        return "Movies{" +
                "imdbID='" + imdbID + '\'' +
                ", title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", type='" + type + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                '}';
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}
