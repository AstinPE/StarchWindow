package me.astin.starch.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.view.View;

import me.astin.starch.Utils;
import me.astin.starch.ui.MyColor;
import me.astin.starch.R;


public class MenuItem {

    public interface OnClickListener {
        public void onClick(View v);
    }
    private Context ctx;
    private TextView iconView;
    private TextView articleView;
    private MenuItem.OnClickListener listener;

    public MenuItem(Context _ctx) {
        this.ctx = _ctx;
        this.iconView = new TextView(this.ctx);
        this.articleView = new TextView(this.ctx);
        this.iconView.setLayoutParams(new LinearLayout.LayoutParams(Utils.dip2px(this.ctx, 25), -1));
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[] { Utils.ColorFilter((BitmapDrawable) this.ctx.getResources().getDrawable(R.drawable.ic_menu_right), MyColor.BLUE.getColor()) });
        layerDrawable.setLayerInset(0, Utils.dip2px(this.ctx, 3), Utils.dip2px(this.ctx, 3), Utils.dip2px(this.ctx, 3), Utils.dip2px(this.ctx, 3));
        this.iconView.setBackgroundDrawable(layerDrawable);
    }

    public MenuItem setIcon(Drawable icon) {
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[] { icon });
        layerDrawable.setLayerInset(0, Utils.dip2px(this.ctx, 3), Utils.dip2px(this.ctx, 3), Utils.dip2px(this.ctx, 3), Utils.dip2px(this.ctx, 3));
        this.iconView.setBackgroundDrawable(layerDrawable);
        return this;
    }

    public MenuItem setText(String article) {
        this.articleView.setText(article);
        this.articleView.setTextColor(Color.GRAY);
        this.articleView.setGravity(Gravity.CENTER|Gravity.LEFT);
        this.articleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        this.articleView.setLayoutParams(new LinearLayout.LayoutParams(Utils.dip2px(this.ctx, 175), Utils.dip2px(this.ctx, 25)));
        return this;
    }

    public MenuItem setOnClickListener(MenuItem.OnClickListener _listener) {
        this.listener = _listener;
        return this;
    }

    public LinearLayout show() {
        final LinearLayout mainLayout = new LinearLayout(this.ctx);
        final MenuItem that = this;
        this.articleView.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == MotionEvent.ACTION_DOWN) {
						mainLayout.setBackgroundColor(Color.parseColor("#EEEEEE"));
					}
					if(event.getAction() == MotionEvent.ACTION_UP) {
						mainLayout.setBackgroundColor(Color.WHITE);
						if(event.getX() > 0 && event.getY() > 0 && event.getX() < Utils.dip2px(that.ctx, 200) && event.getY() < Utils.dip2px(that.ctx, 25)) {
							if(that.listener != null) that.listener.onClick(v);
						}
					}
					return true;
				}
			});
        mainLayout.setOrientation(0);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(Utils.dip2px(this.ctx, 200), Utils.dip2px(this.ctx, 25)));
        mainLayout.addView(this.iconView);
        mainLayout.addView(this.articleView);
        return mainLayout;
    }
}
