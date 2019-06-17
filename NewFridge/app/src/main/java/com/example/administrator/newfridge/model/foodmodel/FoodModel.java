package com.example.administrator.newfridge.model.foodmodel;

/**
 * Created by Administrator on 2018/2/27.
 * 测试用foodmodel类，
 */

public class FoodModel {
    public FoodModel() {}

    private Integer _id;
    private String title;
    private String time;
    private String nature;
    private Integer imageID;

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public FoodModel(Integer _id, String title, String time, String nature, Integer imageID) {
        this._id = _id;
        this.title = title;
        this.time = time;
        this.nature = nature;
        this.imageID = imageID;
    }

    public Integer getImageID() {
        return imageID;
    }

    public void setImageID(Integer imageID) {
        this.imageID = imageID;
    }


    public Integer getFoodID() {
        return _id;
    }

    public void setFoodID(Integer _id) {
        this._id= _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
