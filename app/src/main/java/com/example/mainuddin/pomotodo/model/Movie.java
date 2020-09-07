package com.example.mainuddin.pomotodo.model;

public class Movie {

    private String title;
    private String group;


    public Movie(String title, String group) {
        this.title = title;
        this.group = group;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                '}';
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}