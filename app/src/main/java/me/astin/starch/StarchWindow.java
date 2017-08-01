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
import android.view.MotionEvent;
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
import me.astin.starch.ui.FlatToast;


public class StarchWindow extends Service implements View.OnTouchListener {
    private WindowManager mWindowManager;
    private NotificationManager mNotificationManager;
    private WindowCache sWindowCache = new WindowCache();
    private int sFocusedWindowId = -1;

    private FloatingButton overlayedButton;
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
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //overlayed button
        overlayedButton = new FloatingButton(this);
        overlayedButton.setColor(MyColor.BLUE.getColor());
        overlayedButton.setBackgroundDrawable(Utils.ColorFilter((BitmapDrawable) getResources().getDrawable(R.drawable.ic_window_restore), Color.WHITE), 
                                                  new int[] { Utils.dip2px(this, 8), Utils.dip2px(this, 8), Utils.dip2px(this, 8), Utils.dip2px(this, 8) });
        overlayedButton.setStroke(Utils.dip2px(this, 3), MyColor.BLUE.getColorAccent());
        overlayedButton.setOnTouchListener(this);

        FloatingView = new View(this);
        params = new StarchWindow.WindowParams(Utils.dip2px(this, 35), Utils.dip2px(this, 35), Gravity.TOP | Gravity.LEFT);
        _params = new StarchWindow.WindowParams(0, 0, Gravity.TOP | Gravity.LEFT);

        mWindowManager.addView(FloatingView, _params);
        mWindowManager.addView(overlayedButton, params);

        //test window
        WindowParams windowParams = new WindowParams(Utils.dip2px(this, 250), Utils.dip2px(this, 300), Gravity.TOP | Gravity.LEFT);
        Window testWindow = new Window(this, this, 1, windowParams);
        testWindow.setTitle("Test Window");
        testWindow.setMessage("테스트용 윈도우 입니다.\ntest window.");
        testWindow.setLayoutParams(windowParams);
        addContentWindow(testWindow, windowParams);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeAll();
        if (overlayedButton != null) {
            mWindowManager.removeView(overlayedButton);
        }
        if (FloatingView != null) {
            mWindowManager.removeView(FloatingView);
        }
    }

    public WindowManager getWindowManager() {
        return mWindowManager;
    }

    public final synchronized void addContentWindow(Window window, StarchWindow.WindowParams params) {
        sWindowCache.putCache(window.getId(), window);
        mWindowManager.addView(window, params);
        focus(window.getId());
    }

    public final synchronized void close(int id) {
        final Window window = getWindowById(id);

        if (window == null) {
            return;
        }

        if (!sWindowCache.isCached(id)) {
            return;
        }

        sWindowCache.removeCache(id);
        mWindowManager.removeView(window);
    }

    public final synchronized void closeAll() {
        Set < Integer > ids = sWindowCache.getCachedIds();
        Object[] ids_arr = ids.toArray();

        for (int i = 0; i < ids.size(); i++) {
            mWindowManager.removeView(getWindowById(ids_arr[i]));
            sWindowCache.removeCache(ids_arr[i]);
        }
    }

    public final synchronized boolean focus(int id) {
        Window window = getWindowById(id);
        return window.onFocus(true);
    }

    public final synchronized boolean unfocus(int id) {
        Window window = getWindowById(id);
        return window.onFocus(false);
    }

    public final synchronized void bringToFront(int id) {
        Window window = getWindowById(id);

        if (sWindowCache.isCached(id) == false) {
            return;
        }

        if (window == null) {
            return;
        }

        WindowManager.LayoutParams params = window.getLayoutParams();
        getWindowManager().removeView(window);
        getWindowManager().addView(window, params);
    }

    public void setFocusedWindow(int id) {
        if (sFocusedWindowId != -1) {
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

    public final synchronized void updateViewLayout(int id, StarchWindow.WindowParams params) {
        Window window = getWindowById(id);
        mWindowManager.updateViewLayout(window, params);
    }

    public static class WindowParams extends WindowManager.LayoutParams {
        public WindowParams() {
            super(200, 200,
                  WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                  PixelFormat.TRANSLUCENT);
        }

        public WindowParams(int width, int height) {
            super(width, height,
                  WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                  PixelFormat.TRANSLUCENT);
        }

        public WindowParams(int width, int height, int grav) {
            super(width, height,
                  WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                  PixelFormat.TRANSLUCENT);
            gravity = grav;
        }

        public WindowParams(int width, int height, int xpos, int ypos) {
            super(width, height,
                  WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                  PixelFormat.TRANSLUCENT);

            x = xpos;
            y = ypos;
            gravity = Gravity.LEFT | Gravity.TOP;
        }

        public WindowParams(int width, int height, int grav, int xpos, int ypos) {
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

            int newX = (int)(offsetX + x);
            int newY = (int)(offsetY + y);

            if (Math.abs(newX - this.originalXPos) < 10 && Math.abs(newY - this.originalYPos) < 10 && !this.moving) {
                return false;
            }

            if (params.y < 0) params.y = 0;
            if (params.y > this.HEIGHT - Utils.dip2px(this, 64)) params.y = this.HEIGHT - Utils.dip2px(this, 64);

            params.x = newX - (topLeftLocationOnScreen[0]);
            params.y = newY - (topLeftLocationOnScreen[1]);

            mWindowManager.updateViewLayout(this.overlayedButton, params);
            this.moving = true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (this.moving) {
                WindowManager.LayoutParams params = (LayoutParams) overlayedButton.getLayoutParams();

                if (params.y < 0) params.y = 0;
                if (params.y > this.HEIGHT - Utils.dip2px(this, 74)) params.y = this.HEIGHT - Utils.dip2px(this, 74);

                return true;
            }
            if (!this.moving && event.getX() >= 0 && event.getX() <= Utils.dip2px(this, 35) && event.getY() >= 0 && event.getY() <= Utils.dip2px(this, 35)) {
                
                return true;
            }
        }

        return false;
    }
}
