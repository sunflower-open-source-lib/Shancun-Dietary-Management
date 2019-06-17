package com.example.administrator.newfridge.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.administrator.newfridge.R;
import com.example.administrator.newfridge.model.IceBox.IceBoxIdBean;
import com.example.administrator.newfridge.model.IceBox.IceBoxInfo;
import com.example.administrator.newfridge.model.menus.MenuBody;
import com.example.administrator.newfridge.model.menus.MenuData;
import com.example.administrator.newfridge.model.menus.MenuList;
import com.example.administrator.newfridge.okhttp.PostMenuRq;
import com.example.administrator.newfridge.tool.MyHandlerMsg;
import com.example.administrator.newfridge.view.adapter.EmptyRecyclerView;
import com.example.administrator.newfridge.view.adapter.FoodMenuAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * @author RollingZ
 * 食谱信息列表
 */
public class FoodMenuActivity extends AppCompatActivity {

    protected Context mContext;
    protected EmptyRecyclerView mRecyclerView;
    @BindView(R.id.loading)
    ProgressBar loading;
    private View mEmptyView;
    protected FoodMenuAdapter mFoodAdapter;
    private MenuList menuList = MenuList.getMenuList ();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_foodmenu );
        ButterKnife.bind ( this );
        Toolbar toolbar = findViewById ( R.id.toolbar );
        toolbar.setTitle("食谱推荐");
        setSupportActionBar ( toolbar );
        initView ();
    }

    public void initView() {
        mRecyclerView = findViewById ( R.id.fragment_recyclerview1 );
        mEmptyView = findViewById ( R.id.empty_view1 );
        mRecyclerView.setLayoutManager ( new LinearLayoutManager ( mContext, LinearLayoutManager.VERTICAL, false ) );
        getMenusInfo ();
    }

    public void initData(List<MenuData> menus) {

        if (loading.getVisibility () == View.VISIBLE)
            loading.setVisibility ( View.GONE );
        mFoodAdapter = new FoodMenuAdapter ( this, menus );
        mFoodAdapter.setOnBaseClickListener ( new FoodMenuAdapter.OnFoodClickListener () {
            @Override
            public void onClick(int position, MenuData menuData) {
                Intent intent = new Intent ();
                intent.setClass ( FoodMenuActivity.this, FoodMenuShowActivity.class );
                intent.putExtra ( "position", position );
                startActivity ( intent );
            }
        } );

        mRecyclerView.setAdapter ( mFoodAdapter );
        mRecyclerView.setmEmptyView ( mEmptyView );
        mRecyclerView.hideEmptyView ();
    }

    public void getMenusInfo() {
        if (loading.getVisibility () == View.GONE)
            loading.setVisibility ( View.VISIBLE );
        MyHandler myHandler = new MyHandler ();
        IceBoxIdBean iceBoxIdBean = IceBoxIdBean.getIceBoxIdBean ();
        List<IceBoxInfo> mList = iceBoxIdBean.getIceId_list ();
        if(mList.size () > 0) {
            String macip = mList.get ( 0 ).getIceId ();
            Log.i ( "get", "getMenusInfo: " + macip );
            RequestBody requestBody = new FormBody.Builder ()
                    .add ( "macip", macip )
                    .build ();

            new PostMenuRq ( requestBody, myHandler );
        }else {
            Toast.makeText ( this, "未绑定冰箱", Toast.LENGTH_SHORT )
                    .show ();
        }
    }

    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler implements MyHandlerMsg {

        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MENUS_SUCCESS:
                    String json = (String) msg.obj;
                    jsonTool ( json );
                    break;

                case REQUEST_FAIL:
                    Toast.makeText ( FoodMenuActivity.this, "网络请求失败", Toast.LENGTH_SHORT )
                            .show ();
                    if (loading.getVisibility () == View.VISIBLE)
                        loading.setVisibility ( View.GONE );
                    break;
            }
        }
    }

    private void jsonTool(String json) {

        Gson gson = new GsonBuilder ()
                .setPrettyPrinting ()
                .disableHtmlEscaping ()
                .create ();

        MenuBody body = gson.fromJson ( json, MenuBody.class );
        List<String> menuList = body.getData ();
        List<MenuData> menus = new ArrayList<> ();

        Log.i ( "MenusData", "jsonTool: " + body.getData () );

        for (int i = 0; i < menuList.size (); i++) {
            String temp = menuList.get ( i );
            MenuData menu = gson.fromJson ( temp, MenuData.class );
            menus.add ( menu );
        }
        this.menuList.setMenus ( menus );
        initData ( menus );
    }
}
