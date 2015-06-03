package ru.gubkin.lk.arduinoworksheet.component.servo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Locale;

import ru.gubkin.lk.arduinoworksheet.util.Util;

/**
 * Created by Андрей on 07.05.2015.
 */
public class RadialScaleView extends View {
    private final static String TAG = "SERVO_VIEW";

    private String name = "Сервопривод";
    private int circleColor = 0xffB2EBF2;
    private int arrowColor = 0xffE0F7FA;
    private int valueColor = 0xffB2EBF2;
    private int nameColor = 0xffB2EBF2;

    private Paint arrowPaint;
    private Paint markerPaint;
    private Paint textPaint;
    private Paint valueTextPaint;
    private Paint nameTextPaint;
    private Paint circlePaint;
    private Paint whitePaint;
    private RectF oval;
    private float bearing = 90;

    private float maxValue = 180;
    private float minValue = 0;

    private float value = 120;
    private int textHeight;
    private int nameTextHeight;
    private float circleStrokeWidth = 5;
    private float arrowStrokeWidth = 4;
    private float lineHeight = 7;
    private float bottomPadding = 25;
    private float circleRadius = 7;
//    private int arrowHeight = 4;

    public RadialScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        arrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arrowPaint.setStrokeWidth(arrowStrokeWidth);
        arrowPaint.setColor(arrowColor);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(circleColor);
        circlePaint.setStrokeWidth(circleStrokeWidth);
        circlePaint.setStyle(Paint.Style.STROKE);


        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(circleColor);
        textPaint.setStrokeWidth(circleStrokeWidth);
        textPaint.setTextSize(20);

        valueTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        valueTextPaint.setColor(valueColor);
        valueTextPaint.setTextSize(60);

        nameTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nameTextPaint.setColor(nameColor);
        nameTextPaint.setStrokeWidth(circleStrokeWidth);
        nameTextPaint.setTextSize(60);
        nameTextHeight = (int) nameTextPaint.measureText("yY");

        markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        markerPaint.setColor(circleColor);
        markerPaint.setStrokeWidth(circleStrokeWidth);


        whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        whitePaint.setStrokeWidth(circleStrokeWidth + 1);
        whitePaint.setColor(Color.WHITE);

        textHeight = (int) textPaint.measureText("yY");
        oval = new RectF();

        circleStrokeWidth = Util.convertDpToPixel(circleStrokeWidth, context);
        arrowStrokeWidth = Util.convertDpToPixel(arrowStrokeWidth, context);
        lineHeight = Util.convertDpToPixel(lineHeight, context);
        bottomPadding = Util.convertDpToPixel(bottomPadding, context);
        circleRadius = Util.convertDpToPixel(circleRadius, context);
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int px = getMeasuredWidth() / 2;
        int py = (int) (getMeasuredHeight() - bottomPadding);


        float radius = (float) Math.min(py, px) - Util.convertDpToPixel(2, getContext());

        oval.set(px - radius,
                py - radius, px + radius,
                py + radius);
        canvas.drawArc(oval, 180, 180, true, circlePaint);

//        canvas.drawLine(circleStrokeWidth, py, px * 2 - circleStrokeWidth, py, whitePaint);


        canvas.save();
        canvas.rotate(-bearing, px, py);
        float valueIterator = minValue;
        float step = (maxValue - minValue) / 12;
        // Рисуйте отметки каждые 15 и текст каждые 45.
        for (int i = 0; i < 13; i++) {
            // Нарисуйте метку.
            canvas.drawLine(px, py - radius, px, py - radius + lineHeight, markerPaint);
            canvas.save();
            canvas.translate(0, textHeight);
            // Нарисуйте основные точки
            if (i % 2 == 0) {
                String angle = String.format(Locale.ENGLISH, "%-5.1f",  valueIterator);
                float angleTextWidth = textPaint.measureText(angle);
                int angleTextX = (int) (px - angleTextWidth / 2);
                int angleTextY = (int) (py - radius + textHeight);
                canvas.drawText(angle, angleTextX, angleTextY, textPaint);
            }
            valueIterator += step;
            canvas.restore();
            canvas.rotate(15, px, py);
        }

        canvas.restore();

//        canvas.restore();

        String value_ = String.valueOf(value);
        float valueTextWidth = valueTextPaint.measureText(value_);
        int valueTextX = (int) (px  - valueTextWidth / 2 );
        int valueTextY = (int) (py - bottomPadding);
        canvas.drawText(value_, valueTextX, valueTextY, valueTextPaint);

        float nameTextWidth = nameTextPaint.measureText(name);
        int nameTextX = (int) (px - nameTextWidth / 2);
        int nameTextY = (int) (py + nameTextHeight);
        canvas.drawText(name, nameTextX, nameTextY, nameTextPaint);

        canvas.drawCircle(px, py, circleRadius, arrowPaint);

        float normal = Math.abs(minValue);

        float normalMax;
        float normalValue;

        if (minValue > 0) {
            normalMax = maxValue - normal;
            normalValue = value - normal;
        } else {
            normalMax = maxValue + normal;
            normalValue = value + normal;
        }


        float arrowAngle = (float) Math.toRadians(180 * (normalValue / normalMax));

        float arrowHeight = radius - lineHeight - 5;

        float arrowStartY = (float) (Math.sin(arrowAngle) * arrowHeight);
        float arrowStartX = (float) (Math.cos(arrowAngle) * arrowHeight);

        canvas.drawLine(px - arrowStartX, py - arrowStartY, px, py, arrowPaint);
    }

    public void setArrowColor(int arrowColor) {
        this.arrowColor = arrowColor;
        arrowPaint.setColor(arrowColor);
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }
}
