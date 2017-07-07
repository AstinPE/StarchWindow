package me.astin.starch.ui;

import android.widget.Button;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;


public class FloatingButton {
    private Context ctx;
    private Button fButton;
    private int width;
    private int stroke_color;
    private int color;
    private Drawable drawable;
    private int[] padding;

    public FloatingButton(Context _ctx) {
        this.ctx = _ctx;
        this.fButton = new Button(this.ctx);
    }

    public FloatingButton setStroke(int _width, int _color) {
        this.width= _width;
        this.stroke_color = _color;
        return this;
    }

    public FloatingButton setColor(int _color) {
        this.color = _color;
        return this;
    }

    public FloatingButton setDrawable(Drawable _drawable) {
        this.drawable = _drawable;
        return this;
    }

    public FloatingButton setPadding(int l, int t, int r, int b) {
        padding = new int[] { l, t, r ,b };
        return this;
    }

    public Button show() {
        final int DP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, ctx.getResources().getDisplayMetrics());
        GradientDrawable gradient = new GradientDrawable();
        gradient.setCornerRadius(900 * DP);
        gradient.setStroke(this.width, this.stroke_color);
        gradient.setColor(this.color);

        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[] { gradient, this.drawable });
        layerDrawable.setLayerInset(1, padding[0], padding[1], padding[2], padding[3]);
        this.fButton.setBackgroundDrawable(layerDrawable);

        return this.fButton;
    }
}
