package me.astin.starch.ui;

import android.graphics.Color;


public enum MyColor {
    BLUE(Color.rgb(90, 110, 255), Color.rgb(80, 100, 255)), 
    RED(Color.rgb(255, 110, 90), Color.rgb(255, 100, 80)),
    GREEN(Color.rgb(90, 255, 110), Color.rgb(80, 255, 100)),
    BLACK(Color.rgb(30, 30, 30), Color.rgb(60, 60, 60)),
    GRAY(Color.rgb(130, 130, 130), Color.rgb(160, 160, 160)),
    BLUEGRAY(Color.parseColor("#546E7A"), Color.parseColor("#78909C")),
    CYAN(Color.parseColor("#00ACC1"), Color.parseColor("#26C6DA")),
    LIME(Color.parseColor("#C0CA33"), Color.parseColor("#D4E157")),
    AMBER(Color.parseColor("#FFB300"), Color.parseColor("#FFD54F")),
    BROWN(Color.parseColor("#6D4C41"), Color.parseColor("#8D6E63"));


    private int color;
    private int strokeColor;

    private MyColor(int _color, int _strokeColor) {
        this.color = _color;
        this.strokeColor = _strokeColor;
    }

    public int getColor() {
        return this.color;
    }

    public int getColorAccent() {
        return this.strokeColor;
    }
}
