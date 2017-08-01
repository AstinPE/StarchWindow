package me.astin.starch.ui;

import android.widget.Button;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;

import me.astin.starch.Utils;


public class FloatingButton extends Button {
    private GradientDrawable gradientDrawable;

    public FloatingButton(Context context) {
        super(context);
        this.gradientDrawable = new GradientDrawable();
        this.gradientDrawable.setCornerRadius(Utils.dip2px(context, 900));
        super.setBackgroundDrawable(this.gradientDrawable);
    }

    public void setStroke(int width, int color) {
        this.gradientDrawable.setStroke(width, color);
    }

    public void setColor(int color) {
        this.gradientDrawable.setColor(color);
    }

    public void setBackgroundDrawable(Drawable drawable, int[] padding) {
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[] { this.gradientDrawable, drawable});
        layerDrawable.setLayerInset(1, padding[0], padding[1], padding[2], padding[3]);
        super.setBackgroundDrawable(layerDrawable);
    }
}
