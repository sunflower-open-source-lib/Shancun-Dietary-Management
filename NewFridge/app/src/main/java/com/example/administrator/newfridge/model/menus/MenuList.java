package com.example.administrator.newfridge.model.menus;

import org.w3c.dom.ls.LSInput;

import java.util.List;

public class MenuList {

    private static volatile MenuList menuList;

    private List<MenuData> menus;

    private MenuList(){}

    public static MenuList getMenuList(){

        if ( menuList == null){
            synchronized (MenuList.class){
                if ( menuList == null){
                    menuList = new MenuList ();
                }
            }
        }
        return menuList;
    }

    public void setMenus(List<MenuData> menus){
        this.menus = menus;
    }

    public List getMenus(){
        return menus;
    }
}
