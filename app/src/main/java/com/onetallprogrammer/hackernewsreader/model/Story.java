package com.onetallprogrammer.hackernewsreader.model;

public class Story {

    public String title;

    public String url;

    public Story(String title, String url) {

        this.title = title.replace("\"", "");

        this.url = url.replace("\"", "");

    }

    @Override
    public String toString() {

        return title;

    }
}

