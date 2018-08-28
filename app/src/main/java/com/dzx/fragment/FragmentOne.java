package com.dzx.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.dzx.Activity.AddDiaryActivity;
import com.dzx.Activity.UpdateDiaryActivity;
import com.dzx.Adapter.DiaryAdapter;
import com.dzx.Bean.diaryBean;
import com.dzx.Constants;
import com.dzx.Event.DelDiaryEvent;
import com.dzx.Event.StartUpdateDiaryEvent;
import com.dzx.MainActivity;
import com.dzx.R;
import com.dzx.db.diaryDB;
import com.dzx.util.AppManager;
import com.dzx.util.GetDate;
import com.dzx.util.SpHelper;
import com.dzx.util.StatusBarCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 杜卓轩 on 2018/2/28.
 */




//public class FragmentOne extends BaseFragment {
public class FragmentOne extends Fragment {
    /*public static FragmentOne newInstance(String s){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_ARGS,s);
        FragmentOne fragment = new FragmentOne();
        fragment.setArguments(bundle);
        return fragment;
    }*/




    //@Bind(R.id.common_iv_back)
    //ImageView mCommonIvBack;
    //@Bind(R.id.common_tv_title)
   // TextView mCommonTvTitle;
  //  @Bind(R.id.common_iv_test)
    //ImageView mCommonIvTest;
    //@Bind(R.id.common_title_ll)
   // LinearLayout mCommonTitleLl;
    @Bind(R.id.main_iv_circle)
    ImageView mMainIvCircle;
    @Bind(R.id.main_tv_date)
    TextView mMainTvDate;
    @Bind(R.id.main_tv_content)
    TextView mMainTvContent;
    @Bind(R.id.item_ll_control)
    LinearLayout mItemLlControl;

    @Bind(R.id.main_rv_show_diary)
    RecyclerView mMainRvShowDiary;
    @Bind(R.id.main_fab_enter_edit)
    FloatingActionButton mMainFabEnterEdit;
    @Bind(R.id.main_rl_main)
    RelativeLayout mMainRlMain;
    @Bind(R.id.item_first)
    LinearLayout mItemFirst;
    @Bind(R.id.main_ll_main)
    LinearLayout mMainLlMain;
    @Bind(R.id.searchView)
    SearchView mSearchView;
    private List<diaryBean> mDiaryBeanList=new ArrayList<>();


    private diaryDB mHelper;

    private static String IS_WRITE = "true";

    private int mEditPosition = -1;
    private DiaryAdapter diaryAdapter;
    /**
     * 标识今天是否已经写了日记
     */
    private boolean isWrite = false;
    private static TextView mTvTest;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_1, container, false);
        ButterKnife.bind(this,rootView);
        StatusBarCompat.compat(getActivity(), Color.parseColor("#161414"));
        mHelper = new diaryDB(getActivity(), "Diary.db", null, 1);
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();
        if(!EventBus.getDefault().isRegistered(this)){//加上判断
            EventBus.getDefault().register(this);
        }

        SpHelper spHelper = SpHelper.getInstance(getActivity());

        initTitle();
        getDiaryBeanList();
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        mMainRvShowDiary.setLayoutManager(layoutManager);
        diaryAdapter=new DiaryAdapter(getActivity().getApplicationContext(),mDiaryBeanList);
        mMainRvShowDiary.setAdapter(diaryAdapter);
        mTvTest = new TextView(getActivity());
        mTvTest.setText("hello world");
        searchEvent();
        mSearchView.clearFocus();
        return rootView;
    }
    public static void  startActivity(Context context) {
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
        //diaryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //AppManager.getAppManager().addActivity(getActivity());

    }

    private void initTitle() {
        mMainTvDate.setText("今天，" + GetDate.getDate());
       // mCommonTvTitle.setText("日记");
      //  mCommonIvBack.setVisibility(View.INVISIBLE);
      //  mCommonIvTest.setVisibility(View.INVISIBLE);

    }

    private void searchEvent(){

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {

                    diaryAdapter.filter(newText);

                return false;
            }
        });
    }




    private List<diaryBean> getDiaryBeanList() {

        //mDiaryBeanList = new ArrayList<>();
        List<diaryBean> diaryList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = mHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("Diary", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String dateSystem = GetDate.getDate().toString();
                if (date.equals(dateSystem)) {
                    mMainLlMain.removeView(mItemFirst);

                    break;
                }
            } while (cursor.moveToNext());
        }


        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String tag = cursor.getString(cursor.getColumnIndex("tag"));
                mDiaryBeanList.add(new diaryBean(date, title, content, tag));
            } while (cursor.moveToNext());
        }
        cursor.close();

        for (int i = mDiaryBeanList.size() - 1; i >= 0; i--) {
            diaryList.add(mDiaryBeanList.get(i));
        }

        mDiaryBeanList = diaryList;
        return mDiaryBeanList;
    }

    @Subscribe
    public void startUpdateDiaryActivity(StartUpdateDiaryEvent event) {
        String title = mDiaryBeanList.get(event.getPosition()).getTitle();
        String content = mDiaryBeanList.get(event.getPosition()).getContent();
        String tag = mDiaryBeanList.get(event.getPosition()).getTag();
        UpdateDiaryActivity.startActivity(getActivity(), title, content, tag);
        getActivity().finish();
       // onDestroy();

    }

    @Subscribe
    public void DelDiaryActivity(final DelDiaryEvent event) {
        //final String date = mDiaryBeanList.get(event.getPosition()).getDate();
         String title = mDiaryBeanList.get(event.getPosition()).getTitle();
         String content = mDiaryBeanList.get(event.getPosition()).getContent();
         final String tag = mDiaryBeanList.get(event.getPosition()).getTag();
        //UpdateDiaryActivity.startActivity(getActivity(), title, content, tag);
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("确定要删除该日记吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

//                        String title = mUpdateDiaryEtTitle.getText().toString();
                //String tag = mTvTag.getText().toString();
                SQLiteDatabase dbDelete = mHelper.getWritableDatabase();
                dbDelete.delete("Diary", "tag = ?", new String[]{tag});
                mDiaryBeanList.remove(event.getPosition());
                diaryAdapter.notifyItemRemoved(event.getPosition());

              /*  getDiaryBeanList();
                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
                mMainRvShowDiary.setLayoutManager(layoutManager);*/
                diaryAdapter=new DiaryAdapter(getActivity().getApplicationContext(),mDiaryBeanList);
                mMainRvShowDiary.setAdapter(diaryAdapter);
                //if(event.getPosition()==0){
                    //mMainLlMain.addView(mItemFirst);
               // }



                //mMainRvShowDiary.notify();
                //mDiaryBeanList.remove(new diaryBean(date,title, content, tag));
                //diaryAdapter.notifyDataSetChanged();
                //FragmentOne.startActivity(getActivity());


               // FragmentOne.startActivity(UpdateDiaryActivity.this);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.main_fab_enter_edit)
    public void onClick() {
        AddDiaryActivity.startActivity(getActivity());
        getActivity().finish();
    }

    public void onResume(){
        super.onResume();

        mSearchView.setFocusable(true);
        mSearchView.setFocusableInTouchMode(true);
        //onCreateView(null,null,null);
    }
    public void onStart(){
        super.onStart();
        //getDiaryBeanList();

        //diaryAdapter.notifyDataSetChanged();
    }


}






