package me.astin.starch.ui;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.Build;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.FrameLayout;
import android.view.View;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Color;
import android.util.TypedValue;

import me.astin.starch.Utils;


public class MaterialButton extends Button implements View.OnTouchListener {
    private Context mContext;
    private int AnimationTime = 200;
    private int color, effectColor;
    private Drawable background = null;
	private boolean clicked = false;
	private MaterialButton.OnClickListener mListener;
	
	public interface OnClickListener {
		public void onClick(View view);
	}
    
    public MaterialButton(Context context) {
        super(context);
        this.mContext = context;
        this.setAllCaps(false);
        this.setOnTouchListener(this);
        this.setBackgroundColor(Color.TRANSPARENT);
    }
    
    public void setAnimationTime(int time) {
        this.AnimationTime = time;
    }
    
    public void setColor(int _color) {
        this.color = _color;
        this.setBackgroundColor(_color);
    }
    
    public void setEffectColor(int _color) {
        this.effectColor = _color;
    }
	
	public void setOnClickListener(MaterialButton.OnClickListener listener) {
		mListener = listener;
	}

	public void setBackgroundDrawable(Drawable drawable, boolean save) {
		if(save) {
			super.setBackgroundDrawable(drawable);
			this.background = drawable;
		}
		if(!save) {
			super.setBackgroundDrawable(drawable);
		}
	}
	
	public void setBackgroundDrawable(Drawable drawable, int[] pad) {
		LayerDrawable layerDrawable = new LayerDrawable(new Drawable[] {drawable});
		layerDrawable.setLayerInset(0, pad[0], pad[1], pad[2], pad[3]);
		super.setBackgroundDrawable(layerDrawable);
		this.background = layerDrawable;
	}
    
    @Override
    public boolean onTouch(View view, MotionEvent event) {
		final RippleDrawable ripple = new RippleDrawable(mContext, this);
		ripple.setParams(view.getWidth(), view.getHeight());
		ripple.setColor(color, effectColor);
		ripple.setBackgroundDrawable(background);
		
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                RippleDrawable.drawCircle(this, view.getWidth(), view.getHeight(), event.getX(), event.getY(), color, effectColor, Utils.dip2px(mContext, 10), 255, null);
				clicked = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if(event.getX() > 0 && event.getY() > 0 && event.getX() < view.getWidth() && event.getY() < view.getHeight()) {
					RippleDrawable.drawCircle(this, view.getWidth(), view.getHeight(), event.getX(), event.getY(), color, effectColor, Utils.dip2px(mContext, 10), 255, null);
					clicked = true;
				} else {
					if(clicked) {
						ripple.setHotspot(event.getX(), event.getY());
						ripple.start();
						clicked = false;
					}
				}
                break;
            case MotionEvent.ACTION_UP:
				if(clicked) {
					ripple.setEvent(mListener);
                	ripple.setHotspot(event.getX(), event.getY());
                	ripple.start();
					clicked = false;
				}
                break;
        }
        return true;
    }
}
