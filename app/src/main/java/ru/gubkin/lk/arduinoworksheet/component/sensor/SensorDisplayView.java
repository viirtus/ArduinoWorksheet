package ru.gubkin.lk.arduinoworksheet.component.sensor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import java.security.InvalidParameterException;
import java.util.Locale;

import ru.gubkin.lk.arduinoworksheet.util.Util;

/**
 * Created by root on 23.05.15.
 */
public class SensorDisplayView extends View {
    private static final String TAG = "SENSOR_DISPLAY";
    private Paint borderPaint;
    private Paint valueFontPaint;
    private Paint underValueFontPaint;

    private float borderWidth = 5;
    private int borderColor = 0xff666666;
    private float maxValue = 1024f;
    private float minValue = -1024f;
    private float value = 1;

    private int currentValueColor;


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

        underValueFontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        underValueFontPaint.setTypeface(valueTf);
        underValueFontPaint.setColor(0x11000000);


        borderWidth = Util.convertDpToPixel(borderWidth, context);

        borderPaint.setStrokeWidth(borderWidth);

        currentValueColor = Util.valueToColor(value, minValue, maxValue);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        RectF round = new RectF(0, 0, width, height);
        canvas.drawRoundRect(round, 50, 50, borderPaint);

        String valueFormat = formatNumber(value);
        valueFontPaint.setColor(currentValueColor);

        canvas.drawText(valueFormat, 0, (float) (height / 1.4), valueFontPaint);

        canvas.drawText(" 0000", 0, (float) (height / 1.4), underValueFontPaint);
        canvas.drawText("-----", 0, (float) (height / 1.4), underValueFontPaint);
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



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

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
            underValueFontPaint.setTextSize((float) (height / 1.5));
        }
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
        try {
            currentValueColor = Util.valueToColor(value, minValue, maxValue);
        } catch (InvalidParameterException ignored) {}
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }
}
