package com.example.kylab.androidthingshx711.tools;

import java.io.Serializable;

public class foodMenu implements Serializable {
    private String Menuname;
    private String Menuimg;
    private String Fmaterial;
    private String Describtion;

    public String getMenuimg() {
        return Menuimg;
    }

    public void setMenuimg(String menuimg) {
        Menuimg = menuimg;
    }



    public String getMenuname() {
        return Menuname;
    }

    public void setMenuname(String menuname) {
        Menuname = menuname;
    }

    public String getFmaterial() {
        return Fmaterial;
    }

    public void setFmaterial(String fmaterial) {
        Fmaterial = fmaterial;
    }

    public String getDescribtion() {
        return Describtion;
    }

    public void setDescribtion(String describtion) {
        Describtion = describtion;
    }
}
