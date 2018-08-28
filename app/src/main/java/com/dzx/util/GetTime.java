package com.dzx.util;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by 杜卓轩 on 2018/3/27.
 */

public class GetTime {
    public static HashMap<String,Object> getTime(){
        HashMap<String,Object> map=new HashMap<>();
        Calendar calendar=Calendar.getInstance();
        map.put("year",calendar.get(Calendar.YEAR));
        map.put("month",calendar.get(Calendar.MONTH));
        map.put("day",calendar.get(Calendar.DAY_OF_MONTH));
        map.put("hour",calendar.get(Calendar.HOUR_OF_DAY));
        map.put("minute",calendar.get(Calendar.MINUTE));
        return map;
    }


}
