package com.dzx.Bean;

/**
 * Created by 杜卓轩 on 2018/3/10.
 */

public class weatherInfoBean {



    private String city;
    private String date;
    private String temp;
    private String weather;
    private String wind;
    private String info;
    private String temp_max;
    private String netInfo;

    public String getNetInfo() {
        return netInfo;
    }

    public void setNetInfo(String netInfo) {
        this.netInfo = netInfo;
    }

    public String getParent_city() {
        return parent_city;
    }

    public void setParent_city(String parent_city) {
        this.parent_city = parent_city;
    }

    private String parent_city;
    private String tips;

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(String temp_max) {
        this.temp_max = temp_max;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }


    /**
     *
     * @return 是否为本人发送
     */
    private boolean isMeSend;
    private boolean isWeather;
    private boolean isNet;
    public boolean getisNet() {
        return isNet;
    }
    public boolean isWeather() {
        return isWeather;
    }
    public boolean isMeSend() {
        return isMeSend;
    }
    public void setIsWeather(boolean isWeather) {
        this.isWeather = isWeather;
    }
    public void setIsNet(boolean isNet){this.isNet=isNet;}
    public void setMeSend(boolean isMeSend) {
        this.isMeSend = isMeSend;
    }
    public weatherInfoBean(String city, String date, String temp,String weather,String wind, boolean isMeSend) {
        super();
        this.city = city;
        this.date = date;
        this.temp = temp;
        this.weather=weather;
        this.wind=wind;
        this.isMeSend = isMeSend;
    }
    public weatherInfoBean() {
        super();
    }
}
