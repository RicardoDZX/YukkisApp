package com.dzx.fragment;

import android.Manifest;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.dzx.R;
import com.dzx.Recognization.online.InFileStream;
import com.dzx.util.Logger;

import java.util.ArrayList;

/**
 * Created by 杜卓轩 on 2018/3/4.
 */

public abstract class Fragment_common extends Fragment {
    protected TextView txtLog;
    protected Button btn;
    //protected Button btn_stop;
    protected TextView txtResult;

    protected Handler handler;

    protected String descText;

    protected int layout = R.layout.fragment_2;

    protected Class settingActivityClass = null;

   // protected boolean running = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // setStrictMode();
        InFileStream.setContext(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_2, container, false);

        handler = new Handler() {

            /*
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                handleMsg(msg);
            }

        };
        Logger.setHandler(handler);
        initPermission();
        initView(rootView);
        initRecog();
        return rootView;
    }
    protected abstract void initRecog();

    protected void handleMsg(Message msg) {
        if (txtLog != null && msg.obj != null) {
           txtLog.append(msg.obj.toString() + "\n");

        }
    }

    protected void initView(View rootView) {
        txtResult = (TextView) rootView.findViewById(R.id.txtResult);
        txtLog = (TextView) rootView.findViewById(R.id.txtLog);
        btn = (Button) rootView.findViewById(R.id.btn);

        txtLog.setText(descText + "\n");
         //txtResult.setText("ceshi");
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getActivity(), perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.

            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), toApplyList.toArray(tmpList), 123);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }

    private void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());

    }
}
