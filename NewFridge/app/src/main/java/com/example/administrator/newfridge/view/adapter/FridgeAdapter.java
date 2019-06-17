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

import com.example.administrator.newfridge.R;
import com.example.administrator.newfridge.model.IceBox.IceBoxInfo;

import java.util.List;

public class FridgeAdapter extends RecyclerView.Adapter<FridgeAdapter.FridgeViewHolder> {

    private Context mCotext;
    private List<IceBoxInfo> mList;
    private FridgeAdapter.OnFridgeClickListener onFridgeClickListener;

    public FridgeAdapter(Context mCotext, List<IceBoxInfo> mList) {
        this.mCotext = mCotext;
        this.mList = mList;
    }

    public interface OnFridgeClickListener {
        void onClick(int position, List<IceBoxInfo> mList);
    }

    @NonNull
    @Override
    public FridgeAdapter.FridgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from ( mCotext ).inflate ( R.layout.item_fridge_list, parent, false );
        return new FridgeAdapter.FridgeViewHolder ( view );
    }

    @Override
    public void onBindViewHolder(@NonNull FridgeAdapter.FridgeViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final String iceId = "冰箱:" + mList.get ( position ).getIceName ();

        holder.tv_fridgeName.setText ( iceId );

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onFridgeClickListener) {
                    onFridgeClickListener.onClick(position, mList);
                }
            }
        });
    }

    public void setOnBaseClickListener(FridgeAdapter.OnFridgeClickListener onFridgeClickListener) {
        this.onFridgeClickListener = onFridgeClickListener;
    }

    @Override
    public int getItemCount() {
        return mList.size ();
    }

    class FridgeViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView iv_fridgeIcon;
        TextView tv_fridgeName;
        View view;

        FridgeViewHolder(View itemView) {
            super ( itemView );
            view = itemView;
            cardView = itemView.findViewById ( R.id.cv_fridge );
            iv_fridgeIcon = itemView.findViewById ( R.id.iv_fridge );
            tv_fridgeName = itemView.findViewById ( R.id.tv_fridgeName );

        }
    }

}