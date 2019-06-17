package com.example.administrator.newfridge.model.menus;

import com.google.gson.annotations.SerializedName;

public class MenuAuthor {
    @SerializedName("@type")
    private String type;
    private String name;
    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
