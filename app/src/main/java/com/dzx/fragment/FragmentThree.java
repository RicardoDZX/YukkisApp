package com.dzx.fragment;

import android.Manifest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.dzx.Adapter.weatherInfoViewAdapter;
import com.dzx.Bean.weatherInfoBean;
import com.dzx.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.dzx.Bean.weatherBean;
import com.google.gson.Gson;
import com.dzx.util.GetTime;
import static java.lang.Boolean.TRUE;


/**
 * Created by 杜卓轩 on 2018/2/28.
 */


public class FragmentThree extends Fragment implements AMapLocationListener{
    public AMapLocationClient mLocationClient=null;
    public AMapLocationClientOption mLocationOption=null;
    public String city;
    public TextView fragment3;

    public String cityCode;
    public double mLatitude;
    public double mLongitude;
    public TextView textView2;
    public TextView t_city;
    public TextView t_date;
    public TextView t_temp;
    public TextView t_weath;
    public TextView t_wind;
    public TextView t_air;
    public String weatherInfo;
    public boolean flag=TRUE;

    public String s_city;
    public String s_date;
    public String s_temp;
    public String s_weath;
    public String s_wind;
    public String s_air;
    public GetTime mgetTime;

    public static SharedPreferences sp;


    private String randomTips_night[]={//17-21

            "时间定位：晚上\n坐标定位：失败\n重新搜索中：搜索不到\n（噗...假的假的）",
            "我掐指一算，大概现在是晚上？\n准不准，我就问准不准!",
            "其实我特喜欢晚上，因为挺安静的，\n有时候坐在看台上，有点点风就很舒服。\n心情不好的时候自己掐一掐脸，就觉得脸皮这么厚做什么都没问题\n"
    };
    private String randomTips_morning[]={//6-8
            "嗨，早上好啊！今天天气...嗯让我掐指算一下....\n完了，算不出来，快点一下上面的按钮让我瞧一眼。",
            "嗯！又是新的一天，要带着一天的好心情啊！(绝不是尬笑！我笑起来其实更帅啊有没有觉得，有没有！)",
            "一!定!吃!早!餐!不然容易胃病啊！注意身体",
            "我编不出来了怎么办...头疼。\n假装此处有好长好长的话！"
    };
    private String randomTips_noon[]={//10-14
            "午安~！中午稍微休息一下？",
            "武汉的神奇天气真的是...神奇！\n（假如是夏天）可别被晒晕了啊。\n（伞伞伞你在哪儿其实我也想打伞）",
            "小睡一下养足精神我觉得挺好的。下午就不会犯困啦！",
            "这条也是我苦思冥想想不出说什么的结果。\n啊今天天气真好啊（抬头）",
            "记得不要在路上玩手机啊！注意安全。"
    };
    private String randomTips_midnight[]={//21-6

            "现在...应该是很晚了吧！\n稍微注意一下休息来着。",


            "玉琦大姐会不会有点累...照顾好自己啊。如果很困但还有事情没做完，不如明早早些起完成待续的任务？"

    };
    private String randomTips_daily[]={//8-10
            "这里是日常问候哟！（省略好多好多想说的问候）",
            "如果有什么不开心的事，如果愿意的话，就来和我说一说吧。假装我能听见。（其实并没有）",
            "快看我！尬笑！我猜我现在....可能在尬笑？",
            "(眯眼笑)玉琦大姐，我笑起来是不是特有亲和力！\n今天踢足球比赛的时候对面的数院的都被我感染了！\n学长说我应该去外交。还夸我可爱来着！\n" +
                    "虽然....我觉得夸男生可爱有点怪，但还是被夸了就很开心！"

    };
    private String randomTips_afternoon[]={//14-17
            "现在大概是下午？下午好啊~喝茶了吗？（毕竟有下午茶这个说法）",
            "在上课吗？要好好听讲啊！",
            "也许自习很枯燥但是....玉琦大姐要加油啊！无论在哪你都是最棒的！",
            "此处空白2（等以后想到说什么了版本更新再填吧！）",
            "此处空白1（等以后想到说什么了版本更新再填吧！）"
    };

