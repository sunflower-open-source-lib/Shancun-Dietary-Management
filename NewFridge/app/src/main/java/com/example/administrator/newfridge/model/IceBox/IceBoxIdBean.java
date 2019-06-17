package com.example.administrator.newfridge.model.IceBox;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LG32
 * 2018/12/09
 * 存储icebox_id,单例模式
 */
public class IceBoxIdBean {

    private List<IceBoxInfo> iceId_list ;

    private static IceBoxIdBean iceBoxIdBean;

    private IceBoxIdBean(){
    }

    public static IceBoxIdBean getIceBoxIdBean(){
        if (iceBoxIdBean == null){
            iceBoxIdBean = new IceBoxIdBean ();
            return iceBoxIdBean;
        }else
            return iceBoxIdBean;
    }

    public List<IceBoxInfo> getIceId_list() {
        return iceId_list;
    }

    public void setIceId_list(List<IceBoxInfo> iceId_list) {
        this.iceId_list = iceId_list;
    }

    public void add(IceBoxInfo id){
        iceId_list.add ( id );
    }

    public void clear(){
        if (iceId_list != null){
            iceId_list.clear ();
        }
    }
}
