package com.example.administrator.newfridge.model.foodmodel;

import java.util.ArrayList;

public class FoodList {

    private ArrayList<FoodBean> list;

    private static FoodList foodList;

    private FoodList(){}

    public static FoodList getFoodList(){
        if (foodList == null ){
            foodList = new FoodList ();
            return foodList;
        }
        else
            return foodList;
    }

    public ArrayList<FoodBean> getList() {
        return list;
    }

    public void setList(ArrayList<FoodBean> list) {
        this.list = list;
    }

    public void clearList(){
        list.clear ();
    }
}
