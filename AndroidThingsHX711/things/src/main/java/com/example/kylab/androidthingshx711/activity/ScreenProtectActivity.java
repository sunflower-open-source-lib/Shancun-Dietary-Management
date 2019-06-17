package com.example.kylab.androidthingshx711.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kylab.androidthingshx711.R;

import static android.content.ContentValues.TAG;

public class ScreenProtectActivity extends BaseActivity implements View.OnClickListener{
    private static final int MSGKEY = 0x10001;

    private long exitTime = 0;
    private TextView mTime,mDate;
    private Button mButton;
    private View mbackground;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenprotect);
        mbackground = findViewById(R.id.ll_backgroung);
        mTime = (TextView)findViewById(R.id.tv_time);
        //mButton = (Button)findViewById(R.id.bt_showdetail);
        //mButton.setOnClickListener(this);
        mDate = (TextView)findViewById(R.id.tv_date);
        mbackground.setOnClickListener(this);
        new TimeThread().start();
    }

    @Override
    protected void setState() {

    }

    @Override
    public void updataUI(Message sxurData) {

    }

    @Override
    public void updataNoUI(int dataTpye, String data) {

    }

    @Override
    public void onClick(View v) {
        //if(v.getId() == R.id.bt_showdetail){
            //Log.d(TAG, "onClick: 点击详情");
        //}
        //else{
            Log.d(TAG, "onClick: 点击了其他部位");
            Toast.makeText(getApplicationContext(), "结束屏保", Toast.LENGTH_SHORT).show();
            finish();
        //}
    }

    public class TimeThread extends Thread {

        @Override
        public synchronized void start() {
            super.start();
        }

        @Override
        public void run() {
            do {
                try {

                    //更新时间
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = MSGKEY;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)
//    {
////        if (keyCode == KeyEvent.KEYCODE_ENTER)
////
////            exit();
////            return false;
////
////        return super.onKeyDown(keyCode, event);
//        Log.d(TAG, "onKeyDown: on");
//        finish();
//        return false;
//    }
//
//    public void exit()
//    {
//        if ((System.currentTimeMillis() - exitTime) > 2000)
//        {
//            Toast.makeText(getApplicationContext(), "再按一次解锁键即可进入应用", Toast.LENGTH_SHORT).show();
//            exitTime = System.currentTimeMillis();
//        }
//        else
//        {
//            finish();
//        }
//    }
    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case MSGKEY:
                    long sysTime = System.currentTimeMillis();
                    CharSequence sysTimeStr1 = DateFormat.format("hh:mm", sysTime);
                    CharSequence sysTimeStr2 = DateFormat.format("yyyy年MM月dd日", sysTime);
                    mTime.setText(sysTimeStr1);
                    mDate.setText(sysTimeStr2+" 星期四");
                    break;

            }
        }
    };

}
