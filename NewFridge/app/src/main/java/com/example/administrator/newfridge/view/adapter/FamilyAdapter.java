package com.example.administrator.newfridge.view.adapter;

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
import com.example.administrator.newfridge.model.familymodel.FamilyBean;

import java.util.List;


/**
 * @author LG32
 * 我的家庭列表适配器
 * @date 2019-02-01
 */
public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.FamilyViewHolder> {

    private Context mCotext;
    private List<FamilyBean> mList;
    private OnMsgClickListener onMsgClickListener;

    public interface OnMsgClickListener{
        void onClick(int position, FamilyBean FamilyBean);
    }

    public FamilyAdapter(Context mCotext, List<FamilyBean> mList) {
        this.mCotext = mCotext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public FamilyAdapter.FamilyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from ( mCotext ).inflate ( R.layout.item_familylist, parent, false );
        return new FamilyViewHolder ( view );
    }

    @Override
    public void onBindViewHolder(@NonNull FamilyViewHolder holder, final int position) {

        final FamilyBean familyBean = mList.get ( position );

        holder.tv_familyname.setText ( "家庭组：" + familyBean.getName () );

        holder.view.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (null != onMsgClickListener){
                    onMsgClickListener.onClick ( position, familyBean );
                }
            }
        } );
    }

    public void setOnMsgClickListener(OnMsgClickListener onMsgClickListener){
        this.onMsgClickListener = onMsgClickListener;
    }

    @Override
    public int getItemCount() {
        return mList.size ();
    }

    class FamilyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView iv_familyIcon;
        TextView tv_familyname;
        View view;

        FamilyViewHolder(View itemView) {
            super ( itemView );
            view = itemView;
            cardView = itemView.findViewById ( R.id.cv_family );
            iv_familyIcon = itemView.findViewById ( R.id.iv_familyIcon );
            tv_familyname = itemView.findViewById ( R.id.tv_family_name );

        }
    }
}
