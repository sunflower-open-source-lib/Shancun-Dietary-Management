package com.example.kylab.androidthingshx711.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.kylab.androidthingshx711.R;
import com.example.kylab.androidthingshx711.services.MyHttpService;
import com.example.kylab.androidthingshx711.tools.Material;
import com.example.kylab.androidthingshx711.tools.foodMenu;
import com.github.androidprogresslayout.ProgressLayout;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class MenuActivity extends BaseActivity {
    private Drawable bg_dra;
    private FrameLayout mFrameLayout;
    private RecyclerView mMenuRecycleview;
    private ImageView iv_back;
    private TextView mTime;
    private List<foodMenu> myMenus;
    private MenuAdapter mMenuAdapter;
    private MyHttpServiceConn connhttp;
    private MyHttpService.LoadfoodBinder mLoadfoodBinder;
    private MyReceiver mReceiver;
    private List<foodMenu> mMenus = new ArrayList<foodMenu>();


    private class MyHttpServiceConn implements ServiceConnection  {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //绑定操作http的服务
            mLoadfoodBinder = (MyHttpService.LoadfoodBinder)service;
            downloadMenu();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            myMenus = (List<foodMenu>)bundle.getSerializable("Menu");
                Log.d(TAG, "onReceive: size = " + myMenus.size());
            if(myMenus.size()>0) {
                mMenus.addAll(myMenus);
            }else{
                Toast.makeText(getApplicationContext(),"没有合适的菜谱呦",Toast.LENGTH_LONG)
                        .show();
            }
            if (mMenus.size()!=0) {
                Log.d(TAG, "onReceive: notifyDataSetChanged");
                mMenuAdapter.notifyDataSetChanged();

            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodmenu);
        connhttp = new MyHttpServiceConn();
        bindService(new Intent(this,MyHttpService.class),connhttp,BIND_AUTO_CREATE);
        Log.d("MenuActivity","bindService_MyHttpServiceConn");
        mReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.kylab.androidthingshx711.services.MyHttpService");
        MenuActivity.this.registerReceiver(mReceiver,filter);
        initview();
        /*/orderAdapter.setOnBaseClickListener(new OrderAdapter.OrderClickListener() {
            @Override
            public void onClick(int position, foodMenu order) {
                Toast.makeText(getApplicationContext(), "click num "+position, Toast.LENGTH_SHORT)
                        .show();
            }
        });*/
        iv_back = (ImageView)findViewById(R.id.iv_menu_back);
        mTime = (TextView)findViewById(R.id.tV_menu_time);
        long sysTime = System.currentTimeMillis();
        CharSequence sysTimeStr = DateFormat.format("HH:mm ", sysTime);
        mTime.setText(sysTimeStr);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuActivity.this.finish();
            }
        });
        mFrameLayout = (FrameLayout)findViewById(R.id.fl_background);
    }

    public void initview() {
        mMenuRecycleview = (RecyclerView) findViewById(R.id.menu_recycler_view);
        mMenuRecycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mMenuAdapter = new MenuAdapter(mMenus);
        mMenuRecycleview.setAdapter(mMenuAdapter);

    }

    private class MenuHolder extends RecyclerView.ViewHolder {
        public TextView mtitle,mmate,mdes;
        public ImageView mImg;
        public foodMenu mFoodMenu;
        public View view;
        public MenuHolder(View itemView) {
            super(itemView);
            view =itemView;
           //itemView.setOnClickListener(this);
            mtitle = (TextView)itemView.findViewById(R.id.list_item_menu_title);
            mImg = (ImageView)itemView.findViewById(R.id.list_item_menu_img);
            mmate = (TextView)itemView.findViewById(R.id.list_item_menu_material);
            mdes = (TextView)itemView.findViewById(R.id.list_item_menu_des);

        }
        public void bindMenu(foodMenu foodMenu){
            mFoodMenu = foodMenu;
            Log.d(TAG, "showMenuResponse: name "+mFoodMenu.getMenuname());
            Log.d(TAG, "showMenuResponse: img"+mFoodMenu.getMenuimg());
            Log.d(TAG, "showMenuResponse: material"+mFoodMenu.getFmaterial());
            Log.d(TAG, "showMenuResponse: fdesoo"+mFoodMenu.getDescribtion());
            mtitle.setText(mFoodMenu.getMenuname());
            mdes.setText(mFoodMenu.getDescribtion());
            mmate.setText(mFoodMenu.getFmaterial());
            loadImage(mFoodMenu.getMenuimg(),mImg);
        }



    }
    private class MenuAdapter extends RecyclerView.Adapter<MenuHolder>{
        private List<foodMenu> mFoodMenus;
        public MenuAdapter(List<foodMenu> foodmenus){
                this.mFoodMenus = foodmenus;
        }


        @Override
        public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            View view = layoutInflater.inflate(R.layout.list_item_menu,parent,false);
            return new MenuHolder(view);
        }

        @Override
        public void onBindViewHolder(MenuHolder holder, final int position) {
            final foodMenu foodmenu = mFoodMenus.get(position);
            Log.d(TAG, "onBindViewHolder: mFoodMenus unmber is "+foodmenu.getMenuimg());
            holder.mtitle.setText(foodmenu.getMenuname());
            holder.mdes.setText(foodmenu.getDescribtion());
            holder.mmate.setText(foodmenu.getFmaterial());
            loadImage(foodmenu.getMenuimg(),holder.mImg);


        }

        @Override
        public int getItemCount() {
            return mFoodMenus.size();
        }
    }



    @Override
    protected void setState() {

    }

    @Override
    public void updataUI(Message urData) {

    }

    @Override
    public void updataNoUI(int dataTpye, String data) {

    }
    public void downloadMenu(){
        mLoadfoodBinder.startLoadMenu();
        mLoadfoodBinder.getProess();
        Log.d("TAG","onServiceConnected by http");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        if(mLoadfoodBinder != null){
            if(connhttp!=null)
            {
                unbindService(connhttp);
            }
        }
        stopService(new Intent(MenuActivity.this,MyHttpService.class));

    }


    public void loadImage(String url, final ImageView img){
        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                img.setImageDrawable(resource);
                //bg_dra = resource;
            }
        };
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.lemon)
                .error(R.drawable.step_shown)
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(getApplicationContext())
                .load(url)
                .apply(options)
                .into(simpleTarget);





    }

}

