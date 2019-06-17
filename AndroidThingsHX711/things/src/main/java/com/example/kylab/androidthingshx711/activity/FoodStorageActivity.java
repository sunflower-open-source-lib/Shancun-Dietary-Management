package com.example.kylab.androidthingshx711.activity;


import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kylab.androidthingshx711.R;
import com.example.kylab.androidthingshx711.services.MyHttpService;
import com.example.kylab.androidthingshx711.tools.Material;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.support.constraint.Constraints.TAG;


public class FoodStorageActivity extends BaseActivity {

    private Button NFC_Button, QP_Button,ICE_Button,Fresh_Button,KX_Button;
    private TextView CDate, PWeight, mId,Weight;
    private EditText ed_des;
    private ImageView I_notify;
    private Material mMaterial = new Material();
    private boolean onqp = false;
    private MyHttpService.LoadfoodBinder mLoadfoodBinder;
    private MyHttpServiceConn mConn;
    private String temp ="0";
    private String NFC = "UUID:A4-B3-5E-F2";
    private MyReceiver mReceiver = null;
    private boolean isCardOffical = false;

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Material material = new Material();
            if (bundle != null) {
                material = (Material)bundle.getSerializable("material");
            }
            if (material != null) {
                if(material.getMweight().equals("0") ){
                    if(isCardOffical){
                        ed_des.setText(material.getMcomment());
                        KX_Button.setText(material.getName());
                       // KX_Button.setEnabled(false);
                    }
                }else{
                    ICE_Button.setEnabled(false);
                    Fresh_Button.setEnabled(false);
                    Toast.makeText(getApplicationContext(),"该卡已被占用，请先取出后在执行此操作",Toast.LENGTH_LONG)
                    .show();


                }
            }

        }
    }



    private class MyHttpServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //绑定操作http的服务
            mLoadfoodBinder = (MyHttpService.LoadfoodBinder)service;

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
        mReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.kylab.androidthingshx711.services.MyHttpService");
        FoodStorageActivity.this.registerReceiver(mReceiver,filter);
        setContentView(R.layout.activity_foodstorage);
        CDate = findViewById(R.id.fa_cdate);
        long sysTime = System.currentTimeMillis();
        CharSequence sysTimeStr = DateFormat.format("yyyy-MM-dd HH:mm:ss ", sysTime);
        CDate.setText(sysTimeStr);
//        InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        mMaterial.setMstartTime(sysTimeStr.toString());
        mMaterial.setId(NFC);
