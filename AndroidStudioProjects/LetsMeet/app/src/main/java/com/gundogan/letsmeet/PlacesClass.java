package com.gundogan.letsmeet;

import android.graphics.Bitmap;

public class PlacesClass {
    private static PlacesClass Instance;

    private Bitmap image;
    private String name;
    private  String type;
    private  String details;

    private PlacesClass()
    {

    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public  static PlacesClass getInstance()
    {
        if(Instance ==null){
          Instance=new PlacesClass();

        }
        return  Instance;
    }

}
