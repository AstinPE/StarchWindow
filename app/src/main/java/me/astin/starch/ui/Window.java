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
import me.astin.starch.ui.FrameLayoutParams;
import me.astin.starch.ui.Divider;
import me.astin.starch.R;


public class Window extends FrameLayout {
    private StarchWindow mContext;
    private Context vContext;
    private final int id;
    private boolean focused = false;
    private StarchWindow.WindowParams params = null;
    private int window_flag;
    
    private GradientDrawable background_border, background_border_focus;
    private TextView title = null;
    
    private int size, margin_size;
    private String titleStr = "StarchWindow";
    
    private boolean resized, dragable = true, resizeable = true;
    private float mx, my, originalWidth, originalHeight;
    private float offsetX;
    private float offsetY;
    private boolean moving;

    public interface onDismissListener {
        public void onWindowDismiss(Window window);
    }
    
	
    public Window(final Context context, final StarchWindow service, final int id, final StarchWindow.WindowParams _params) {
        super(context);
        this.mContext = service;
        this.vContext = context;
        this.id = id;
        this.params = _params;
        
        final Window thiz = this;
        
        //unfocused background drawable
		background_border = new GradientDrawable();
        background_border.setStroke(Utils.dip2px(context, 1), Color.BLACK);
        background_border.setColor(Color.WHITE);
        
        //focused background drawable
        background_border_focus = new GradientDrawable();
        background_border_focus.setStroke(Utils.dip2px(context, 1), Color.rgb(53, 182, 229));
        background_border_focus.setColor(Color.WHITE);
        
        //default size
        size = Utils.dip2px(context, 24);
        margin_size = Utils.dip2px(context, 1);
        
        //image padding
        int[] pad = new int[] { Utils.dip2px(context, 3), 
            Utils.dip2px(context, 3), 
            Utils.dip2px(context, 3), 
            Utils.dip2px(context, 5) };
        
        //close
        final MaterialButton iconView = new MaterialButton(context);
        iconView.setEffectColor(MyColor.RED.getColorAccent());
        iconView.setBackgroundDrawable(Utils.ColorFilter((BitmapDrawable)context.getResources().getDrawable(R.drawable.ic_close_white_48dp), MyColor.RED.getColor()), pad);
        iconView.setOnClickListener(new MaterialButton.OnClickListener() {
            public void onClick(View v) {
                mContext.close(id);
            }
        });
        //close button params
        FrameLayoutParams iconParams = new FrameLayoutParams(size, size, Gravity.TOP|Gravity.RIGHT, new int[] { 0, margin_size, margin_size, 0 });
        super.addView(iconView, iconParams);
        
        //menu
        final MaterialButton menuView = new MaterialButton(context);
        menuView.setEffectColor(MyColor.BLUE.getColorAccent());
        menuView.setBackgroundDrawable(Utils.ColorFilter((BitmapDrawable)context.getResources().getDrawable(R.drawable.ic_menu_white_48dp), Color.BLACK), pad);
        menuView.setOnClickListener(new MaterialButton.OnClickListener() {
            public void onClick(View v) {
                
            }
        });
        //menu button params
        FrameLayoutParams menuParams = new FrameLayoutParams(size, size, Gravity.TOP|Gravity.LEFT, new int[] { margin_size, margin_size, 0, 0 });
        super.addView(menuView, menuParams);
        
        title = new TextView(vContext);
        title.setAllCaps(false);
        title.setText("");
        title.setTextSize(20);
        title.setTextColor(Color.BLACK);
        final View.OnTouchListener listener = new View.OnTouchListener() {
            public boolean onTouch(View _view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    moving = false;
                    
                    int[] location = new int[2];
                    thiz.getLocationOnScreen(location);
                    
                    offsetX = event.getRawX() - location[0];
                    offsetY = event.getRawY() - location[1];
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    float x = event.getRawX();
                    float y = event.getRawY();

                    int X = (int) (x - offsetX);
                    int Y = (int) (y - offsetY - Utils.dip2px(vContext, 25));

                    _params.x = X;
                    _params.y = Y;

                    mContext.updateViewLayout(id, _params);
                    moving = true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (moving) {
                        
                    }
                    if (!moving && event.getX() >= 0 && event.getX() <= _params.width && event.getY() >= 0 && event.getY() <= _params.height) {
                        
                    }
                }

                return true;
            }
        };
        title.setOnTouchListener(listener);
        FrameLayoutParams titleParams = new FrameLayoutParams(-1, size, Gravity.TOP|Gravity.LEFT, new int[] { Utils.dip2px(vContext, 25), margin_size, Utils.dip2px(vContext, 25), 0 });
        title.setLayoutParams(titleParams);
        super.addView(title);
        
