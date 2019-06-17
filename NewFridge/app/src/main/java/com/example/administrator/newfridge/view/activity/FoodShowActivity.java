package com.example.administrator.newfridge.view.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.newfridge.R;
import com.example.administrator.newfridge.model.foodmodel.FoodBean;
import com.example.administrator.newfridge.model.foodmodel.FoodList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author LG32
 * 存储食品信息列表
 */
public class FoodShowActivity extends AppCompatActivity {

    @BindView(R.id.iv_food)
    ImageView ivFood;
    @BindView(R.id.tv_food_name)
    TextView tvFoodName;
    @BindView(R.id.tv_food_description)
    TextView tvFoodDescription;
    @BindView(R.id.tv_food_begin)
    TextView tvFoodBegin;
    @BindView(R.id.tv_food_end)
    TextView tvFoodEnd;
    @BindView(R.id.tv_food_deadline)
    TextView tvFoodDeadline;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.foodshow );
        ButterKnife.bind ( this );
        try {
            initValue ();
        } catch (ParseException e) {
            e.printStackTrace ();
        }
        initView ();
    }

    @SuppressLint("SetTextI18n")
    private void initValue() throws ParseException {

        Intent intent = getIntent ();
        int position = intent.getIntExtra ( "position", 0 );
        FoodList foodList = FoodList.getFoodList ();
        List<FoodBean> foodBeans = foodList.getList ();
        String type = null;

        switch ( foodBeans.get ( position ).getType () ){
            case "0":
                type = "保鲜";
                break;

            case "2":
                type = "冷藏";
                break;
        }

        tvFoodName.setText ( foodBeans.get ( position ).getComment () );
        tvFoodDescription.setText ( "食物类别: " + foodBeans.get ( position ).getFoodName () );
        tvFoodBegin.setText ( "开始存储时间： " + parseDate ( foodBeans.get ( position ).getStartTime () ) );
        tvFoodEnd.setText ( "保鲜时长：" + foodBeans.get ( position ).getTime () +"天");
        tvFoodDeadline.setText ( "保存类型：" + "保鲜" );

        Glide.with ( this ).load ( foodBeans.get ( position ).getFoodUrl () )
                .placeholder ( R.drawable.xiaomiao )
                .error ( R.drawable.error )
                .into ( ivFood );
    }

    private void initView() {
        Toolbar toolbar = findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );
    }

    public static String parseDate(String dateStr) throws ParseException {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        Date result;
        result = df.parse(dateStr);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone( TimeZone.getTimeZone("GMT"));
        return sdf.format(result);
    }
}
