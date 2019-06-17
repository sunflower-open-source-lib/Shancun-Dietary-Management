package com.example.administrator.newfridge.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.administrator.newfridge.R;
import com.example.administrator.newfridge.model.foodmodel.FoodBean;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @author RollingZ LG32
 * @date 2018/12/12
 */

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private Context mCotext;
    private List<FoodBean> mList;
    private OnFoodClickListener onFoodClickListener;
    private boolean mIsStaggered = false;

    public interface OnFoodClickListener {
        void onClick(int position, FoodBean foodBean);
    }


    public FoodAdapter(Context mCotext, List<FoodBean> mList) {
        this.mCotext = mCotext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from ( mCotext ).inflate ( R.layout.item_foodfragment, parent, false );
        return new FoodViewHolder ( view );
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final FoodBean foodBean = mList.get ( position );
        String type = null;
//        holder.tv_author.setText(foodModel.);
        holder.tv_title.setText ( "食物名称：" + foodBean.getComment () );
        holder.tv_nature.setText ( "食物种类：" + foodBean.getFoodName () );
        holder.tv_startTime.setText ( "存放日期：" + dateFormat ( foodBean.getStartTime () ) );
        holder.tv_endTime.setText ( "保质长度：" + foodBean.getTime () + "天" );
        holder.tv_weight.setText ( "重量：" + foodBean.getWeight () + "克" );

        if (foodBean.getType ().equals ( "1" )){
            type = "保鲜";
        }else if(foodBean.getType ().equals ( "2" )){
            type = "冷藏";
        }
        holder.tv_type.setText( type );
        Glide.with ( mCotext ).load ( foodBean.getFoodUrl () )
                .placeholder ( R.drawable.xiaomiao )
                .error ( R.drawable.error )
                .into ( holder.imageView );

        holder.view.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (null != onFoodClickListener) {
                    onFoodClickListener.onClick ( position, foodBean );
                }
            }
        } );
    }

    public void setOnFoodClickListener(OnFoodClickListener onFoodClickListener) {
        this.onFoodClickListener = onFoodClickListener;
    }

    public void setmIsStaggered(boolean mIsStaggered) {
        this.mIsStaggered = mIsStaggered;
    }

    @Override
    public int getItemCount() {
        return mList.size ();
    }

    public void setmList(List<FoodBean> mList) {
        this.mList = mList;
    }

    class FoodViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView tv_title;
        TextView tv_nature;
        TextView tv_endTime;
        TextView tv_startTime;
        TextView tv_weight;
        TextView tv_type;
        View view;

        FoodViewHolder(View itemView) {
            super ( itemView );
            view = itemView;
            cardView = itemView.findViewById ( R.id.shiyan );
            imageView = itemView.findViewById ( R.id.iv_cover );
            tv_title = itemView.findViewById ( R.id.tv_title );
            tv_nature = itemView.findViewById ( R.id.tv_nature );
            tv_endTime = itemView.findViewById ( R.id.tv_endTime );
            tv_startTime = itemView.findViewById ( R.id.tv_startTime );
            tv_weight = itemView.findViewById ( R.id.tv_weight );
            tv_type = itemView.findViewById ( R.id.tv_type );
        }
    }

    private String dateFormat(String date){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd'T'HH:mm:ss" );
        try {
            Date date1 = format.parse ( date );
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );
            return sdf.format ( date1 );
        } catch (ParseException e) {
            e.printStackTrace ();
            return null;
        }
    }
}
