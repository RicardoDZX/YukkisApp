package com.dzx.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.dzx.LinedEditText;
import com.dzx.R;
import com.dzx.Recognization.online.CommonRecogParams;
import com.dzx.Recognization.online.IStatus;
import com.dzx.Recognization.online.MessageStatusRecogListener;
import com.dzx.Recognization.online.StatusRecogListener;
import com.dzx.control.MyRecognizer;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cc.trity.floatingactionbutton.FloatingActionsMenu;

/**
 * 识别的基类Activity。封装了识别的大部分逻辑，包括MyRecognizer的初始化，资源释放、
 * <p>
 * 大致流程为
 * 1. 实例化MyRecognizer ,调用release方法前不可以实例化第二个。参数中需要开发者自行填写语音识别事件的回调类，实现开发者自身的业务逻辑
 * 2. 如果使用离线命令词功能，需要调用loadOfflineEngine。在线功能不需要。
 * 3. 根据识别的参数文档，或者demo中测试出的参数，组成json格式的字符串。调用 start 方法
 * 4. 在合适的时候，调用release释放资源。
 * <p>

 */

public abstract class ActivityRecog extends ActivityCommon implements IStatus {
    @Bind(R.id.add_diary_tv_date)
    TextView mAddDiaryTvDate;
    @Bind(R.id.add_diary_et_title)
    EditText mAddDiaryEtTitle;
    @Bind(R.id.add_diary_et_content)
    LinedEditText mAddDiaryEtContent;
    @Bind(R.id.add_diary_fab_back)
    cc.trity.floatingactionbutton.FloatingActionButton mAddDiaryFabBack;
    @Bind(R.id.add_diary_fab_add)
    cc.trity.floatingactionbutton.FloatingActionButton mAddDiaryFabAdd;

    @Bind(R.id.right_labels)
    FloatingActionsMenu mRightLabels;
    @Bind(R.id.common_tv_title)
    TextView mCommonTvTitle;
    @Bind(R.id.common_title_ll)
    LinearLayout mCommonTitleLl;
    @Bind(R.id.common_iv_back)
    ImageView mCommonIvBack;
    @Bind(R.id.common_iv_test)
    ImageView mCommonIvTest;
    public ProgressDialog mProgressDialog;


    /**
     * 识别控制器，使用MyRecognizer控制识别的流程
     */
    protected MyRecognizer myRecognizer;
     String voice_content="";
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
    private static final String TAG = "ActivityRecog";

    /**
     * 在onCreate中调用。初始化识别控制类MyRecognizer
     */




    protected void initRecog() {
        StatusRecogListener listener = new MessageStatusRecogListener(handler2);
        myRecognizer = new MyRecognizer(this, listener);
        apiParams = getApiParams();
        status = STATUS_NONE;

    }


    /**
     * 销毁时需要释放识别资源。
     */
    @Override
    protected void onDestroy() {
        myRecognizer.release();
        Log.i(TAG, "onDestory");
        super.onDestroy();
    }

    /**
     * 开始录音，点击“开始”按钮后调用。
     */
    protected void start() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ActivityRecog.this);
        //  上面的获取是为了生成下面的Map， 自己集成时可以忽略
        Map<String, Object> params = apiParams.fetch(sp);
        // 集成时不需要上面的代码，只需要params参数。
        // 这里打印出params， 填写至您自己的app中，直接调用下面这行代码即可。
        myRecognizer.start(params);
    }


    /**
     * 开始录音后，手动停止录音。SDK会识别在此过程中的录音。点击“停止”按钮后调用。
     */
    protected void stop() {
        myRecognizer.stop();
    }

    /**
     * 开始录音后，取消这次录音。SDK会取消本次识别，回到原始状态。点击“取消”按钮后调用。
     */
    protected void cancel() {
        myRecognizer.cancel();
    }


    /**
     * @return
     */
    protected abstract CommonRecogParams getApiParams();

    // 以上为 语音SDK调用，以下为UI部分
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }


    @Override
    protected void initView() {
        super.initView();
        click();

    }


    public  Handler handler_button = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    /*
                    语音识别完毕
                     */
                    //如果我不是对这个按钮操作
                    //而是弹出一个等待的对话框呢？？？？
                    //这个应该简单一点
                    //if(mProgressDialog!=null) {
                        mProgressDialog.dismiss();
                    //}
                    int index = mAddDiaryEtContent.getSelectionStart();//获取光标所在位置
                    Editable edit = mAddDiaryEtContent.getEditableText();//获取EditText的文字

                    if (index < 0 || index >= edit.length() ){
                        edit.append(voice_content);
                        voice_content="";
                    }else{
                        edit.insert(index,voice_content);//光标所在位置插入文字
                        voice_content="";
                    }
                    break;

                default:
                    break;
            }
        };
    };

    protected void click() {


        switch (status) {
            case STATUS_NONE: // 初始状态
                start();
                status = STATUS_WAITING_READY;
                //updateBtnTextByStatus();
                //txtLog.setText("");
                //txtResult.setText("");
                break;
            case STATUS_WAITING_READY: // 调用本类的start方法后，即输入START事件后，等待引擎准备完毕。
            case STATUS_READY: // 引擎准备完毕。
            case STATUS_SPEAKING:
            case STATUS_FINISHED: // 长语音情况
            case STATUS_RECOGNITION:
                stop();
                status = STATUS_STOPPED; // 引擎识别中

                //handler_button.sendEmptyMessage(1);
                //updateBtnTextByStatus();

                break;
            case STATUS_STOPPED: // 引擎识别中
                cancel();
                status = STATUS_NONE; // 识别结束，回到初始状态

                //updateBtnTextByStatus();
                break;
            default:
                break;
        }

    }



    protected void handleMsg(Message msg) {
        super.handleMsg(msg);

        switch (msg.what) { // 处理MessageStatusRecogListener中的状态回调
            case STATUS_FINISHED:
                if (msg.arg2 == 1) {
                     voice_content=msg.obj.toString();
                     txtResult.append(voice_content);
                     handler_button.sendEmptyMessage(1);
                }
                status = msg.what;
                //updateBtnTextByStatus();
                break;
            case STATUS_NONE:
            case STATUS_READY:
            case STATUS_SPEAKING:
            case STATUS_RECOGNITION:
                status = msg.what;
                //updateBtnTextByStatus();
                break;
            default:
                break;

        }
    }

    private void updateBtnTextByStatus() {
        switch (status) {
            case STATUS_NONE:

                //btn.setText("开始录音");
               // btn.setEnabled(true);

                //setting.setEnabled(true);
                break;
            case STATUS_WAITING_READY:
            case STATUS_READY:
            case STATUS_SPEAKING:
            case STATUS_RECOGNITION:
               // btn.setText("停止录音");
               // btn.setEnabled(true);
                //setting.setEnabled(false);
                mAddDiaryFabVoice.setColorNormal(0x24d63c);
                break;

            case STATUS_STOPPED:
                //btn.setText("取消整个识别过程");
               // btn.setEnabled(true);
                //setting.setEnabled(false);
                mAddDiaryFabVoice.setColorNormal(0xe41c1c);
                break;
            default:
                break;
        }
    }
}