        //top bar divider
        Divider topDivider = new Divider(context);
        topDivider.setColor(Color.BLACK);
        //divider params
        FrameLayoutParams dividerParams = new FrameLayoutParams(-1, margin_size * 2, Gravity.TOP|Gravity.LEFT, new int[] { margin_size, margin_size + size, margin_size, 0 });
        super.addView(topDivider, dividerParams);
        
        //bottom divider
        Divider bottomDivider = new Divider(context);
        bottomDivider.setColor(Color.BLACK);
        //divider params
        FrameLayoutParams bottomParams = new FrameLayoutParams(-1, margin_size * 2, Gravity.BOTTOM|Gravity.BOTTOM, new int[] { margin_size, 0, margin_size, margin_size + Utils.dip2px(context, 20) });
        super.addView(bottomDivider, bottomParams);
        
        final Button resizeButton = new Button(context);
        final BitmapDrawable resize_res = (BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_resize_bottom_right);
        FrameLayoutParams resizeParams = new FrameLayoutParams(Utils.dip2px(context, 20), Utils.dip2px(context, 20), Gravity.BOTTOM|Gravity.RIGHT, new int[] { 0, 0, margin_size, margin_size });
        resizeButton.setBackgroundDrawable(Utils.ColorFilter(resize_res, Color.BLACK));
        resizeButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                final MotionEvent _event = event;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(_event.getAction() == MotionEvent.ACTION_DOWN) {
                            resized = false;
                            originalWidth = thiz.getLayoutParams().width;
                            originalHeight = thiz.getLayoutParams().height;
                            mx = _event.getRawX();
                            my = _event.getRawY();
                            resizeButton.setBackgroundDrawable(Utils.ColorFilter(resize_res, Color.rgb(53, 182, 229)));
                        }
                        if(_event.getAction() == MotionEvent.ACTION_MOVE) {
                            if(resizeable) {
                                int dx = (int)mx - (int)_event.getRawX();
                                int dy = (int)my - (int)_event.getRawY();
                        
                                int X = (int)originalWidth - dx;
                                int Y = (int)originalHeight - dy;
                                _params.width = X;
                                _params.height = Y;
                                mContext.updateViewLayout(thiz.id, _params);
                                resized = true;
                            }
                        }
                        if(_event.getAction() == MotionEvent.ACTION_UP) {
                            resizeButton.setBackgroundDrawable(Utils.ColorFilter(resize_res, Color.BLACK));
                        }
                    }
                }, 5);
                return true;
            }
        });
        super.addView(resizeButton, resizeParams);
    }
    
    public void setTitle(String str) {
        titleStr = str;
        title.setText(str);
    }
    
    public void setMessage(String message) {
        TextView messageView = new TextView(vContext);
        messageView.setText(message);
        messageView.setTextColor(Color.BLACK);
        messageView.setTextSize(17);
        messageView.setAllCaps(false);
        messageView.setGravity(Gravity.TOP|Gravity.LEFT);
        messageView.setPadding(margin_size * 5, margin_size * 3, margin_size * 5, margin_size * 3);
        
        FrameLayoutParams messageParams = new FrameLayoutParams(-1, -1, Gravity.TOP|Gravity.LEFT, new int[] { margin_size, size + margin_size * 3, margin_size, size + margin_size * 3 });
        super.addView(messageView, messageParams);
    }
    
    public void setView(View content) {
        FrameLayoutParams viewParams = new FrameLayoutParams(-1, -1, Gravity.CENTER, new int[] { margin_size, size + margin_size * 3, margin_size, size + margin_size * 3 });
        super.addView(content, viewParams);
    }
    
    public void setFlag(int flag) {
        this.window_flag = flag;
    }
    
    @Override
    public int getId() {
        return this.id;
    }
    
    @Override
    public StarchWindow.WindowParams getLayoutParams() {
        return this.params;
    }
    
    @Override
    public void setLayoutParams(ViewGroup.LayoutParams _params) {
        this.params = (StarchWindow.WindowParams) _params;
        super.setLayoutParams(_params);
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
            super.setBackgroundDrawable(background_border_focus);
        } else {
            super.setBackgroundDrawable(background_border);
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
