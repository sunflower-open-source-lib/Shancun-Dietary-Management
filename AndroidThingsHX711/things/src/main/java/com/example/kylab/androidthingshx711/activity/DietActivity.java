package com.example.kylab.androidthingshx711.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kylab.androidthingshx711.R;

public class DietActivity extends Activity {
    private TextView tv_time;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);
        iv_back = (ImageView) findViewById(R.id.iv_diet_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DietActivity.this.finish();
            }
        });
        tv_time = (TextView)findViewById(R.id.tv_diet_time);
        long sysTime = System.currentTimeMillis();
        CharSequence sysTimeStr = DateFormat.format("HH:mm ", sysTime);
        tv_time.setText(sysTimeStr);

    }
}