    Random rand=new Random();

    //测试代码。测试功能
    private weatherInfoViewAdapter weatherInfoViewAdapters;
    private ListView weather_listView;

    private List<weatherInfoBean> weatherInfoBeans_List = new ArrayList<weatherInfoBean>();
    private Handler handler_p = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    /**
                     * ListView条目控制在最后一行
                     */
                    weather_listView.setSelection(weatherInfoBeans_List.size());
                    break;

                default:
                    break;
            }
        };
    };






    public static boolean isNetworkAvailable(Context context) {

        if(context!=null) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null) {return false;
            } else {
                //如果仅仅是用来判断网络连接
                //则可以使用 cm.getActiveNetworkInfo().isAvailable();
                NetworkInfo[] info = cm.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.fragment_3, container, false);
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        initPermission();
        location();



        //测试：

        weather_listView = (ListView) rootView.findViewById(R.id.lv_chat_dialog);
        Button btn_forecast_send = (Button) rootView.findViewById(R.id.btn_forecast);
        Button btn_tips_send = (Button) rootView.findViewById(R.id.btn_tips);
        /**
         * 发送按钮的点击事件
         */

        btn_tips_send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // TODO Auto-generated method stub
               // if (isNetworkAvailable(getActivity().getApplicationContext())) {
                HashMap<String,Object> map=mgetTime.getTime();
                int nowtime=Integer.parseInt(map.get("hour").toString());
                int nowDay=Integer.parseInt(map.get("day").toString());
                sp = getActivity().getSharedPreferences("Time_record", Context.MODE_PRIVATE);

                if(nowtime-sp.getInt("LAST_TIME",0)>=1  ||nowDay-sp.getInt("LAST_DAY",0)!=0) {//如果与上次使用tips间隔超过三小时（仅使用hour来粗略估计）



                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("LAST_TIME", nowtime);
                    editor.putInt("LAST_DAY",nowDay);
                    editor.commit();
                    //更新使用时间。

                    weatherInfoBean weatherInfoBeans = new weatherInfoBean();
                    weatherInfoBeans.setMeSend(true);
                    weatherInfoBeans.setIsNet(true);
                    weatherInfoBeans.setInfo("想说的话");
                    weatherInfoBeans_List.add(weatherInfoBeans);

                    //weatherInfoViewAdapters.notifyDataSetChanged();
                    // if (!isNetworkAvailable(getActivity().getApplicationContext())) {
                    weatherInfoViewAdapters = new weatherInfoViewAdapter(getActivity(), weatherInfoBeans_List);
                    weather_listView.setAdapter(weatherInfoViewAdapters);
                    //  }
                    weatherInfoViewAdapters.notifyDataSetChanged();

                    weatherInfoBeans = new weatherInfoBean();
                    weatherInfoBeans.setMeSend(false);
                    weatherInfoBeans.setIsWeather(false);

                    //Log.i("tag",map.get("hour").toString());
                    if (nowtime <= 24 && nowtime >= 21) {
                        int random_tips = rand.nextInt(2);
                        weatherInfoBeans.setTips(randomTips_midnight[random_tips]);
                    } else if (nowtime <= 21 && nowtime >= 17) {
                        int random_tips = rand.nextInt(3);
                        weatherInfoBeans.setTips(randomTips_night[random_tips]);
                    } else if (nowtime <= 17 && nowtime >= 14) {
                        int random_tips = rand.nextInt(5);
                        weatherInfoBeans.setTips(randomTips_afternoon[random_tips]);
                    } else if (nowtime <= 14 && nowtime >= 10) {
                        int random_tips = rand.nextInt(5);
                        weatherInfoBeans.setTips(randomTips_noon[random_tips]);
                    } else if (nowtime <= 10 && nowtime >= 8) {
                        int random_tips = rand.nextInt(4);
                        weatherInfoBeans.setTips(randomTips_daily[random_tips]);
                    } else if (nowtime <= 8 && nowtime >= 6) {
                        int random_tips = rand.nextInt(4);
                        weatherInfoBeans.setTips(randomTips_morning[random_tips]);
                    } else if (nowtime <= 6 && nowtime >= 0) {
                        int random_tips = rand.nextInt(2);
                        weatherInfoBeans.setTips(randomTips_midnight[random_tips]);
                    }


                    weatherInfoBeans_List.add(weatherInfoBeans);

                    weatherInfoViewAdapters.notifyDataSetChanged();
                    handler_p.sendEmptyMessage(1);
                }
                else {
                    weatherInfoBean weatherInfoBeans = new weatherInfoBean();
                    weatherInfoBeans.setMeSend(false);
                    weatherInfoBeans.setIsWeather(false);
                    //weatherInfoBeans.setIsNet(true);
                    weatherInfoBeans.setTips("先去忙吧！"+"\n"+"我设定的是间隔要一个小时来着（笑）。\n"+"不然点点点就没意思啦！"+"\n"+"考虑一下存货问题！"+"\n"+"留一些以后慢慢看吧...");
                    //weatherInfoBeans.setTips("先去忙吧！不要这么频繁的点击啊。考虑一下存货问题！留一些以后看可好...");

                    weatherInfoBeans_List.add(weatherInfoBeans);

                    //weatherInfoViewAdapters.notifyDataSetChanged();
                    // if (!isNetworkAvailable(getActivity().getApplicationContext())) {
                    weatherInfoViewAdapters = new weatherInfoViewAdapter(getActivity(), weatherInfoBeans_List);
                    weather_listView.setAdapter(weatherInfoViewAdapters);
                    //  }
                    weatherInfoViewAdapters.notifyDataSetChanged();
                    handler_p.sendEmptyMessage(1);
                }
               // }
               /*else{
                    weatherInfoBean weatherInfoBeans = new weatherInfoBean();
                    weatherInfoBeans.setMeSend(true);
                    weatherInfoBeans.setIsNet(false);
                    weatherInfoBeans.setNetInfo("网络没有连接上啊,请先连接网络再试");
                    weatherInfoBeans_List.add(weatherInfoBeans);
                    weatherInfoViewAdapters = new weatherInfoViewAdapter(getActivity(), weatherInfoBeans_List);
                    weather_listView.setAdapter(weatherInfoViewAdapters);
                    weatherInfoViewAdapters.notifyDataSetChanged();
                }*/
            }
        });
        btn_forecast_send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (isNetworkAvailable(getActivity().getApplicationContext())) {
                    Log.i("tag","youwangluolianjie");
                    weatherInfoBean weatherInfoBeans = new weatherInfoBean();
                    weatherInfoBeans.setMeSend(true);
                    weatherInfoBeans.setIsNet(true);
                    weatherInfoBeans.setInfo("未来N天天气预报");
                    weatherInfoBeans_List.add(weatherInfoBeans);
                    //weatherInfoViewAdapters.notifyDataSetChanged();

                    /*try {
                        Thread.currentThread().sleep(1000);
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/

                    weatherInfoBeans = new weatherInfoBean();
                    weatherInfoBeans.setMeSend(false);
                    weatherInfoBeans.setIsWeather(true);
                    Gson gson = new Gson();
                    weatherBean weatherBean = gson.fromJson(weatherInfo, weatherBean.class);
                    weatherInfoBeans.setParent_city(weatherBean.getHeWeather6().get(0).getBasic().getParent_city());
                    weatherInfoBeans.setCity(weatherBean.getHeWeather6().get(0).getBasic().getLocation());
                    weatherInfoBeans.setDate(weatherBean.getHeWeather6().get(0).getDaily_forecast().get(1).getDate());
                    weatherInfoBeans.setTemp(weatherBean.getHeWeather6().get(0).getDaily_forecast().get(1).getTmp_min());
                    weatherInfoBeans.setTemp_max(weatherBean.getHeWeather6().get(0).getDaily_forecast().get(1).getTmp_max());
                    weatherInfoBeans.setWeather(weatherBean.getHeWeather6().get(0).getDaily_forecast().get(1).getCond_txt_d());
                    weatherInfoBeans.setWind(weatherBean.getHeWeather6().get(0).getDaily_forecast().get(1).getWind_dir());
                    weatherInfoBeans_List.add(weatherInfoBeans);

                    weatherInfoViewAdapters.notifyDataSetChanged();


                    weatherInfoBeans = new weatherInfoBean();
                    weatherInfoBeans.setMeSend(false);
                    weatherInfoBeans.setIsWeather(true);
                    gson = new Gson();
                    weatherBean = gson.fromJson(weatherInfo, weatherBean.class);
                    weatherInfoBeans.setParent_city(weatherBean.getHeWeather6().get(0).getBasic().getParent_city());
                    weatherInfoBeans.setCity(weatherBean.getHeWeather6().get(0).getBasic().getLocation());
                    weatherInfoBeans.setDate(weatherBean.getHeWeather6().get(0).getDaily_forecast().get(2).getDate());
                    weatherInfoBeans.setTemp(weatherBean.getHeWeather6().get(0).getDaily_forecast().get(2).getTmp_min());
                    weatherInfoBeans.setTemp_max(weatherBean.getHeWeather6().get(0).getDaily_forecast().get(2).getTmp_max());
                    weatherInfoBeans.setWeather(weatherBean.getHeWeather6().get(0).getDaily_forecast().get(2).getCond_txt_d());
                    weatherInfoBeans.setWind(weatherBean.getHeWeather6().get(0).getDaily_forecast().get(2).getWind_dir());
                    weatherInfoBeans_List.add(weatherInfoBeans);
                    weatherInfoViewAdapters.notifyDataSetChanged();

                    handler_p.sendEmptyMessage(1);
                }
                else{
                    weatherInfoBean weatherInfoBeans = new weatherInfoBean();
                    weatherInfoBeans.setMeSend(true);
                    weatherInfoBeans.setIsNet(false);
                    weatherInfoBeans.setNetInfo("网络没有连接上啊,请先连接网络再试");
                    weatherInfoBeans_List.add(weatherInfoBeans);
                    weatherInfoViewAdapters = new weatherInfoViewAdapter(getActivity(), weatherInfoBeans_List);
                    weather_listView.setAdapter(weatherInfoViewAdapters);
                    weatherInfoViewAdapters.notifyDataSetChanged();
                    handler_p.sendEmptyMessage(1);
                }
            }
        });
      //  initView(rootView);

        return rootView;
    }


    private void initView(View rootView){
        fragment3= rootView.findViewById(R.id.fragment_3_text);
        textView2=rootView.findViewById(R.id.textView1);
        t_air=rootView.findViewById(R.id.tv_air);
        t_city=rootView.findViewById(R.id.tv_city);
        t_date=rootView.findViewById(R.id.tv_date);
        t_temp=rootView.findViewById(R.id.tv_temp);
        t_weath=rootView.findViewById(R.id.tv_weath);
        t_wind=rootView.findViewById(R.id.tv_wind);

    }
    @Override
    public void onStart(){
        super.onStart();
       // fragment3.setText(cityCode);
        //fragment3.setText("cityCode");
     //   Log.i("tag",cityCode);
       // textView2.setText(weatherInfo);
    }

    private void location() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getContext());
        //设置定位回调监听
        mLocationClient.setLocationListener((AMapLocationListener) this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

        //启动定位
        mLocationClient.startLocation();


    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {

        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //可在其中解析amapLocation获取相应内容。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                city = amapLocation.getCity().toString();//城市信息
                mLatitude = amapLocation.getLatitude();//获取纬度
                mLongitude = amapLocation.getLongitude();//获取经度
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = "https://free-api.heweather.com/s6/weather?" +
                                "location=" + mLongitude + "," + mLatitude + "&" +
                                "key=1c3120151deb44f3aaa70d5c2e0e5581" + "&" +

                                "unit=m";

                        OkHttpClient okHttpClient = new OkHttpClient();
                        Request request = new Request.Builder().url(url).build();
                        Call call = okHttpClient.newCall(request);
                        try {
                            Response response = call.execute();
                            //打印json
                            //System.out.println(response.body().string());
                            // Log.i("tag",response.body().string());
                            weatherInfo = response.body().string();
                            Log.i("tag", weatherInfo);

                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.i("tag", "ioexception wrong");
                            //textView2.setText("error");
                        }


                      //  s_air = weatherBean.getHeWeather6().get(0).getNow();


                      //  tv_qlty.setText("空气质量："+qlty);
                        handler.sendEmptyMessage(1);
                    }
                }).start();
                //System.out.println(mLatitude);
                //System.out.println(mLongitude);
                //fragment3.setText(city);
                /*city="北京";
                Cursor C=com.dzx.db.rawQuery("select * from city.com.dzx.db where city like" + city+"%",null);
                while	(C.moveToNext()) {
                    String	cityCode	=	C.getString(C.getColumnIndex("number"));
                }*/
               // fragment3.setText(cityCode);
             //   textView2.setText(weatherInfo);







                /*SharedPreferences sharedPreferences=getSharedPreferences("CityCodePreference", Activity_MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("cityCode",cityCode);
                editor.commit();
                Intent intent=new intent(getActivity(),);
                intent.putExtra("cityCode",cityCode);
                startActivity(intent);*/

            }
        } else {
            //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
            Log.e("AmapError", "location Error, ErrCode:"
                    + amapLocation.getErrorCode() + ", errInfo:"
                    + amapLocation.getErrorInfo());
        }
    }




    private Handler handler = new Handler(){
        @Override
public  void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

                    break;
                case 1:
                   // fragment3.setText(cityCode);
                   // textView2.setText(weatherInfo);
                   // t_city.setText("城市："+s_city);
                    //t_date.setText("日期："+s_date);
                   // t_temp.setText("温度："+s_temp);
                   // t_weath.setText("天气情况："+s_weath);
                   // t_wind.setText("风向："+s_wind);
                    if (isNetworkAvailable(getActivity())) {
                        weatherInfoBean weatherInfoBeans = new weatherInfoBean();
                        weatherInfoBeans.setMeSend(false);
                        weatherInfoBeans.setIsWeather(true);
                        Gson gson = new Gson();
                        weatherBean weatherBean = gson.fromJson(weatherInfo, weatherBean.class);
                        weatherInfoBeans.setParent_city(weatherBean.getHeWeather6().get(0).getBasic().getParent_city());
                        weatherInfoBeans.setCity(weatherBean.getHeWeather6().get(0).getBasic().getLocation());
                        weatherInfoBeans.setDate(weatherBean.getHeWeather6().get(0).getDaily_forecast().get(0).getDate());
                        weatherInfoBeans.setTemp(weatherBean.getHeWeather6().get(0).getDaily_forecast().get(0).getTmp_min());
                        weatherInfoBeans.setTemp_max(weatherBean.getHeWeather6().get(0).getDaily_forecast().get(0).getTmp_max());
                        weatherInfoBeans.setWeather(weatherBean.getHeWeather6().get(0).getDaily_forecast().get(0).getCond_txt_d());
                        weatherInfoBeans.setWind(weatherBean.getHeWeather6().get(0).getDaily_forecast().get(0).getWind_dir());
                        weatherInfoBeans_List.add(weatherInfoBeans);

                        /**
                         *setAdapter
                         */
                        weatherInfoViewAdapters = new weatherInfoViewAdapter(getActivity(), weatherInfoBeans_List);
                        weather_listView.setAdapter(weatherInfoViewAdapters);
                        weatherInfoViewAdapters.notifyDataSetChanged();
                    }


                    break;
                default:
                    break;
            }
        }
    };








    private void initPermission() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
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
}
