package ru.gubkin.lk.arduinoworksheet.component.sensor;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Locale;

import ru.gubkin.lk.arduinoworksheet.util.Util;

/**
 * Created by root on 23.05.15.
 */
public class SensorDisplayView extends View {
    private static final String TAG = "SENSOR_DISPLAY";
    private Paint borderPaint;
    private Paint valueFontPaint;

    private float borderWidth = 5;
    private int borderColor = 0xff666666;

    public SensorDisplayView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    private void init(Context context) {
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(borderColor);
        borderPaint.setStyle(Paint.Style.STROKE);

        valueFontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Typeface valueTf = Typeface.createFromAsset(getContext().getAssets(), "digital-7.ttf");
        valueFontPaint.setTypeface(valueTf);

        borderWidth = Util.convertDpToPixel(borderWidth, context);

        borderPaint.setStrokeWidth(borderWidth);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();

        RectF r = new RectF(0, 0, width, height);
        canvas.drawRoundRect(r, 20, 20, borderPaint);
        float value = 1;

        String valueFormat = formatNumber(value);
        valueFontPaint.setColor(valueToColor(value));

        canvas.drawText(valueFormat, 0, (float) (getHeight() / 1.5), valueFontPaint);
    }

    private String formatNumber(float number) {
        String format = "";
        float n = Math.abs(number);
        if (n == (int) n) {
            format = String.format("%04d", (int) n);
        } else {
            format = String.format(Locale.ENGLISH, "%04.3f", n);
        }
        if (number < 0) {
            format = "-" + format;
        } else {
            format = " " + format;
        }
        return format;
    }

    private int valueToColor(float value) {
        float maxValue = 1024;
        float minValue = -200;
        float middle = (maxValue + minValue) / 2;

        float width = maxValue - minValue;

        int r = 0;
        int g = 0;
        int b = 0;

        if (value < middle) {
            b = (int) (255 * (Math.abs((value / minValue))));
            g = (int) (255 * (1 - Math.abs(value / minValue)));
        } else {
            g = (int) (255 * (1 - (value / maxValue)));
            r = (int) (255 * (value / maxValue));
        }

        return Color.rgb(r, g, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int desiredWidth = 100;
        int desiredHeight = 100;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
            valueFontPaint.setTextSize((float) (height / 1.5));
        }
    }
}
