package com.example.administrator.newfridge.model.menus;

import java.util.List;

public class MenuBody {

    private String msg;
    private int code;
    private List<String> data;
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
    public List<String> getData() {
        return data;
    }
}
