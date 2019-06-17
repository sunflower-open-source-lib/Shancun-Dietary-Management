package com.example.kylab.androidthingshx711.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;

import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.kylab.androidthingshx711.R;
import com.example.kylab.androidthingshx711.services.MyHttpService;
import com.example.kylab.androidthingshx711.tools.Family;
import com.example.kylab.androidthingshx711.tools.Material;

import static android.support.constraint.Constraints.TAG;

public class SetActivity extends BaseActivity {
    private TextView mManage,mMember,mtime;
    private ImageView fa_code;
    private Button fa_finish;
    private MyHttpService.LoadfoodBinder mLoadfoodBinder;
    private MyHttpServiceConn connhttp;
    private MyReceiver receiver = null;
    private Family mFamily = new Family();
    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            mFamily = (Family) bundle.getSerializable("Family");
            mManage.setText(mFamily.getManager());
            mMember.setText(mFamily.getCommon());
            String url = mFamily.getFid();
            loadImage(url,fa_code);

        }
    }
    private class MyHttpServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //绑定操作http的服务
            mLoadfoodBinder = (MyHttpService.LoadfoodBinder)service;
            httpGetFamily();
            Log.d("EActivity","bindService_MyHttpServiceConn");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icebox_message);
        connhttp = new MyHttpServiceConn();
        bindService(new Intent(this,MyHttpService.class),connhttp,BIND_AUTO_CREATE);
        mManage = (TextView)findViewById(R.id.fa_manage);
        mMember = (TextView)findViewById(R.id.fa_member);
        mtime = (TextView)findViewById(R.id.tv_message__time);
        long sysTime = System.currentTimeMillis();
        CharSequence sysTimeStr = DateFormat.format("HH:mm ", sysTime);
        mtime.setText(sysTimeStr);

        fa_code = (ImageView)findViewById(R.id.Iv_family);
        fa_finish =(Button)findViewById(R.id.fa_fhbu);
        fa_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetActivity.this.finish();
            }
        });

    }

    @Override
    protected void onResume() {
        receiver = new SetActivity.MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.kylab.androidthingshx711.services.MyHttpService");
        SetActivity.this.registerReceiver(receiver,filter);
        super.onResume();

    }

    @Override
    protected void setState() {
        setCanUiShow(true);
        setCanUseUar(false);
        setWeightDataPrm(false);
        setNFCDataPrm(false);
    }
    public void httpGetFamily(){
        mLoadfoodBinder.downloadFamily();
        Log.d("TAG","httpGetAFood by http");
    }
    @Override
    public void updataUI(Message urData) {

    }

    @Override
    public void updataNoUI(int dataTpye, String data) {

    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        Log.d(TAG, "onPause: executed");
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        if(mLoadfoodBinder != null){
            unbindWFService();
        }

        stopService(new Intent(SetActivity.this,MyHttpService.class));
        super.onDestroy();

    }
    public void unbindWFService()
    {
        if(connhttp!=null)
        {
            unbindService(connhttp);
        }
    }
    public void loadImage(String url,ImageView img){
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.lemon)
                .error(R.drawable.outline_info_white_48dp)
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(getApplicationContext())
                .load(url)
                .apply(options)
                .into(img);
    }

}
