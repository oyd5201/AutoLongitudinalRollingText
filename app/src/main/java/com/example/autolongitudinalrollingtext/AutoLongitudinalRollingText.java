package com.example.autolongitudinalrollingtext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AutoLongitudinalRollingText extends TextView {
    private TextPaint textPaint = null;//绘图样式
    private String text = "";//文本内容
    private String text2 = "";//
    private List<String> listText = new ArrayList<>();  //存储单字换行的list
    private StaticLayout layout;
    private float step = 0f;//文字的纵坐标

    private float temp_view_plus_two_text_length = 0.0f;//用于计算的临时变量
    public AutoLongitudinalRollingText(Context context) {
        super(context);
    }

    public AutoLongitudinalRollingText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoLongitudinalRollingText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init() {

        textPaint = getPaint();
        textPaint.setColor(getCurrentTextColor());
        textPaint.setTextSize(getTextSize());
        textPaint.setAntiAlias(true);

        text = getText().toString();
        text2 = text + " "+text;


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("", String.format("onMeasure width = %d,height=%d", getWidth(), getHeight()));

        Log.e(getWidth() + " 高", getHeight() + "");
    }

    private int getAvailableWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    //判断textview是否越界
    private boolean isOverFlowed() {
        Paint paint = getPaint();
        float width = paint.measureText(getText().toString());
        if (width > getAvailableWidth()) return true;
        return false;
    }

    private void setRollingText() {
        //如果越界就滚动
        if (isOverFlowed()) {
            char a[] = text2.toCharArray();
            String textStr = "";
            listText.clear();
            for (int i = 0; i < a.length; i++) {
                listText.add(String.valueOf(a[i]));
            }
//            listText.add(String.valueOf(a[0]));
            for (int i = 0; i < listText.size(); i++) {
                textStr += listText.get(i)+"\r\n";
            }
            text2 = textStr;
            layout  = new StaticLayout(text2, textPaint, 300,
                    Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            invalidate();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
//        setRollingText();
        setRollingText();


        canvas.save();
        canvas.translate(10, 10-step);

        if(layout!=null)
        layout.draw(canvas);
        canvas.restore();

        step +=2;

        if(step > textPaint.measureText(text)+100)
            step =  0;
        if(listText.size()>0){
            text2 = "";
            for (int i = 0; i < listText.size(); i++) {

                text2 += listText.get(i);
            }
        }

    }
}