package com.example.kylab.androidthingshx711.activity;

import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.example.kylab.androidthingshx711.R;
import com.example.kylab.androidthingshx711.design.IMainView;
import com.example.kylab.androidthingshx711.services.MyHttpService;
import com.example.kylab.androidthingshx711.services.MyService;
import com.example.kylab.androidthingshx711.tools.MainPresenter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.support.constraint.Constraints.TAG;

public class MainActivity extends BaseActivity implements IMainView{
    Button scc_button,scq_button,notify_button,cp_button,yszs_button;
    private ImageView iv_setting,iv_protect;
    private MainPresenter mPresenter;
    private MyHttpService.LoadfoodBinder mLoadfoodBinder;
    private MyHttpServiceConn mConn;


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
        mConn = new MyHttpServiceConn();
        bindService(new Intent(this,MyHttpService.class),mConn,BIND_AUTO_CREATE);
        Log.d("MActivity","bindService_MyHttpServiceConn");
        setContentView(R.layout.activity_main);
        mPresenter = new MainPresenter(this);
        iv_protect = findViewById(R.id.imageView_minfo);
        iv_protect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTipsView();
            }
        });
        iv_setting = findViewById(R.id.miv_setting);
        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: set");
                Intent intent = new Intent(MainActivity.this, SetActivity.class);
                startActivity(intent);
            }
        });
        cp_button = findViewById(R.id.button_cp);
        cp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,MenuActivity.class);
                startActivity(i);
            }
        });


        yszs_button = findViewById(R.id.button_yszs);
        yszs_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,DietActivity.class);
                startActivity(i);
            }
        });
        scc_button = findViewById(R.id.button_scc);
        scc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,FoodStorageActivity.class);
                startActivity(i);
            }
        });
        scq_button = findViewById(R.id.button_scq);
        scq_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,FoodExtractionActivity.class);
                startActivity(i);
            }
        });

        notify_button = findViewById(R.id.Bt_notify);
        notify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,FoodManageActivity.class);
                startActivity(i);
            }
        });
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        mPresenter.resetTipsTimer();
        return false;
    }
    @Override
    protected void onResume()
    {
        //启动默认开始计时
        mPresenter.startTipsTimer();
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        //有其他操作时结束计时
        mPresenter.endTipsTimer();
        super.onPause();
    }
    @Override
    protected void setState() {
        setCanUiShow(true);
        setCanUseUar(true);
        setWeightDataPrm(false);
        setNFCDataPrm(false);
        setCanUseHttp(true);
    }

    @Override
    public void updataUI(Message urData) {

    }

    @Override
    public void updataNoUI(int dataTpye, String data) {


    }


    @Override
    public void showTipsView()
    {
        //展示屏保界面
        Intent intent = new Intent(MainActivity.this, ScreenProtectActivity.class);
        startActivity(intent);
    }
    public void invokinghttp(){
        mLoadfoodBinder.startDownload();
        mLoadfoodBinder.getProess();
        Log.d("TAG","onServiceConnected by http");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLoadfoodBinder != null){
            if(mConn!=null)
            {
                unbindService(mConn);
            }
        }
    }

}
