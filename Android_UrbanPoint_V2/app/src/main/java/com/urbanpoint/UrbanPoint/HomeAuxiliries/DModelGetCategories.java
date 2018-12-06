package com.urbanpoint.UrbanPoint.HomeAuxiliries;

/**
 * Created by Danish on 2/13/2018.
 */

public class DModelGetCategories {

    String id;
    String name;
    String image;

    public DModelGetCategories(String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

   public DModelGetCategories() {
        this.id = "";
        this.name = "";
        this.image = "";
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getImage(){
        return image;
    }
}
