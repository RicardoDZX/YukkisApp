package com.dzx.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dzx.Bean.weatherInfoBean;
import com.dzx.R;

import java.util.List;

/**
 * Created by 杜卓轩 on 2018/3/10.
 */

public class weatherInfoViewAdapter extends BaseAdapter {


    private Context context;
    private List<weatherInfoBean> lists;

    public weatherInfoViewAdapter(Context context, List<weatherInfoBean> lists) {
        super();
        this.context = context;
        this.lists = lists;
    }

    /**
     * 是否是自己发送的消息
     *
     * @author cyf
     *
     */
    public static interface IMsgViewType {
        int IMVT_COM_MSG = 0;// 收到对方的消息
        int IMVT_TO_MSG = 1;// 自己发送出去的消息
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return lists.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return lists.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    /**
     * 得到Item的类型，是对方发过来的消息，还是自己发送出去的
     */
    public int getItemViewType(int position) {
        weatherInfoBean entity = lists.get(position);

        if (entity.isMeSend()) {// 收到的消息
            return IMsgViewType.IMVT_COM_MSG;
        } else {// 自己发送的消息
            return IMsgViewType.IMVT_TO_MSG;
        }
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        HolderView holderView = null;
        weatherInfoBean entity = lists.get(arg0);
        boolean isMeSend = entity.isMeSend();
        boolean isWeather=entity.isWeather();
        boolean isNetwork=entity.getisNet();
        if (holderView == null) {
            holderView = new HolderView();
            if (!isMeSend) {
                if(isWeather) {
                    arg1 = View.inflate(context, R.layout.left_msg,
                            null);
                    holderView.tv_chat_me_message = (TextView) arg1
                            .findViewById(R.id.tv_left);
                    holderView.tv_chat_me_message.setText(" 城市: "+entity.getParent_city()+"\n"+" 地点: " + entity.getCity() + "\n" + " 日期: " + entity.getDate() +
                            "\n" + " 温度: " + entity.getTemp() + "--" + entity.getTemp_max() + "\n" + " 天气: " + entity.getWeather() + "\n" + " 风向: " + entity.getWind());
                }
                else{
                    arg1 = View.inflate(context, R.layout.left_msg,
                            null);
                    holderView.tv_chat_me_message = (TextView) arg1
                            .findViewById(R.id.tv_left);
                    holderView.tv_chat_me_message.setText(entity.getTips());
                }
            } else {
                if(isNetwork) {
                    arg1 = View.inflate(context, R.layout.right_msg,
                            null);
                    holderView.tv_chat_me_message = (TextView) arg1
                            .findViewById(R.id.tv_right);
                    holderView.tv_chat_me_message.setText(entity.getInfo());
                }
                else {
                    arg1 = View.inflate(context, R.layout.right_msg,
                            null);
                    holderView.tv_chat_me_message = (TextView) arg1
                            .findViewById(R.id.tv_right);
                    holderView.tv_chat_me_message.setText(entity.getNetInfo());
                }
            }
            arg1.setTag(holderView);
        } else {
            holderView = (HolderView) arg1.getTag();
        }

        return arg1;
    }

    class HolderView {
        TextView tv_chat_me_message;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }





}
