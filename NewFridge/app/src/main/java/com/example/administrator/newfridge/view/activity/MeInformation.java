package com.example.administrator.newfridge.view.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.administrator.newfridge.R;

/**
 * @author RollingZ
 * 展现我的信息页面
 */
public class MeInformation extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meinformation);
        Toolbar toolbar = findViewById ( R.id.toolbar );
        toolbar.setTitle("个人信息");
        setSupportActionBar ( toolbar );
    }
}
