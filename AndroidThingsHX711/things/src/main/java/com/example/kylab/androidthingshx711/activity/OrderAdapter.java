package com.example.kylab.androidthingshx711.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.kylab.androidthingshx711.R;
import com.example.kylab.androidthingshx711.tools.foodMenu;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.orderViewHolder> {

    private Context mContext;
    private List<foodMenu> mList;
    private OrderAdapter.OrderClickListener orderClickListener;

    public interface OrderClickListener {
        void onClick(int position, foodMenu order);
    }


    public OrderAdapter(Context mContext, List<foodMenu> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public orderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_menu, parent, false);
        return new orderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(orderViewHolder holder, final int position) {
        final foodMenu orderone = mList.get(position);
        holder.mtitle.setText(orderone.getMenuname());
        holder.mdes.setText(orderone.getDescribtion());
        holder.mmate.setText(orderone.getFmaterial());
        loadImage(mContext,orderone.getMenuimg(),holder.mImg);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != orderClickListener) {
                    orderClickListener.onClick(position, orderone);
                }
            }
        });
    }

    public void setOnBaseClickListener(OrderAdapter.OrderClickListener orderClickListener) {
        this.orderClickListener = orderClickListener;
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setmList(List<foodMenu> mList) {
        this.mList = mList;
    }

    class orderViewHolder extends RecyclerView.ViewHolder {
       // private foodMenu mFoodMenu;
        public TextView mtitle, mmate, mdes;
        public ImageView mImg;
        public View view;

        public orderViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            mtitle = (TextView) itemView.findViewById(R.id.list_item_menu_title);
            mImg = (ImageView) itemView.findViewById(R.id.list_item_menu_img);
            mmate = (TextView) itemView.findViewById(R.id.list_item_menu_material);
            mdes = (TextView) itemView.findViewById(R.id.list_item_menu_des);
        }
    }
    public void loadImage(Context context,String url,ImageView img){
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.lemon)
                .error(R.drawable.outline_info_white_48dp)
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(context)
                .load(url)
                .apply(options)
                .into(img);
    }
}

