package com.example.administrator.newfridge.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.administrator.newfridge.R;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @author RollingZ
 * 欢迎页
 */
public class GuideActivity extends AppCompatActivity {
    private TextView mTvTime;
    private int mTime = 3;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mTvTime = findViewById(R.id.tv_guide_time);
        mTvTime.setText(mTime+" s");
        Observable.timer(1, TimeUnit.SECONDS)
                .repeat()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mTime--;
                        mTvTime.setText(mTime+" s");
                        if (mTime == 0) {
                            //倒计时结束
                            startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                });
    }
}
