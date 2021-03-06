package com.dzx.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;


import com.dzx.Recognization.online.CommonRecogParams;
import com.dzx.Recognization.online.IStatus;
import com.dzx.Recognization.online.MessageStatusRecogListener;
import com.dzx.Recognization.online.StatusRecogListener;
import com.dzx.control.MyRecognizer;
import com.dzx.util.Logger;

import java.util.Map;

/**
 * Created by 杜卓轩 on 2018/3/4.
 */

public abstract class Fragment_recog extends Fragment_common implements IStatus {


        /**
         * 识别控制器，使用MyRecognizer控制识别的流程
         */
        protected MyRecognizer myRecognizer;

        /*
         * Api的参数类，仅仅用于生成调用START的json字符串，本身与SDK的调用无关
         */
        protected CommonRecogParams apiParams;

        /*
         * 本Activity中是否需要调用离线命令词功能。根据此参数，判断是否需要调用SDK的ASR_KWS_LOAD_ENGINE事件
         */
        protected boolean enableOffline = false;


        /**
         * 控制UI按钮的状态
         */
        protected int status;

        /**
         * 日志使用
         */
        private static final String TAG = "Fragment_recog";

        /**
         * 在onCreate中调用。初始化识别控制类MyRecognizer
         */
        protected void initRecog() {
            StatusRecogListener listener = new MessageStatusRecogListener(handler);
            myRecognizer = new MyRecognizer(getActivity(),  listener);
            apiParams = getApiParams();
            status = STATUS_NONE;

        }


        /**
         * 销毁时需要释放识别资源。
         */
        @Override
        public void onDestroy() {
            myRecognizer.release();
            Log.i(TAG, "onDestory");
            super.onDestroy();
        }
        @Override
        public void onStop(){

            super.onStop();
        }

        public void onStart(){
           // start();
            super.onStart();
        //目前的结论是，锁屏时调用release，把asr注销，isinited=false
            //导致了再次开启屏幕时，报nullpointer exception
            //asr.send函数报错。
        }

        public void onPause(){
            super.onPause();
            //myRecognizer.release();
            //这个有没有用。
            Logger.info(TAG, "进行了release");
        }

        /**
         * 开始录音，点击“开始”按钮后调用。
         */
        protected void start() {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            //  上面的获取是为了生成下面的Map， 自己集成时可以忽略
            Map<String, Object> params = apiParams.fetch(sp);
            // 集成时不需要上面的代码，只需要params参数。
            // 这里打印出params， 填写至您自己的app中，直接调用下面这行代码即可。
            myRecognizer.start(params);
        }


        /**
         * 开始录音后，手动停止录音。SDK会识别在此过程中的录音。点击“停止”按钮后调用。
         */
        private void stop() {
            myRecognizer.stop();
        }

        /**
         * 开始录音后，取消这次录音。SDK会取消本次识别，回到原始状态。点击“取消”按钮后调用。
         */
        private void cancel() {
            myRecognizer.cancel();
        }


        /**
         * @return
         */
        protected abstract CommonRecogParams getApiParams();

        // 以上为 语音SDK调用，以下为UI部分
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }


        @Override
        protected void initView(View rootView) {
            super.initView(rootView);
            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    switch (status) {
                        case STATUS_NONE: // 初始状态
                            start();
                            status = STATUS_WAITING_READY;
                            updateBtnTextByStatus();
                            txtLog.setText("");
                            txtResult.setText("");
                            break;
                        case STATUS_WAITING_READY: // 调用本类的start方法后，即输入START事件后，等待引擎准备完毕。
                        case STATUS_READY: // 引擎准备完毕。
                        case STATUS_SPEAKING:
                        case STATUS_FINISHED: // 长语音情况
                        case STATUS_RECOGNITION:
                            stop();
                            status = STATUS_STOPPED; // 引擎识别中
                            updateBtnTextByStatus();
                            break;
                        case STATUS_STOPPED: // 引擎识别中
                            cancel();
                            status = STATUS_NONE; // 识别结束，回到初始状态
                            updateBtnTextByStatus();
                            break;
                        default:
                            break;
                    }

                }
            });
        }

        protected void handleMsg(Message msg) {
            super.handleMsg(msg);

            switch (msg.what) { // 处理MessageStatusRecogListener中的状态回调
                case STATUS_FINISHED:
                   if (msg.arg2 == 1) {
                       txtResult.setText(msg.obj.toString());
                    }
                    status = msg.what;
                    updateBtnTextByStatus();
                    break;
                case STATUS_NONE:
                case STATUS_READY:
                case STATUS_SPEAKING:
                case STATUS_RECOGNITION:
                    status = msg.what;
                    updateBtnTextByStatus();
                    break;
                default:

                    break;

            }
        }

        private void updateBtnTextByStatus() {
            switch (status) {
                case STATUS_NONE:
                    btn.setText("开始录音");
                    btn.setEnabled(true);
                    //setting.setEnabled(true);
                    break;
                case STATUS_WAITING_READY:
                case STATUS_READY:
                case STATUS_SPEAKING:
                case STATUS_RECOGNITION:
                    btn.setText("停止录音");
                    btn.setEnabled(true);
                    //setting.setEnabled(false);
                    break;

                case STATUS_STOPPED:
                    btn.setText("取消整个识别过程");
                    btn.setEnabled(true);
                   // setting.setEnabled(false);
                    break;
                default:
                    break;
            }
        }
    }

