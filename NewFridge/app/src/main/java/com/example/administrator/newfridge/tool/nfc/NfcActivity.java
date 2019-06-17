package com.example.administrator.newfridge.tool.nfc;


import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.administrator.newfridge.R;
import com.example.administrator.newfridge.tool.nfc.widget.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class NfcActivity extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout selectDate;
    private TextView currentDate;
    private CustomDatePicker customDatePicker1;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;
    private TextView Name;
    private Toolbar toolbar;
    private Button mButton1;
    private Button mButton2;
    private String xixi = new String();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        Name = findViewById(R.id.name);
        selectDate =  findViewById(R.id.selectDate);
        selectDate.setOnClickListener(this);
        currentDate =  findViewById(R.id.currentDate);
        initDatePicker();
        mNfcAdapter =NfcAdapter.getDefaultAdapter(this);
        toolbar = findViewById(R.id.toolbar);

        if (mNfcAdapter == null) {
            Name.setText("不支持NFC。");
        }


        mPendingIntent =PendingIntent.getActivity(this, 0,
                new Intent(this, getClass())
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefIntent.addDataType("*/*");
            mIntentFilters = new IntentFilter[] { ndefIntent };
        } catch (Exception e) {
            Log.e("TagDispatch",e.toString());
        }

        mNFCTechLists = new String[][] { new String[] { NfcF.class.getName() } };
        initToolbar();
        mButton1 = findViewById(R.id.quxiao);
        mButton2 = findViewById(R.id.queding);
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NfcActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    /**
     * 对toolbar进行设置
     */
    public void initToolbar(){
        toolbar.setTitle("增加菜品");
        setSupportActionBar(toolbar);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onNewIntent(Intent intent){
        String action =intent.getAction();
        Tag tag =intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        String s = action +"\n\n" + tag.toString();


        Parcelable[] data =intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        if (data != null) {
            try {
                for (int i= 0; i < data.length; i++) {
                    NdefRecord[] recs = ((NdefMessage)data[i]).getRecords();
                    for(int j = 0; j < recs.length; j++) {
                        if(recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                                Arrays.equals(recs[j].getType(),NdefRecord.RTD_TEXT)) {

                            byte[] payload = recs[j].getPayload();
                            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" :"UTF-16";
                            int langCodeLen = payload[0] & 0077;

                            s+= ("\n\nNdefMessage[" + i + "], NdefRecord[" + j +"]:\n\"" +
                                    new String(payload, langCodeLen + 1,
                                            payload.length- langCodeLen - 1, textEncoding) +
                                    "\"");
                            xixi+= new String(payload, langCodeLen + 1,
                                    payload.length- langCodeLen - 1, textEncoding);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("TagDispatch",e.toString());
            }

        }
        Name.setText(xixi);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent,mIntentFilters, mNFCTechLists);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectDate:
                // 日期格式为yyyy-MM-dd
                customDatePicker1.show(currentDate.getText().toString());
                break;
        }
    }

    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        currentDate.setText(now.split(" ")[0]);
        //currentTime.setText(now);

        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                currentDate.setText(time.split(" ")[0]);
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(false); // 不显示时和分
        customDatePicker1.setIsLoop(true); // 不允许循环滚动
    }
}
