package com.dzx.Setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;


import com.dzx.R;

/**
 * Created by 杜卓轩 on 2018/3/4.
 */

public abstract class Setting_common extends PreferenceActivity implements Preference.OnPreferenceChangeListener{
    protected int setting;
    protected String title = "设置";
    private Toolbar toolbar;
    private AppCompatDelegate mDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceState);

        super.onCreate(savedInstanceState);
        setTitle(title);
        setContentView(R.layout.setting_layout);

        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.addPreferencesFromResource(setting);

        PreferenceScreen s = (PreferenceScreen) findPreference("root_screen");
        bind(s);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getDelegate().onPostCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getDelegate().setContentView(layoutResID);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getDelegate().onPostResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getDelegate().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getDelegate().onDestroy();
    }


    private void setSupportActionBar(@Nullable Toolbar toolbar) {
        getDelegate().setSupportActionBar(toolbar);
    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }

    private void bind(PreferenceGroup group) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        for (int i = 0; i < group.getPreferenceCount(); i++) {
            Preference p = group.getPreference(i);
            if (p instanceof PreferenceGroup) {
                bind((PreferenceGroup) p);
            } else {
                if (p instanceof CheckBoxPreference) {
                    ;
                } else {
                    Object val = sp.getAll().get(p.getKey());
                    p.setSummary(val == null ? "" : ("" + val));
                    p.setOnPreferenceChangeListener(this);
                }
            }
        }
    }

    @Override
    public boolean onPreferenceChange(Preference p, Object newValue) {
        if (p instanceof CheckBoxPreference) {
            ;
        } else {
            p.setSummary("" + newValue);
        }
        return true;
    }
}

