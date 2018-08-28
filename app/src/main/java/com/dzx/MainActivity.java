package com.dzx;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.dzx.Setting.Setting_use;
import com.dzx.fragment.FragmentCommon;
import com.dzx.fragment.FragmentFour;
import com.dzx.fragment.FragmentOne;
import com.dzx.fragment.FragmentThree;
import com.dzx.fragment.FragmentTwo;
import com.dzx.Adapter.SectionsPagerAdapter;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationBar.OnTabSelectedListener,
        ViewPager.OnPageChangeListener{

    //之前出现的问题是因为没有实现全部的接口的抽象方法，我加上reselected和unselected之后就不报错了。
    private ViewPager viewPager;
    private BottomNavigationBar mBottomNavigationBar;
    private List<Fragment> fragments;
    private MenuItem menuItem;
  //  private FragmentOne mFragmentOne;
   // private FragmentTwo mFragmentTwo;
  //  private FragmentThree mFragmentThree;
  //  private FragmentFour mFragmentFour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    private void initView(){
        initBottomNavigationBar();
        initViewPager();
    }

    private void initBottomNavigationBar(){
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationBar.setTabSelectedListener(this);
        mBottomNavigationBar.clearAll();
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
//        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        mBottomNavigationBar.setBarBackgroundColor(R.color.red);//set background color for navigation bar
        mBottomNavigationBar.setInActiveColor(R.color.gray);//unSelected icon color
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_tab3, "记录").setActiveColorResource(R.color.white))
                .addItem(new BottomNavigationItem(R.drawable.ic_tab2, "声音").setActiveColorResource(R.color.white))
                .addItem(new BottomNavigationItem(R.drawable.ic_tab1, "天气").setActiveColorResource(R.color.white))
                .addItem(new BottomNavigationItem(R.drawable.icon_four, "没想好").setActiveColorResource(R.color.white))
                //.setFirstSelectedPosition(0)
                .initialise();


        //setDefaultFragment();
    }


    private void initViewPager(){
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        fragments = new ArrayList<Fragment>();
        fragments.add(new FragmentOne());
        fragments.add(new Fragment());
        fragments.add(new FragmentThree());
        fragments.add(new FragmentFour());

        viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(0);

    }

    /*private void setDefaultFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mFragmentOne = FragmentOne.newInstance("First Fragment");
        transaction.replace(R.id.bottom_nav_content, mFragmentOne).commit();
        //第一个参数是将fragment动态添加到那个id布局里，第二个参数是添加哪个fragment
        //还有add方法，remove方法。
        //transaction.commit()是提交事务。
    }*/
    //@Override
    public void onTabSelected(int position) {
       /* FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                if (mFragmentOne == null) {
                    mFragmentOne = FragmentOne.newInstance("First Fragment");
                }
                transaction.replace(R.id.bottom_nav_content, mFragmentOne);
                break;
            case 1:
                if (mFragmentTwo == null) {
                    mFragmentTwo = FragmentTwo.newInstance("Second Fragment");
                }
                transaction.replace(R.id.bottom_nav_content, mFragmentTwo);
                break;
            case 2:
                if (mFragmentThree == null) {
                    mFragmentThree = FragmentThree.newInstance("Third Fragment");
                }
                transaction.replace(R.id.bottom_nav_content, mFragmentThree);
                break;
            case 3:
                if (mFragmentFour == null) {
                    mFragmentFour = FragmentFour.newInstance("Forth Fragment");
                }
                transaction.replace(R.id.bottom_nav_content, mFragmentFour);
                break;
            default:
                if (mFragmentOne == null) {
                    mFragmentOne = FragmentOne.newInstance("First Fragment");
                }
                transaction.replace(R.id.bottom_nav_content, mFragmentOne);
                break;
        }
        transaction.commit();*/
        viewPager.setCurrentItem(position);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {


        } else if (id == R.id.nav_manage) {
            Intent intent=new Intent(MainActivity.this, Setting_use.class);
            startActivity(intent);



        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override

    public void onTabReselected(int position) {

    }
    @Override

    public void onTabUnselected(int position) {

    }
    @Override

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    @Override
    public void onPageSelected(int position) {
        mBottomNavigationBar.selectTab(position);
    }
    @Override

    public void onPageScrollStateChanged(int state) {

    }

}
