package me.astin.starch;

import android.app.Service;
import android.app.PendingIntent;
import android.app.NotificationManager;
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
import android.view.View;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.FrameLayout;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.support.v4.app.NotificationCompat;
import java.util.Set;

import me.astin.starch.ui.Window;
import me.astin.starch.WindowCache;
import me.astin.starch.ui.MaterialButton;
import me.astin.starch.ui.FloatingButton;
import me.astin.starch.ui.MyColor;


public class StarchWindow extends Service implements View.OnTouchListener {
    private WindowManager mWindowManager;
    private WindowCache sWindowCache = new WindowCache();
    private int sFocusedWindowId = -1;
    
	private Button overlayedButton;
	private View FloatingView;
	private StarchWindow.WindowParams params, _params;
    
	private float offsetX;
    private float offsetY;
    private int originalXPos;
    private int originalYPos;
    private boolean moving;
    private boolean showing;
    
    private int WIDTH;
    private int HEIGHT;
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        createNotification();
		
		overlayedButton = new FloatingButton(this)
			.setColor(MyColor.BLUE.getColor())
			.setDrawable((BitmapDrawable) getResources().getDrawable(R.drawable.ic_list_white_48dp))
			.setStroke(Utils.dip2px(this, 3) , MyColor.BLUE.getColorAccent())
			.setPadding(Utils.dip2px(this, 13), Utils.dip2px(this, 10), Utils.dip2px(this, 13), Utils.dip2px(this, 10))
			.show();
        overlayedButton.setOnTouchListener(this);

		FloatingView = new View(this);
        params = new StarchWindow.WindowParams(Utils.dip2px(this, 50), Utils.dip2px(this, 50));
		_params = new StarchWindow.WindowParams(0, 0);
		
		mWindowManager.addView(FloatingView, _params);
		mWindowManager.addView(overlayedButton, params);
		
		final Window w1 = new Window(this, this, 1);
		this.addContentWindow(w1, 1, new WindowParams(Utils.dip2px(this, 100), Utils.dip2px(this, 100), 0, 0, Gravity.CENTER));
		
