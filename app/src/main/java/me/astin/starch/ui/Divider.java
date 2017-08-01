package me.astin.starch.ui;

import android.widget.TextView;
import android.content.Context;
import android.widget.LinearLayout;


public class Divider extends TextView {
    public Divider(Context ctx) {
        super(ctx);
    }

    public Divider setColor(int color) {
        super.setBackgroundColor(color);
        return this;
    }

    public Divider setWH(int w, int h) {
        super.setLayoutParams(new LinearLayout.LayoutParams(w, h));
        return this;
    }
}
