package com.example.administrator.newfridge.model;

import java.util.ArrayList;

/**
 * @author LG32
 * @date 2018/11/19
 * 食谱详细信息
 */
public class MenuInfoBean {

    private String foodImg_src;
    private String menu_name;
    private ArrayList<String> food_material = new ArrayList<> ();
    private ArrayList <String> cooking_way = new ArrayList<> ();

    public MenuInfoBean(){
    }

    public MenuInfoBean(String foodImg_src, String menu_name,
                        ArrayList<String> food_material, ArrayList<String> cooking_way){
        this.foodImg_src = foodImg_src;
        this.menu_name = menu_name;
        this.food_material = food_material;
        this.cooking_way = cooking_way;
    }

    public String getFoodImg_src() {
        return foodImg_src;
    }

    public void setFoodImg_src(String foodImg_src) {
        this.foodImg_src = foodImg_src;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public ArrayList<String> getFood_material() {
        return food_material;
    }

    public void setFood_material(ArrayList<String> food_material) {
        this.food_material = food_material;
    }

    public ArrayList<String> getCooking_way() {
        return cooking_way;
    }

    public void setCooking_way(ArrayList<String> cooking_way) {
        this.cooking_way = cooking_way;
    }
}
