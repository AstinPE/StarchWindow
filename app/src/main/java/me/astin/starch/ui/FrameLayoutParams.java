package me.astin.starch.ui;

import android.widget.FrameLayout;


public class FrameLayoutParams extends FrameLayout.LayoutParams {
    public FrameLayoutParams() {
        super(200, 200);
    }

    public FrameLayoutParams(int width, int height) {
        super(width, height);
    }

    public FrameLayoutParams(int width, int height, int grav) {
        super(width, height);
        gravity = grav;
    }

    public FrameLayoutParams(int width, int height, int grav, int[] margin) {
        super(width, height);
        gravity = grav;
        leftMargin = margin[0];
        topMargin = margin[1];
        rightMargin = margin[2];
        bottomMargin = margin[3];
    }
}
