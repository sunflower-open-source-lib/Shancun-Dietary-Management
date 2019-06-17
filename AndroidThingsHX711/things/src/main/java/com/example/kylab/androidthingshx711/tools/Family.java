package com.example.kylab.androidthingshx711.tools;

import java.io.Serializable;

public class Family implements Serializable{
    private String fid;
    private String manager;
    private String common;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }
}
