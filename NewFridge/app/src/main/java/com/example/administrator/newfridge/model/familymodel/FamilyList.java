package com.example.administrator.newfridge.model.familymodel;

import java.util.ArrayList;

public class FamilyList {

    private ArrayList<FamilyBean> list;

    private static FamilyList familyList;

    private FamilyList(){}

    public static FamilyList getFamilyList(){
        if (familyList == null ){
            familyList = new FamilyList ();
            return familyList;
        }
        else
            return familyList;
    }

    public ArrayList<FamilyBean> getList() {
        return list;
    }

    public void setList(ArrayList<FamilyBean> list) {
        this.list = list;
    }

    public int size(){return list.size ();}

    public void clearList(){
        list.clear ();
    }
}
