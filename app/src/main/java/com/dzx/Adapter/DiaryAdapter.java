package com.dzx.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dzx.Event.DelDiaryEvent;
import com.dzx.R;
import com.dzx.Bean.diaryBean;
import com.dzx.Event.StartUpdateDiaryEvent;
import com.dzx.util.GetDate;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杜卓轩 on 2018/3/17.
 */

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>{

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<diaryBean> mDiaryBeanList,itemsCopy=new ArrayList<>();
    private int mEditPosition = -1;

    public DiaryAdapter(Context context, List<diaryBean> mmDiaryBeanList){
        mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mDiaryBeanList = mmDiaryBeanList;
        if(mmDiaryBeanList!=null) {
            itemsCopy.addAll(mmDiaryBeanList);
        }
    }
    @Override
    public DiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DiaryViewHolder(mLayoutInflater.inflate(R.layout.item_rv_diary, parent, false));
    }

    @Override
    public void onBindViewHolder(final DiaryViewHolder holder, final int position) {

        String dateSystem = GetDate.getDate().toString();
        if(mDiaryBeanList.get(position).getDate().equals(dateSystem)){
            holder.mIvCircle.setImageResource(R.drawable.circle_orange);
        }
        holder.mTvDate.setText(mDiaryBeanList.get(position).getDate());
        holder.mTvTitle.setText(mDiaryBeanList.get(position).getTitle());
       // if(mDiaryBeanList.get(position).getContent().length()<=10) {
        holder.mTvContent.setText("       " + mDiaryBeanList.get(position).getContent());
       // }
        //else {
         //   holder.mTvContent.setText("       " + mDiaryBeanList.get(position).getContent().substring(0,10));
       // }
        holder.mIvEdit.setVisibility(View.INVISIBLE);
        holder.mIvDel.setVisibility(View.INVISIBLE);
        if(mEditPosition == position){
            holder.mIvEdit.setVisibility(View.VISIBLE);
            holder.mIvDel.setVisibility(View.VISIBLE);
        }else {
            holder.mIvEdit.setVisibility(View.GONE);
            holder.mIvDel.setVisibility(View.GONE);
        }
        holder.mLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.mIvEdit.getVisibility() == View.VISIBLE){
                    holder.mIvEdit.setVisibility(View.GONE);
                }else {
                    holder.mIvEdit.setVisibility(View.VISIBLE);
                }
                if(holder.mIvDel.getVisibility() == View.VISIBLE){
                    holder.mIvDel.setVisibility(View.GONE);
                }else {
                    holder.mIvDel.setVisibility(View.VISIBLE);
                }
                if(mEditPosition != position){
                    notifyItemChanged(mEditPosition);
                }
                mEditPosition = position;
            }
        });

        holder.mIvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new StartUpdateDiaryEvent(position));
            }
        });
        holder.mIvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DelDiaryEvent(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDiaryBeanList.size();
    }

    public static class DiaryViewHolder extends RecyclerView.ViewHolder{

        TextView mTvDate;
        TextView mTvTitle;
        TextView mTvContent;
        ImageView mIvEdit;
        ImageView mIvDel;
        LinearLayout mLlTitle;
        LinearLayout mLl;
        ImageView mIvCircle;
        LinearLayout mLlControl;
        RelativeLayout mRlEdit;

        DiaryViewHolder(View view){
            super(view);
            mIvCircle = (ImageView) view.findViewById(R.id.main_iv_circle);
            mTvDate = (TextView) view.findViewById(R.id.main_tv_date);
            mTvTitle = (TextView) view.findViewById(R.id.main_tv_title);
            mTvContent = (TextView) view.findViewById(R.id.main_tv_content);
            mIvEdit = (ImageView) view.findViewById(R.id.main_iv_edit);
            mIvDel=(ImageView)view.findViewById(R.id.main_iv_del);
            mLlTitle = (LinearLayout) view.findViewById(R.id.main_ll_title);
            mLl = (LinearLayout) view.findViewById(R.id.item_ll);
            mLlControl = (LinearLayout) view.findViewById(R.id.item_ll_control);
            mRlEdit = (RelativeLayout) view.findViewById(R.id.item_rl_edit);
        }
    }

    public void filter(String text) {
        mDiaryBeanList.clear();
        if(text.isEmpty()){
            mDiaryBeanList.addAll(itemsCopy);
        } else{
            text = text.toLowerCase();
            for(diaryBean item: itemsCopy){
                if(item.getContent().toLowerCase().contains(text) || item.getDate().toLowerCase().contains(text)||
                        item.getTitle().toLowerCase().contains(text)){
                    mDiaryBeanList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }



}
