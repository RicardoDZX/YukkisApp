package com.dzx.Event;

/**
 * Created by 杜卓轩 on 2018/3/21.
 */

public class DelDiaryEvent {
    private int position;

    public DelDiaryEvent(int position) {
        this.position = position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
