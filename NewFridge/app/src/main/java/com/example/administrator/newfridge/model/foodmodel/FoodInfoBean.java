package com.example.administrator.newfridge.model.foodmodel;

/**
 * @author LG32
 * 储存的食材信息
 */
public class FoodInfoBean {

    private String food_src;
    private String food_name;
    private String food_description;
    private String food_begin;
    private String food_end;
    private String food_deadline;

    public FoodInfoBean(){
    }

    public String getFood_src() {
        return food_src;
    }

    public void setFood_src(String food_src) {
        this.food_src = food_src;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_description() {
        return food_description;
    }

    public void setFood_description(String food_description) {
        this.food_description = food_description;
    }

    public String getFood_begin() {
        return food_begin;
    }

    public void setFood_begin(String food_begin) {
        this.food_begin = food_begin;
    }

    public String getFood_end() {
        return food_end;
    }

    public void setFood_end(String food_end) {
        this.food_end = food_end;
    }

    public String getFood_deadline() {
        return food_deadline;
    }

    public void setFood_deadline(String food_deadline) {
        this.food_deadline = food_deadline;
    }
}
