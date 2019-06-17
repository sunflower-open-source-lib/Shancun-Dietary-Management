package com.example.administrator.newfridge.view.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.newfridge.R;
import com.example.administrator.newfridge.model.MenuInfoBean;
import com.example.administrator.newfridge.model.menus.MenuData;
import com.example.administrator.newfridge.model.menus.MenuList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LG32
 * 食谱信息页面
 */
@SuppressLint("Registered")
public class FoodMenuShowActivity extends AppCompatActivity {

    private MenuInfoBean menuInfoBean;
    private ArrayList<String> food_material = new ArrayList<> ();
    private ArrayList<String> cooking_way = new ArrayList<> ();
    private int position;
    private MenuData menuData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.happy_cook);
        Intent intent = getIntent ();
        position = intent.getIntExtra ("position", 0);
        initValue ();
        initView();
    }

    private void initValue(){
        MenuList menuList = MenuList.getMenuList ();
        List<MenuData> menus = menuList.getMenus ();
        menuData = menus.get ( position );
        menuInfoBean = new MenuInfoBean ();
        StringBuilder chief_material = new StringBuilder ( "主料：" );
        for(int i = 0; i < menuData.getRecipeIngredient ().size (); i++){
            chief_material.append ( menuData.getRecipeIngredient ().get ( i ) );
        }

        menuInfoBean.setMenu_name ( menuData.getName () );
        food_material.add ( chief_material.toString () );

        menuInfoBean.setFood_material ( food_material );
        cooking_way.add ( menuData.getRecipeInstructions () );
        menuInfoBean.setCooking_way ( cooking_way );
    }

    private void initView(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView iv_food = findViewById ( R.id.iv_food );
        TextView tv_food_name = findViewById ( R.id.tv_food_name );
        TextView tv_chief_material = findViewById ( R.id.tv_chief_material );
        TextView tv_cooking_way = findViewById ( R.id.tv_cooking_way );

        Glide.with ( this ).load ( menuData.getImage () )
                .placeholder ( R.drawable.error )
                .error ( R.drawable.error )
                .into ( iv_food );

        tv_food_name.setText ( menuInfoBean.getMenu_name () );
        tv_chief_material.setText ( food_material.get ( 0 ) );

        StringBuilder str = new StringBuilder ();
        for (int i = 0; i < cooking_way.size (); i++){
            str.append ( cooking_way.get ( i ) ).append ( "\n" );
        }
        tv_cooking_way.setText ( str.toString () );
    }
}
