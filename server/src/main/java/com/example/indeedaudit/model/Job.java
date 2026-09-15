package com.example.indeedaudit.model;

public class Job {
    private String id;
    private String title;
    private String location;

    public Job(String id, String title, String location) {
        this.id = id;
        this.title = title;
        this.location = location;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getLocation() { return location; }
}