//        CDate.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                showDatePickerDig();
//            }
//        });

        NFC_Button = findViewById(R.id.fa_rdbu);
        QP_Button = findViewById(R.id.fa_qpbu);
        PWeight = findViewById(R.id.fa_pweight);
        Weight = findViewById(R.id.fa_weight);
        mId = findViewById(R.id.fa_uuid);
        ICE_Button = findViewById(R.id.fa_lcbu);
        Fresh_Button = findViewById(R.id.fa_bxbu);
        ed_des = (EditText)findViewById(R.id.fa_scbz);
        ed_des.clearFocus();


        ed_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: ed_des is onfocuse");
                ed_des.setFocusable(true);
                ed_des.requestFocus();
            }
        });
        I_notify = findViewById(R.id.imageView_notify);
        KX_Button = findViewById(R.id.fa_kxbu);
        KX_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });
        I_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodStorageActivity.this.finish ();
            }
        });
        NFC_Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("IceBox", "duqu is on");
                if (getMyuaService() != null) {
                    getMyuaService().sendMessagetoUA(1);
                    Log.d("IceBox", "duqu commend is sent");


                }
            }
        });
        QP_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("IceBox", "qupi is on");
                onqp = true;

            }
        });
        ICE_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterial.setMcomment(ed_des.getText().toString());
                Log.d(TAG, "onClick: ed_des.getText() is "+ed_des.getText().toString());
                mMaterial.setMtype("0");
                mMaterial.setMpercent("0.3");
                mMaterial.setMtime("3");
                if(!KX_Button.isEnabled()){
                    mMaterial.setName(KX_Button.getText().toString());
                }else{
                    mMaterial.setName("null0");
                }
                uploadfood(mMaterial);


            }
        });
        Fresh_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterial.setMcomment(ed_des.getText().toString());
                mMaterial.setMtype("1");
                mMaterial.setMpercent("0.3");
                mMaterial.setMtime("3");
                if(!KX_Button.isEnabled()){
                    mMaterial.setName(KX_Button.getText().toString());
                }else{
                    mMaterial.setName("null0");
                }
                uploadfood(mMaterial);

            }
        });



    }

    @Override
    protected void setState() {
        setCanUiShow(true);
        setCanUseUar(true);
        setWeightDataPrm(true);
        setNFCDataPrm(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onqp = false;
        if(mLoadfoodBinder != null){
            unbindWFService();
        }
        stopService(new Intent(FoodStorageActivity.this,MyHttpService.class));
        unregisterReceiver(mReceiver);
        removeO();

    }
    public void unbindWFService()
    {
        if(mConn!=null)
        {
            unbindService(mConn);
        }
    }


    @Override
    public void updataUI(Message urData) {

        switch (urData.what) {
            case 1:
                if(!onqp){
                    PWeight.setText("去皮" + urData.obj.toString() + "g");
                    temp = urData.obj.toString();

                }else{
                    int weight =  Integer.parseInt(urData.obj.toString());
                    //String pWeightText = urData.obj.toString().substring(2, 5);
                    String pWeightText = temp;
                    Log.d(TAG, "updataUI: pi zhong"+pWeightText);

                    int p =  Integer.parseInt(pWeightText);
                    Weight.setText((weight-p)+"g");
                    mMaterial.setMweight(String.valueOf(weight-p));
                    mMaterial.setTareweight(pWeightText);
                    Log.d(TAG, "updataUI:setTareweight "+mMaterial.getTareweight());
                }
                Log.d("FoodStorageActivity Pweight", "handlemessage: " + urData.obj.toString());
                break;
            case 2:
                String data = urData.obj.toString();
                if(data.length()==42){
                    isCardOffical = true;
                    Log.d("FoodStorageActivity", "this card is offical");
                }
                NFC = data.substring(data.length() -16,data.length()-5);
                mId.setText(NFC);
                mMaterial.setId(NFC);
                Log.d("FoodStorageActivity mid", "handlemessage: " + NFC);
                mLoadfoodBinder.startloaddetail(NFC);
                break;
            default:
                break;

        }

    }
    int yourChoice;
    private void showInputDialog() {
        final String[] items = { "牛肉","虾","西红柿","鸡肉","猪肉","鱼","虾","酸奶" };
        yourChoice = -1;
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(FoodStorageActivity.this);
        singleChoiceDialog.setTitle("快速选择");
        // 第二个参数是默认选项，此处设置为0
//        singleChoiceDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                yourChoice = position;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        singleChoiceDialog.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: which = "+which);
                        yourChoice = which;
                    }

                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (yourChoice != -1) {
//                            Toast.makeText(FoodStorageActivity.this,
//                                    "你选择了" + items[yourChoice],
//                                    Toast.LENGTH_SHORT).show();
                            ed_des.setText(items[yourChoice]);
                        }
                    }
                });
        singleChoiceDialog.show();
    }

//    public void showDatePickerDig(){
//        Calendar calendar = Calendar.getInstance();
//        String Sdate = CDate.getText().toString();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        Date date = null;
//        try {
//            date = sdf.parse(Sdate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        calendar.setTime(date);
//       int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                int realMonth = monthOfYear+1;
//                mMaterial.setMstartTime(new GregorianCalendar(year,monthOfYear,dayOfMonth).getTime());
//                CDate.setText(year + "-" + realMonth+ "-" + dayOfMonth);
//            }
//        },year,month,day);
//        datePickerDialog.show();
//
//    }
    @Override
    public void updataNoUI(int dataTpye, String data) {


    }
    public void uploadfood(Material material){
        mLoadfoodBinder.startuploadfood(material);
        Log.d("TAG","onServiceConnected by http");
    }

}
