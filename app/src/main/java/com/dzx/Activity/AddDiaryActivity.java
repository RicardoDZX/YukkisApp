package com.dzx.Activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dzx.LinedEditText;
import com.dzx.MainActivity;
import com.dzx.R;
import com.dzx.Recognization.online.CommonRecogParams;
import com.dzx.Recognization.online.OnlineRecogParams;
import com.dzx.Setting.Setting_use;
import com.dzx.fragment.FragmentOne;
import com.dzx.util.AppManager;
import com.dzx.util.GetDate;
import com.dzx.util.StatusBarCompat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import cc.trity.floatingactionbutton.FloatingActionsMenu;

import com.dzx.db.diaryDB;


/**
 * Created by 杜卓轩 on 2018/3/17.
 */

public class AddDiaryActivity extends ActivityRecog {
   /* @Bind(R.id.add_diary_tv_date)
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
    ImageView mCommonIvTest;*/


    private diaryDB mHelper;
    //private ActivityRecog m;

    public AddDiaryActivity() {
        super();
        settingActivityClass = Setting_use.class;
    }

    protected CommonRecogParams getApiParams() {
        return new OnlineRecogParams(this);
    }


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AddDiaryActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String title, String content) {
        Intent intent = new Intent(context, AddDiaryActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_diary);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();
        Intent intent = getIntent();
        mAddDiaryEtTitle.setText(intent.getStringExtra("title"));
        StatusBarCompat.compat(this, Color.parseColor("#161414"));

        mCommonTvTitle.setText("添加日记");
        mAddDiaryTvDate.setText("今天，" + GetDate.getDate());
        mAddDiaryEtContent.setText(intent.getStringExtra("content"));
        mHelper = new diaryDB(this, "Diary.db", null, 1);
    }


    @OnClick({R.id.common_iv_back, R.id.add_diary_et_title, R.id.add_diary_et_content, R.id.add_diary_fab_back, R.id.add_diary_fab_add
            //,R.id.add_diary_fab_voice
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_iv_back:
                FragmentOne.startActivity(this);
                finish();
            case R.id.add_diary_et_title:
                break;
            case R.id.add_diary_et_content:
                break;
            case R.id.add_diary_fab_back:
                String date = GetDate.getDate().toString();
                String tag = String.valueOf(System.currentTimeMillis());
                String title = mAddDiaryEtTitle.getText().toString() + "";
                String content = mAddDiaryEtContent.getText().toString() + "";
                if (!title.equals("") || !content.equals("")) {
                    SQLiteDatabase db = mHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("date", date);
                    values.put("title", title);
                    values.put("content", content);
                    values.put("tag", tag);
                    db.insert("Diary", null, values);
                    values.clear();
                }
                FragmentOne.startActivity(this);
                finish();
                break;
           /* case R.id.add_diary_fab_voice:
                click();
                int index = mAddDiaryEtContent.getSelectionStart();//获取光标所在位置
                //String text="I want to input str";
                Editable edit = mAddDiaryEtContent.getEditableText();//获取EditText的文字

                if (index < 0 || index >= edit.length() ){
                    edit.append(voice_content);
                    voice_content="";
                }else{
                    edit.insert(index,voice_content);//光标所在位置插入文字
                    voice_content="";
                }
                //txtResult.append(voice_content);
                break;*/
            case R.id.add_diary_fab_add:
                final String dateBack = GetDate.getDate().toString();
                final String titleBack = mAddDiaryEtTitle.getText().toString();
                final String contentBack = mAddDiaryEtContent.getText().toString();
                if(!titleBack.isEmpty() || !contentBack.isEmpty()){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("是否保存日记内容？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SQLiteDatabase db = mHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("date", dateBack);
                            values.put("title", titleBack);
                            values.put("content", contentBack);
                            db.insert("Diary", null, values);
                            values.clear();
                            FragmentOne.startActivity(AddDiaryActivity.this);
                            finish();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            FragmentOne.startActivity(AddDiaryActivity.this);
                        }
                    }).show();

                }else{
                    FragmentOne.startActivity(this);
                    finish();
                }
                break;
        }
    }

    @OnTouch({R.id.add_diary_fab_voice})
    public boolean onTouch(View view, MotionEvent event){
        switch (view.getId()) {
            case R.id.add_diary_fab_voice:

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    click();
                    //start();
                    //status=STATUS_WAITING_READY;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //stop();
                    //status=STATUS_STOPPED;
                   mProgressDialog=mProgressDialog.show(this,"请稍等","语音识别未完成");

                    //cancel();
                    //status=STATUS_NONE;
                }
                //click();
                /*switch (status) {
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
                        //status = STATUS_STOPPED; // 引擎识别中
                        //updateBtnTextByStatus();
                        //break;
                    //case STATUS_STOPPED: // 引擎识别中
                        cancel();
                        status = STATUS_NONE; // 识别结束，回到初始状态
                        //updateBtnTextByStatus();*/





        }
    return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentOne.startActivity(this);
       // finish();
    }
}
