package me.astin.starch.ui;

import android.view.View;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import me.astin.starch.Utils;


public class RippleDrawable {
	private Context ctx;
	private MaterialButton view;
	private float width, height, x, y;
	private int color, _color;
	private MaterialButton.OnClickListener event;
	private Drawable background;
	
	public RippleDrawable(Context _ctx, final MaterialButton _view) {
		this.ctx = _ctx;
		this.view = _view;
	}
	
	public void setParams(final float _width, final float _height) {
		this.width = _width;
		this.height = _height;
	}
	
	public void setHotspot(final float _x, final float _y) {
		this.x = _x;
		this.y = _y;
	}
	
	public void setColor(final int __color, final int ___color) {
		this.color = __color;
		this._color = ___color;
	}
	
	public void setEvent(final MaterialButton.OnClickListener listener) {
		this.event = listener;
	}
	
	public void setBackgroundDrawable(Drawable drawable) {
		this.background = drawable;
	}
	
	public void start() {
		final RippleDrawable thiz = this;
		final float radius = Math.min(this.width, this.height) / 2;
		final float max_radius = (float) (Math.hypot(this.width, this.height) / 2) + Utils.dip2px(this.ctx, 100);
		
		final ValueAnimator valueAnimator = ValueAnimator.ofFloat(new float[] {radius, max_radius}),
		_valueAnimatorX = ValueAnimator.ofFloat(new float[] {x, this.width / 2}),
		_valueAnimatorY = ValueAnimator.ofFloat(new float[] {y, this.height / 2});
		
		_valueAnimatorX.setDuration(200);
		_valueAnimatorY.setDuration(200);
		
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			public boolean click = false;
			public void onAnimationUpdate(ValueAnimator _valueAnimator) {
				float current_radius = _valueAnimator.getAnimatedValue(),
				circle_point_x = _valueAnimatorX.getAnimatedValue(),
				circle_point_y = _valueAnimatorY.getAnimatedValue();
				
				int percent = (int) (255 * (1 - (current_radius / max_radius)));
				
				if(current_radius < max_radius) {
					drawCircle(thiz.view, (int) thiz.width, (int) thiz.height, circle_point_x, circle_point_y, thiz.color, thiz._color, current_radius, percent, thiz.background);
				}
				
				if(circle_point_x == width / 2 && !click) {
					view.setBackgroundDrawable(thiz.background);
					if(thiz.event != null) thiz.event.onClick(view);
					
					click = true;
				}
				
				if(circle_point_x >= width / 2 && click) {
					view.setBackgroundDrawable(thiz.background);
				}
			}
		});
			
		valueAnimator.setDuration(250);
		valueAnimator.start();
		_valueAnimatorX.start();
		_valueAnimatorY.start();
	}
	
	public static void drawCircle(MaterialButton view, int width, int height, float x, float y, int color, int _color, float radius, int alpha, Drawable drawable) {
		Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bm);
		final Paint paint = new Paint();
		
		paint.setColor(_color);
		paint.setAlpha(alpha);
		paint.setAntiAlias(true);
		
		canvas.drawCircle(x, y, radius, paint);
		
		if(drawable != null) {
            view.setBackgroundDrawable(new LayerDrawable(new Drawable[] {drawable, new BitmapDrawable(bm)}));
        }
        if(drawable == null) {
            view.setBackgroundDrawable(new LayerDrawable(new Drawable[] {new ColorDrawable(color), new BitmapDrawable(bm)}));
        }
	}
}
