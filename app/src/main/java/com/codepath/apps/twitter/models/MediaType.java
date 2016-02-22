package com.codepath.apps.twitter.models;

public enum MediaType {
    VIDEO("video"), PHOTO("photo");

    private String value;

    MediaType(String value){
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}