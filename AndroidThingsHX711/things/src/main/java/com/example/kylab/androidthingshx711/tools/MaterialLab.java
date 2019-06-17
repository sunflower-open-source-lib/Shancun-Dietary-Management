package com.example.kylab.androidthingshx711.tools;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MaterialLab {
    private  static MaterialLab sMaterialLab;
    private List<Material> mMaterials;

    public static MaterialLab get(Context context){
        if(sMaterialLab == null){
            sMaterialLab = new MaterialLab(context);
        }
        return sMaterialLab;
    }

    public  MaterialLab (Context context){
        mMaterials = new ArrayList<>();
    }
    public void addMaterial(Material a){
        mMaterials.add(a);
    }
    public int MaterialSize(){
       return mMaterials.size();
    }
    public List<Material> getMaterials(){
        return mMaterials;
    }
    public Material getMaterial(String id){
        for(Material material:mMaterials){
            if(material.getId().equals(id)){
                return material;
            }
        }
        return  null;
    }
    public void cleanMaterial(){
        mMaterials.clear();
    }
}
