package com.example.startpage;

/**
 * Created by Administrator on 2017/4/28 0028.
 */

public class Fruit {

    private String name;
    private int imageId;

    public Fruit(String name, int imageId) {

    this.name=name;
    this.imageId=imageId;
}

    public String getName() {

        return name;
    }

    public int getImageId() {
        return imageId;
    }
}