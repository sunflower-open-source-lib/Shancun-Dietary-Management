package com.example.administrator.newfridge.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.newfridge.R;
import com.example.administrator.newfridge.model.menus.MenuData;

import java.util.List;

/**
 * @author RollingZ
 * 食谱菜单列表
 */
public class FoodMenuAdapter extends RecyclerView.Adapter<FoodMenuAdapter.FoodViewHolder> {

    private Context mContext;
    private List<MenuData> mList;
    private FoodMenuAdapter.OnFoodClickListener onFoodClickListener;

    public interface OnFoodClickListener {
        void onClick(int position, MenuData menuData);
    }


    public FoodMenuAdapter(Context mContext, List<MenuData> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_foodmenu, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final MenuData menuData = mList.get(position);
            holder.tv_title.setText(menuData.getName ());

        Glide.with ( mContext ).load ( menuData.getImage () )
                .placeholder ( R.drawable.error )
                .error ( R.drawable.error )
                .into ( holder.imageView );

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onFoodClickListener) {
                    onFoodClickListener.onClick(position, menuData);
                }
            }
        });
    }

    public void setOnBaseClickListener(FoodMenuAdapter.OnFoodClickListener onFoodClickListener) {
        this.onFoodClickListener = onFoodClickListener;
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv_title;
        View view;

        FoodViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = itemView.findViewById(R.id.iv_cover1);
            tv_title = itemView.findViewById(R.id.tv_title1);
        }
    }
}

