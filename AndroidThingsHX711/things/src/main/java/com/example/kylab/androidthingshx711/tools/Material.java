package com.example.kylab.androidthingshx711.tools;

import android.graphics.Bitmap;
import android.media.Image;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Material implements Serializable {
    private String mId;
    private String mName;
    private String mUrl;
    private String mPhotoUrl;
    private String mcomment;
    private String mweight;
    private String mstartTime;
    private String mtime;
    private String mtype;
    private String mpercent;
    private String tareweight;
    private Bitmap img;

    public String getTareweight() {
        return tareweight;
    }

    public void setTareweight(String tareweight) {
        this.tareweight = tareweight;
    }



    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }

    public String getMcomment() {
        return mcomment;
    }

    public void setMcomment(String mcomment) {
        this.mcomment = mcomment;
    }

    public String getMweight() {
        return mweight;
    }

    public void setMweight(String mweight) {
        this.mweight = mweight;
    }

    public String getMstartTime() {
        return mstartTime;
    }

    public void setMstartTime(String mstartTime) {
        this.mstartTime = mstartTime;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public String getMtype() {
        return mtype;
    }

    public void setMtype(String mtype) {
        this.mtype = mtype;
    }

    public String getMpercent() {
        return mpercent;
    }

    public void setMpercent(String mpercent) {
        this.mpercent = mpercent;
    }



}

