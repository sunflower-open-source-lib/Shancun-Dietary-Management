package com.example.kylab.androidthingshx711.activity;

import com.example.kylab.androidthingshx711.design.MyUAObServer;
import com.example.kylab.androidthingshx711.services.MyHttpService;
import com.example.kylab.androidthingshx711.services.MyUaService;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;


/**
 * @author zhang
 * 9/21
 */
public abstract class BaseActivity extends Activity implements MyUAObServer {

    private MyUaServiceConn connuart;

    private MyUaService myuaService;

    boolean canUseUart,canUiShow,canUseHttp;
    boolean weightDataPrm,NFCDataPrm;

    public void setWeightDataPrm(boolean dataPrm) {
        this.weightDataPrm = dataPrm;
    }
    public void setNFCDataPrm(boolean commandPrm) {
        this.NFCDataPrm = commandPrm;
    }
    public MyUaService getMyuaService() {
        return myuaService;
    }

    @Override
    public boolean getWeightDataPrm() {
        return weightDataPrm;
    }
    @Override
    public boolean getNFCDataPrm() {
        return NFCDataPrm;
    }

    Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg!=null)
            {
                updataUI(msg);

            }
        }
    };


    /**
     * @param canUseUart 是否注册UART业务
     * */
    public void setCanUseUar(boolean canUseUart) {
        this.canUseUart = canUseUart;
    }

    /**
     * @param canUiShow    是否注册在页面显示DATA变化
     * */
    public void setCanUiShow(boolean canUiShow) {
        this.canUiShow = canUiShow;
    }
    /**
     * @param canUseHttp 是否注册WIFI业务
     * */
    public void setCanUseHttp(boolean canUseHttp) {
        this.canUseHttp = canUseHttp;
    }
//不同activity中重写此方法


    private class MyUaServiceConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            myuaService = ((MyUaService.LocalBinder)service).getService();
            Log.d("TAG","onServiceConnected by uart");
            myuaService.registerObserver ( BaseActivity.this );
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myuaService.removeObserver ( BaseActivity.this );
            Log.d("BAcivity","myuaService_onServiceDisconnected");
            myuaService = null;

        }
    }

    void  removeO()
    {
        myuaService.removeObserver ( BaseActivity.this );
        Log.d("BAcivity","myuaService_onServiceDisconnected");
        //myuaService = null;
    }


    /**
     *
     * @param dataType    是否注册在页面显示DATA变化
     * @param data
     *
     * */
    @Override
    public void updata(int dataType,String data) {

        if(canUiShow)
        {
            Message myMessage = new Message();
            myMessage.what = dataType;
            myMessage.obj = data;
            myhandler.sendMessage(myMessage);
        }
        updataNoUI(dataType,data);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setState();

        if (canUseUart) {
            connuart = new MyUaServiceConn();
            bindService(new Intent(this, MyUaService.class), connuart, BIND_AUTO_CREATE);
            Log.d("BActivity","bindService_MyUAServiceConn");
        }

    }

    /**
     *
     *setCanUseUartWifi(T/F) 是否读写串口 wifi
     *setCanUiShow(T/F) 是否开启页面显示
     *setCommandPrm(T/F) 是否接收命令
     *setDataPrm(T/F) 是否接受数据
     * */
    protected abstract void setState();




    public  void closeUaService()
    {
        if(myuaService!=null) {
            myuaService.stopMyService();
            //unbindUaService();
            Log.d("BAcivity","closeUAWFService!");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myuaService!=null){
            if(connuart!=null)
            {
                unbindService(connuart);
                Log.d("test","Send");
            }
        }



    }

    public abstract void updataUI(Message urData);
    public abstract void updataNoUI(int dataTpye,String data);





}
