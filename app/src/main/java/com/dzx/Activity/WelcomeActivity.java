package com.dzx.Activity;

import android.content.Intent;


import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.dzx.MainActivity;
import com.dzx.R;
import com.dzx.util.GetTime;

import java.util.HashMap;
import java.util.Random;



/**
 * Created by 杜卓轩 on 2018/3/5.
 * 这是一个欢迎页面
 */


public class WelcomeActivity extends AppCompatActivity {
    public GetTime mgetTime;
    private ImageView imageView;
    private TextView textView;
    private int randomImage[]={
            R.mipmap.pic_01,
            R.mipmap.pic_02,
            R.mipmap.pic_03,
            R.mipmap.pic_04,
            R.mipmap.pic_05,
            R.mipmap.pic_06,
            R.mipmap.pic_07,
            R.mipmap.pic_08

    };

    Random rand=new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);

        imageView=(ImageView)findViewById(R.id.image_choose);
        textView=(TextView)findViewById(R.id.introword);
        int random_img=rand.nextInt(7);
        random_img=randomImage[random_img];

        HashMap<String,Object> map=mgetTime.getTime();
        //int nowYear=Integer.parseInt(map.get("year").toString());
        int nowMonth=Integer.parseInt(map.get("month").toString());
        int nowDay=Integer.parseInt(map.get("day").toString());
        if(nowMonth==6&&nowDay==13){
            random_img=randomImage[7];
            textView.setText("玉琦大姐生快呀！\n(找了半天才找到一张像蛋糕的图片)");
        }

        imageView.setImageDrawable(getResources().getDrawable(random_img));


        Handler handler = new Handler();
        //当计时结束,跳转至主界面
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        }, 3000);
    }




}
