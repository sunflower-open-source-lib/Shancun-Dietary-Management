package com.example.administrator.newfridge.model.IceBox;

import java.util.List;

public class IceBoxBody {
    private String msg;
    private int code;
    private List<IceBoxInfo> data;

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

    public void setData(List<IceBoxInfo> data) {
        this.data = data;
    }
    public List<IceBoxInfo> getData() {
        return data;
    }
}
