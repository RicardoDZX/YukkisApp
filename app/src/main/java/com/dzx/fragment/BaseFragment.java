package com.dzx.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dzx.Constants;
import com.dzx.R;
/**
 * Created by 杜卓轩 on 2018/2/28.
 */


/**
 * LayoutInflater是布局填充器，将xml文件转换成view对象。
 * ViewGroup 表示fragment插入activity中的布局视图对象。
 * Bundle表示存储上一个fragment的信息
 */
public class BaseFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false);


        /*
        可以在这里添加一些点击事件。在xml文件中添加id
        然后
        relativeLayout layout=view.findViewById(R.id.xml的id)
        layout.setOnClickListener(new OnClickListener(){
        public void onClick()})
         */
       /* TextView mFragmentText = (TextView) view.findViewById(R.id.fragment_1_text);
        Bundle bundle = getArguments();
        String args = bundle.getString(Constants.KEY_ARGS);
        mFragmentText.setText(args);*/
        return view;


    }
}
