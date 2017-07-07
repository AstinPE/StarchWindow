package me.astin.starch.ui;

import android.content.Context;
import android.widget.PopupWindow;
import android.widget.FrameLayout;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.util.TypedValue;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;
import java.lang.Thread;
import java.lang.Runnable;

import me.astin.starch.ui.MaterialButton;
import me.astin.starch.ui.MyColor;
import me.astin.starch.ui.Divider;
import me.astin.starch.Utils;
import me.astin.starch.R;


public class PopupMenu {
    private Context ctx;
    private int width, height;
    private float x, y;

    private LinearLayout main_layout;
    private PopupWindow window;
    private View contentView;
    private LinearLayout contentViewLayout;

    public PopupMenu(Context _ctx) {
        this.ctx = _ctx;
        this.window = new PopupWindow();
        this.window.setOutsideTouchable(true);
        this.contentViewLayout = new LinearLayout(this.ctx);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public void setWidth(int _width) {
        this.width = _width;
    }

    public void setHeight(int _height) {
        this.height = _height;
    }

    public void setContentView(View v) {
        this.contentViewLayout.addView(v);
    }

    public void showAtLocation(final View v, int x, int y) {
        final PopupMenu that = this;
        this.window.setOnDismissListener(new PopupWindow.OnDismissListener() {
				public void onDismiss() {
					that.main_layout.removeAllViews();
				}
			});

        this.main_layout = new LinearLayout(this.ctx);
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[] { (NinePatchDrawable)this.ctx.getResources().getDrawable(android.R.drawable.dialog_frame), new ColorDrawable(Color.WHITE) });
        layerDrawable.setLayerInset(0, Utils.dip2px(this.ctx, 5), Utils.dip2px(this.ctx, 5) + 3, Utils.dip2px(this.ctx, 5), Utils.dip2px(this.ctx, 5));
        this.main_layout.setBackgroundDrawable(layerDrawable);
        this.main_layout.addView(this.contentViewLayout);
        this.window.setContentView(this.main_layout);
        this.window.setWidth(this.width);
        this.window.setHeight(this.height);
        this.window.showAsDropDown(v, x, y);
    }
}
