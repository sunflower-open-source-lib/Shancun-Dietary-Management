package com.example.kylab.androidthingshx711.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kylab.androidthingshx711.R;
import com.example.kylab.androidthingshx711.services.MyHttpService;
import com.example.kylab.androidthingshx711.tools.Material;
import com.example.kylab.androidthingshx711.tools.MaterialLab;

import java.util.List;

import static android.content.ContentValues.TAG;

public class FoodExtractionActivity extends BaseActivity {
    private MaterialLab mMaterials = MaterialLab.get(this);
    private ImageView I_notify;
    private ProgressDialog waitingDialog;
    private TextView muuid,mweight,mdate,mdescribe,mmode,mpweight;
    private Button mfinish;
    private MyHttpServiceConn connhttp;
    private MyHttpService.LoadfoodBinder mLoadfoodBinder;
    private MyReceiver  receiver = null;
    private Material mMaterial;
    String NfcData = "0000000000000000";
    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
             mMaterial = (Material)bundle.getSerializable("material");
             if(mMaterial.getMweight() == "0"){
                 Toast.makeText(getApplicationContext(),"冰箱中没有该食物",Toast.LENGTH_SHORT).show();
             }else{
                 mweight.setText(mMaterial.getMweight()+"g");
                 mpweight.setText("去皮 "+mMaterial.getTareweight()+"g");
                 mdate.setText(mMaterial.getMstartTime().substring(0,10));
                 mdescribe.setText(mMaterial.getName()+"  "+mMaterial.getMcomment());
                 if(mMaterial.getMtype().equals("1")){
                     mmode.setText("保鲜");
                 } else{
                     mmode.setText("冷藏");
                 }
                 httpDeleteFood(NfcData);
             }




        }
    }
    private class MyHttpServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //绑定操作http的服务
            mLoadfoodBinder = (MyHttpService.LoadfoodBinder)service;
            Log.d("EActivity","bindService_MyHttpServiceConn");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_extraction);
        connhttp = new MyHttpServiceConn();
        bindService(new Intent(this,MyHttpService.class),connhttp,BIND_AUTO_CREATE);
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.kylab.androidthingshx711.services.MyHttpService");
        FoodExtractionActivity.this.registerReceiver(receiver,filter);
        showWaitingDialog();
        muuid = findViewById(R.id.fa_uuid);
        mweight = findViewById(R.id.fa_weight);
        mpweight = findViewById(R.id.fa_pweight);
        mdate = findViewById(R.id.fa_cdate);
        mdescribe = findViewById(R.id.fa_scbz);
        mmode = findViewById(R.id.bx_tv);

        I_notify = findViewById(R.id.imageView_notify);
        I_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodExtractionActivity.this.finish();
            }
        });
        mfinish =(Button) findViewById(R.id.fa_bxbu);
        mfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodExtractionActivity.this.finish();
            }
        });
    }
    @Override
    protected void setState() {
        setCanUiShow(true);
        setCanUseUar(true);
        setWeightDataPrm(false);
        setNFCDataPrm(true);

    }

    @Override
    public void updataUI(Message urData) {
        switch (urData.what) {
            case 2:

                Log.d("FoodStorageActivity NFCnum", "handlemessage: " + urData.obj.toString());
                String Data = urData.obj.toString();
                NfcData = Data.substring(Data.length() -16,Data.length()-5);
                Log.d("FoodStorageActivity NFCnum", "handlemessage: " +NfcData);

                muuid.setText(NfcData);

                Log.d(TAG, "updataUI: Materials size1 = "+mMaterials.MaterialSize());
                httpGetAFood(NfcData);
                break;
            default:
                break;

        }
    }
    @Override

    public void updataNoUI(int dataTpye, String data) {

    }
    public void httpGetAFood(String uuid){
            mLoadfoodBinder.startloaddetail(uuid);
            Log.d("TAG","httpGetAFood by http");
        }
    public void httpDeleteFood(String uuid){
        mLoadfoodBinder.deleteFood(uuid);
        Log.d("TAG","httpGetAFood by http");
    }



    private void showWaitingDialog(){

        waitingDialog= new ProgressDialog(FoodExtractionActivity.this);
        waitingDialog.setTitle("Waiting for NFC number");
        waitingDialog.setMessage("请将食材放在平台上...");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(true);
        waitingDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                   waitingDialog.dismiss();
                Log.d(TAG, "onClick: quxiao is on ");
                FoodExtractionActivity.this.finish();
                Log.d(TAG, "onClick: quxiao is on ");
            }
        });
        waitingDialog.setButton(ProgressDialog.BUTTON_NEUTRAL,"我已放好", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: getnfc is on ");
                if (getMyuaService() != null) {
                    getMyuaService().sendMessagetoUA(1);
                    Log.d(TAG, "command 1 is send");
                }
            }
        });
        waitingDialog.show();


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLoadfoodBinder != null){
            unbindWFService();
        }
        unregisterReceiver(receiver);
        stopService(new Intent(FoodExtractionActivity.this,MyHttpService.class));
        Log.d(TAG, "onClick: destory is on ");
        removeO();


    }
    public void unbindWFService()
    {
        if(connhttp!=null)
        {
            unbindService(connhttp);
        }
    }

}
