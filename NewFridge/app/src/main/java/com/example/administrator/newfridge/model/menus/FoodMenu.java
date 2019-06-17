package com.example.administrator.newfridge.model.menus;

/**
 * @author RollingZ
 * 食谱信息列表
 * Created by Administrator on 2018/4/18.
 * 已失效2019/4/3
 */

public class FoodMenu {
    private int ImageView;
    private String title;

    public FoodMenu(int imageView, String title) {
        ImageView = imageView;
        this.title = title;
    }

    public int getImageView() {
        return ImageView;
    }

    public void setImageView(int imageView) {
        ImageView = imageView;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
