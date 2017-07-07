package me.astin.starch.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.MotionEvent;
import android.view.KeyEvent;
import android.view.View.OnTouchListener;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.util.TypedValue;
import java.lang.Runnable;
import android.os.Handler;


import me.astin.starch.StarchWindow;
import me.astin.starch.Utils;
import me.astin.starch.ui.Divider;
import me.astin.starch.R;


public class Window extends FrameLayout {
    private StarchWindow mContext;
    private final int id;
    private boolean focused = false;

    public interface onDismissListener {
        public void onWindowDismiss(Window window);
    }
    
	
    public Window(final Context context, final StarchWindow service, final int id) {
        super(context);
        this.mContext = service;
        this.id = id;
        
		super.setBackgroundDrawable((NinePatchDrawable) context.getResources().getDrawable(R.drawable.border));
    }
    
    public void setTitle(String title) {
        
    }
    
    public void setMessage(String message) {
        
    }
    
    public void setView(View content) {
        
    }
    
    public void setWindowParams(StarchWindow.WindowParams params) {
        
    }
    
    @Override
    public int getId() {
        return this.id;
    }
	
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mContext.getFocusedWindow() != this) {
                mContext.focus(this.id);
            }
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_OUTSIDE:
                if (mContext.getFocusedWindow() == this) {
                    mContext.unfocus(this.id);
                }
                break;
        }
        return true;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                mContext.unfocus(this.id);
                return true;
        }

        return super.dispatchKeyEvent(event);
    }

    public boolean onFocus(boolean focus) {
        if (focus == focused) {
            return false;
        }

        focused = focus;

        if (focus) {
            super.setBackgroundResource(R.drawable.border_focused);
        } else {
            super.setBackgroundResource(R.drawable.border);
        }

        if (focus) {
            mContext.setFocusedWindow(this.id);
        } else {
            if (mContext.getFocusedWindow() == this) {
                
            }
        }
        return true;
	}
}
