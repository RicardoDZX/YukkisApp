package com.dzx.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dzx.Recognization.online.CommonRecogParams;
import com.dzx.Recognization.online.OnlineRecogParams;
import com.dzx.Setting.Setting_use;

/**
 * Created by 杜卓轩 on 2018/3/4.
 */

public class FragmentCommon extends Fragment_recog {




    public FragmentCommon() {
        super();
        settingActivityClass = Setting_use.class;
    }

    @Override
    protected CommonRecogParams getApiParams() {
        return new OnlineRecogParams(getActivity());
    }

    public static String  TAG = FragmentCommon.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(TAG, "onAttach");
    }
    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestory");
        super.onDestroy();
    }
    @Override
    public void onDetach() {
        Log.i(TAG, "onDetch");
        super.onDetach();
    }
    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView");
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated");
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }
}
