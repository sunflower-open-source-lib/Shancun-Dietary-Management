package com.example.kylab.androidthingshx711.activity;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.kylab.androidthingshx711.R;
import com.example.kylab.androidthingshx711.services.MyHttpService;
import com.example.kylab.androidthingshx711.tools.Material;
import com.example.kylab.androidthingshx711.tools.MaterialLab;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class FoodManageActivity extends BaseActivity {
    private MyHttpServiceConn connhttp;
    private MyHttpService.LoadfoodBinder mLoadfoodBinder;
    private RecyclerView mMaterialRecyclerView;
    private MaterialAdapter mAdapter;
    private TextView tx_cdate,tx_qdate,tx_amount,tx_time;
    private ImageView iv_back,iv_show,iv_overtime;
    private Button bt_refresh,bt_add;
    private RequestListener<Drawable> requestListener = new RequestListener<Drawable>() {

        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            Log.d(TAG, "onException: " + e.toString()+"  model:"+model+" isFirstResource: "+isFirstResource);
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            Log.e(TAG,  "model:"+model+" isFirstResource: "+isFirstResource);
            return false;
        }
    };

    public void loadImage(String url,ImageView img){
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.lemon)
                .error(R.drawable.outline_info_white_48dp)
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(getApplicationContext())
                .load(url)
                .apply(options)
                .listener(requestListener)
                .into(img);
    }
    private class MyHttpServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //绑定操作http的服务
            mLoadfoodBinder = (MyHttpService.LoadfoodBinder)service;
            invokinghttp();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connhttp = new MyHttpServiceConn();
        bindService(new Intent(this,MyHttpService.class),connhttp,BIND_AUTO_CREATE);
        Log.d("MActivity","bindService_MyHttpServiceConn");
        setContentView(R.layout.activity_foodmanage);
        iv_back = (ImageView) findViewById(R.id.imageView_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodManageActivity.this.finish();
            }
        });
        mMaterialRecyclerView = (RecyclerView) findViewById(R.id.material_recycler_view);
        mMaterialRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        iv_show = (ImageView) findViewById(R.id.imageView_image);
        iv_overtime = (ImageView)findViewById(R.id.Imgview_overtime);
        tx_cdate = (TextView)findViewById(R.id.fa_cdate);
        tx_qdate = (TextView)findViewById(R.id.fa_quality_date);
        tx_amount = (TextView)findViewById(R.id.fa_amount);
        tx_time = (TextView)findViewById(R.id.textView_time);
        long sysTime = System.currentTimeMillis();
        CharSequence sysTimeStr = DateFormat.format("HH:mm ", sysTime);
        tx_time.setText(sysTimeStr);
        bt_refresh = (Button) findViewById(R.id.bt_refresh);
        bt_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               invokinghttp();
                mAdapter.notifyDataSetChanged();
            }
        });
        bt_add = (Button)findViewById(R.id.bt_addfood);
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FoodManageActivity.this,FoodStorageActivity.class);
                startActivity(i);
            }
        });
        updateUI();

    }

    @Override
    public void updataUI(Message urData) {

    }

    @Override
    public void updataNoUI(int dataTpye, String data) {

    }

    @Override
    protected void setState() {
        setCanUiShow(true);
        setCanUseUar(false);
        setWeightDataPrm(false);
        setNFCDataPrm(false);

    }
    public void invokinghttp(){
        mLoadfoodBinder.startDownload();
        mLoadfoodBinder.getProess();
        Log.d("TAG","onServiceConnected by http");
    }
    private void updateUI(){
        MaterialLab materialLab = MaterialLab.get(this);
        List<Material> materials = materialLab.getMaterials();
        Log.d("aaa", "updateUI:materials size is"+materials.size());
        mAdapter = new MaterialAdapter(materials);
        mMaterialRecyclerView.setAdapter(mAdapter);
    }
    private class MaterialHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public Material mMaterial;
        public TextView mFoodNameView;
        public TextView mFoodDateView;
        public Button mFoodHandleButton;
        public ImageView mFoodImage,mstateImage;
        public MaterialHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mstateImage = (ImageView)itemView.findViewById(R.id.image_state_View);
            mFoodNameView = (TextView)itemView.findViewById(R.id.list_item_material_name_text_view);
            mFoodDateView = (TextView)itemView.findViewById(R.id.list_item_material_date_text_view);
            mFoodHandleButton = (Button) itemView.findViewById(R.id.list_item_material_handle_button);
            mFoodImage = (ImageView)itemView.findViewById(R.id.list_item_material_Image);
            mFoodHandleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FoodManageActivity.this,FoodExtractionActivity.class);
                    startActivity(intent);
                }
            });

        }


        public void bindMaterial(Material material){
            mMaterial = material;
            mFoodNameView.setText(mMaterial.getMcomment());
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            String date = sdf.format(mMaterial.getMstartTime());
            mFoodDateView.setText(mMaterial.getMstartTime().substring(0,10));
//            Date mdate = new Date();
            mFoodHandleButton.setBackgroundResource(R.drawable.yellow_out_button);//判断一下类型
            mstateImage.setImageResource(R.drawable.alert_tag_1);//判断一下类型

            Log.d(TAG, "bindMaterial: geturl = "+mMaterial.getUrl());
            String url = mMaterial.getUrl();
            loadImage(url,mFoodImage);
            Log.d(TAG, "bindMaterial: is over");


        }

        @Override
        public void onClick(View v) {
           // Toast.makeText(getApplicationContext(),mMaterial.getName()+" clicked!",Toast.LENGTH_SHORT).show();
            switch(v.getId()){
                case R.id.list_item_material_handle_button:

                    break;
                default:
                    tx_cdate.setText(mFoodDateView.getText());
                    tx_qdate.setText(mMaterial.getMtime()+"天");
                    tx_amount.setText(mMaterial.getMweight()+" g");
                    iv_show.setImageDrawable(mFoodImage.getDrawable());
                    iv_overtime.setImageResource(R.drawable.expir_image);//判断具体选则输出什么
                    break;

            }


        }
    }
    private class MaterialAdapter extends RecyclerView.Adapter<MaterialHolder>{
        private List<Material> mMaterials;
        public MaterialAdapter(List<Material> materials){
            mMaterials = materials;
        }
        @NonNull
        @Override
        public MaterialHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            View view = layoutInflater.inflate(R.layout.list_item_material,parent,false);
            return new MaterialHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MaterialHolder holder, int position) {
            Material material = mMaterials.get(position);
            Log.d(TAG, "onBindViewHolder: mMaterials unmber is "+mMaterials.size());
            holder.bindMaterial(material);
        }



        @Override
        public int getItemCount() {
            Log.d("aaa", "getItemCount:mMaterials size is"+mMaterials.size());
            return mMaterials.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLoadfoodBinder != null){
            unbindWFService();
        }
    }
    public void unbindWFService()
    {
        if(connhttp!=null)
        {
            unbindService(connhttp);
        }
    }
}

