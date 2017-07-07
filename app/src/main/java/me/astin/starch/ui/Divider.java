package me.astin.starch.ui;

import android.widget.TextView;
import android.content.Context;
import android.widget.LinearLayout;


public class Divider {
    private TextView divider;

    public Divider(Context ctx) {
        this.divider = new TextView(ctx);
    }

    public Divider setColor(int color) {
        this.divider.setBackgroundColor(color);
        return this;
    }

    public Divider setParams(int w, int h) {
        this.divider.setLayoutParams(new LinearLayout.LayoutParams(w, h));
        return this;
    }

    public TextView show() {
        return this.divider;
    }
}
