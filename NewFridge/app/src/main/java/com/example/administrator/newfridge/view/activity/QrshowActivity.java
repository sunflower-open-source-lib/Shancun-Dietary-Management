package com.example.administrator.newfridge.view.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.newfridge.R;
import com.example.administrator.newfridge.tool.nfc.widget.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class QrshowActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_family_name)
    TextView tvFamilyName;
    @BindView(R.id.iv_qrimage)
    ImageView ivQrimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_qrcodeshow );
        ButterKnife.bind ( this );
        Intent intent = getIntent ();
        String familyName = intent.getStringExtra ( "familyName" );
        String urlParam = intent.getStringExtra ( "urlParam" );
        String url = "http://gavinwang.cn/" + urlParam;
        initToolbar (familyName);
        initQrImage ( url );

    }

    @Override
    public void onClick(View v) {

    }


    @SuppressLint("ResourceType")
    private void initToolbar(String familyName) {
        toolbar.setTitle ( "家庭二维码" );
        tvFamilyName.setText ( familyName );
        setSupportActionBar ( toolbar );
        //设置返回键可用
        getSupportActionBar ().setHomeButtonEnabled ( true );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        toolbar.setNavigationOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish ();
            }
        } );
    }

    private void initQrImage(String url) {
        Glide.with ( this ).load ( url )
                .placeholder ( R.drawable.xiaomiao )
                .error ( R.drawable.error )
                .into ( ivQrimage );
    }
}
