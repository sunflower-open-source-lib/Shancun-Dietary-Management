package com.example.administrator.newfridge.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.administrator.newfridge.R;
import com.example.administrator.newfridge.model.AbilityBean;
import com.example.administrator.newfridge.view.adapter.AbilityMapView;

/**
 * @author RollingZ
 * 健康建议activity
 */
public class RecordActivity extends AppCompatActivity {
    private TextView mtextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("健康提示");
        setSupportActionBar(toolbar);
        mtextView = findViewById(R.id.jianyi);
        mtextView.setText("      1、能提供多种维生素：人体所需要的水溶性维生素如维生素C主要由新鲜蔬菜提供，此外，蔬菜还能提供叶酸、维生素K等营养元素。"+"\n"+
                "      2、提供矿物质元素：蔬菜是钾、钙、镁等矿物质元素的重要食物来源。多吃蔬菜对改善细胞代谢、提高体质有帮助。"+"\n"+
                "      3、蔬菜中膳食纤维改善肠道功能：蔬菜中的纤维素能有效促进肠与胃的蠕动，同时有利于肠道益生菌的增殖。"+"\n"+
                "      4、蔬菜中的植物化学物质具有重要的健康促进作用。");
        AbilityMapView abilitymapview = findViewById ( R.id.ability_map_view );
        abilitymapview.setData(new AbilityBean (80, 60, 60, 70, 80));
    }
}