		//final Window w2 = new Window(this, this, 2);
		//this.addContentWindow(w2, 2, new WindowParams(Utils.dip2px(this, 100), Utils.dip2px(this, 100), Utils.dip2px(this, 50), Utils.dip2px(this, 50), Gravity.CENTER));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
		closeAll();
		if(overlayedButton != null) {
			mWindowManager.removeView(overlayedButton);
		}
		if(FloatingView != null) {
			mWindowManager.removeView(FloatingView);
		}
    }
	
	public WindowManager getWindowManager() {
		return mWindowManager;
	}
    
    public void addContentWindow(Window window, int id, StarchWindow.WindowParams params) {
        sWindowCache.putCache(id, window);
		mWindowManager.addView(window, params);
        focus(id);
    }
    
    public void close(int id) {
        sWindowCache.removeCache(id);
		mWindowManager.removeView(getWindowById(id));
    }
	
	public void closeAll() {
		Set<Integer> ids = sWindowCache.getCachedIds();
		Object[] ids_arr = ids.toArray();
		
		for(int i = 0; i < ids.size(); i++) {
			mWindowManager.removeView(getWindowById(ids_arr[i]));
		}
	}
    
    public boolean focus(int id) {
        Window window = getWindowById(id);
        return window.onFocus(true);
    }

    public boolean unfocus(int id) {
        Window window = getWindowById(id);
        return window.onFocus(false);
    }
    
    public void bringToFront(int id) {
        Window window = getWindowById(id);
        if(sFocusedWindowId == id) {
            return;
        } else {
            window.bringToFront();
        }
    }
    
    public void setFocusedWindow(int id) {
        if(sFocusedWindowId != -1) {
            unfocus(sFocusedWindowId);
        }
        sFocusedWindowId = id;
        bringToFront(id);
    }
    
    public Window getFocusedWindow() {
        return getWindowById(sFocusedWindowId);
    }
    
    public Window getWindowById(int id) {
        return sWindowCache.getCache(id);
    }
	
	public void updateViewLayout(int id, StarchWindow.WindowParams params) {
		Window window = getWindowById(id);
		window.setLayoutParams(params);
		mWindowManager.updateViewLayout(window, params);
	}
    
    public void setOnTouchHandler(Window window) {
        
    }
    
    public void createNotification() {
        Intent notificationIntent = new Intent(getApplicationContext(), StarchWindow.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, notificationIntent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_window_restore).setTicker("Click to start launcher").setWhen(System.currentTimeMillis())
            .setContentTitle("Start launcher")
            .setContentText("Click to start launcher");

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
	
	
	public static class WindowParams extends WindowManager.LayoutParams {
		public WindowParams() {
			super(200, 200,
				  WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, 
				  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
				  PixelFormat.TRANSLUCENT);
				  
			x = y = 0;
			gravity = Gravity.LEFT|Gravity.TOP;
		}
		
		public WindowParams(int width, int height) {
			super(width, height,
				 WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, 
				 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
				 PixelFormat.TRANSLUCENT);
				 
			x = y = 0;
			gravity = Gravity.LEFT|Gravity.TOP;
		}
		
		public WindowParams(int width, int height, int grav) {
			super(width, height,
				  WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, 
				  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
				  PixelFormat.TRANSLUCENT);

			x = y = 0;
			gravity = grav;
		}
		
		public WindowParams(int width, int height, int xpos, int ypos) {
			super(width, height,
				  WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, 
				  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
				  PixelFormat.TRANSLUCENT);
				  
			x = xpos;
			y = ypos;
			gravity = Gravity.LEFT|Gravity.TOP;
		}
		
		public WindowParams(int width, int height, int xpos, int ypos, int grav) {
			super(width, height,
				  WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, 
				  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
				  PixelFormat.TRANSLUCENT);

			x = xpos;
			y = ypos;
			gravity = grav;
		}
	}
	

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        this.WIDTH = mWindowManager.getDefaultDisplay().getWidth();
        this.HEIGHT = mWindowManager.getDefaultDisplay().getHeight();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getRawX();
            float y = event.getRawY();

            this.moving = false;

            int[] location = new int[2];
            this.overlayedButton.getLocationOnScreen(location);

            this.originalXPos = location[0];
            this.originalYPos = location[1];

            this.offsetX = originalXPos - x;
            this.offsetY = originalYPos - y;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE && !showing) {
            int[] topLeftLocationOnScreen = new int[2];
            this.FloatingView.getLocationOnScreen(topLeftLocationOnScreen);

            float x = event.getRawX();
            float y = event.getRawY();

            WindowManager.LayoutParams params = (LayoutParams) overlayedButton.getLayoutParams();

            int newX = (int) (offsetX + x);
            int newY = (int) (offsetY + y);

            if (Math.abs(newX - this.originalXPos) < 10 && Math.abs(newY - this.originalYPos) < 10 && !this.moving) {
                return false;
            }

            if(params.y < 0) params.y = 0;
            if(params.y > this.HEIGHT - Utils.dip2px(this, 64)) params.y = this.HEIGHT - Utils.dip2px(this, 64);

            params.x = newX - (topLeftLocationOnScreen[0]);
            params.y = newY - (topLeftLocationOnScreen[1]);

            mWindowManager.updateViewLayout(this.overlayedButton, params);
            this.moving = true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (this.moving) {
                WindowManager.LayoutParams params = (LayoutParams) overlayedButton.getLayoutParams();

                if(params.y < 0) params.y = 0;
                if(params.y > this.HEIGHT - Utils.dip2px(this, 74)) params.y = this.HEIGHT - Utils.dip2px(this, 74);

                return true;
            }
            if (!this.moving && event.getX() >= 0 && event.getX() <= Utils.dip2px(this, 50) && event.getY() >= 0 && event.getY() <= Utils.dip2px(this, 50)) {
                
                return true;
            }
        }

        return false;
    }
}
