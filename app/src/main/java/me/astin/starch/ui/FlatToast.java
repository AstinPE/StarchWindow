package me.astin.starch.ui;

import android.widget.Toast;
import android.widget.TextView;
import android.view.Gravity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import me.astin.starch.Utils;


public class FlatToast extends Toast {
    private Context mContext;
    private int backColor = Color.parseColor("#212121");
    private TextView textView;
    private GradientDrawable gradientDrawable;
    
    public FlatToast(Context context) {
        super(context);
        mContext = context;
        
        //toast text
        textView = new TextView(context);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(17);
        textView.setPadding(Utils.dip2px(context, 15), Utils.dip2px(context, 8), Utils.dip2px(context, 15), Utils.dip2px(context, 8));
        
        //toast background
        gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color.parseColor("#212121"));
        gradientDrawable.setAlpha(200);
        gradientDrawable.setCornerRadius(Utils.dip2px(context, 10));
    }
    
    public void showText(String text) {
        textView.setText(text);
        textView.setBackgroundDrawable(gradientDrawable);
        super.setDuration(Toast.LENGTH_SHORT);
        super.setView(textView);
        super.show();
    }
}
