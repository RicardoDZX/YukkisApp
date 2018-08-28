package com.dzx.Activity;


import com.dzx.Recognization.online.CommonRecogParams;
import com.dzx.Recognization.online.OnlineRecogParams;
import com.dzx.Setting.Setting_use;

/**
 * 在线识别，用于展示在线情况下的识别参数和效果。
 */
public class ActivityOnline extends ActivityRecog {


    public ActivityOnline() {
        super();
        settingActivityClass = Setting_use.class;
    }

    @Override
    protected CommonRecogParams getApiParams() {
        return new OnlineRecogParams(this);
    }


}