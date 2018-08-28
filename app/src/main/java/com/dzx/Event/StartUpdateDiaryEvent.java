package com.dzx.Event;

/**
 * Created by 杜卓轩 on 2018/3/17.
 */

public class StartUpdateDiaryEvent {

    private int position;

    public StartUpdateDiaryEvent(int position) {
        this.position = position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
