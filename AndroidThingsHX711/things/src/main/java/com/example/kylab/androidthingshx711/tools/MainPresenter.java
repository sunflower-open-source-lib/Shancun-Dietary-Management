package com.example.kylab.androidthingshx711.tools;

import android.os.Handler;
import android.os.Message;

import com.example.kylab.androidthingshx711.design.IMainView;

public class MainPresenter {
    public final static int MSG_SHOW_TIPS = 0x01;

    private IMainView mMainView;

    private ScreenProtectHandler mScreenProtectHandler;

    private boolean tipsIsShowed = true;
    private Runnable tipsShowRunnable = new Runnable(){

        @Override
        public void run() {
            mScreenProtectHandler.obtainMessage(MSG_SHOW_TIPS).sendToTarget();

        }
    };
    public MainPresenter(IMainView view){
        mMainView = view;
        mScreenProtectHandler = new ScreenProtectHandler();
    }
    public void startTipsTimer()
    {
        mScreenProtectHandler.postDelayed(tipsShowRunnable, 5000);
    }
    public void endTipsTimer()
    {
        mScreenProtectHandler.removeCallbacks(tipsShowRunnable);
    }
    public void resetTipsTimer()
    {
        tipsIsShowed = false;
        mScreenProtectHandler.removeCallbacks(tipsShowRunnable);
        mScreenProtectHandler.postDelayed(tipsShowRunnable, 60000);
    }
    public class ScreenProtectHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case MSG_SHOW_TIPS:
                    mMainView.showTipsView();
                    tipsIsShowed = true;
                    break;
            }
        }
    }




}


