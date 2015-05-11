package ru.gubkin.lk.arduinoworksheet.component.servo;

import android.content.DialogInterface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import ru.gubkin.lk.arduinoworksheet.R;

/**
 * Created by Андрей on 09.05.2015.
 */
public class OnControlClickListener implements View.OnTouchListener, View.OnClickListener {
    private RadialScaleView view;
    private Servo servo;
    private long lastCommandTime = 0;
    public OnControlClickListener(RadialScaleView view, Servo servo) {
        this.view = view;
        this.servo = servo;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        int value = servo.getValue();
        switch (id) {
            case R.id.servo_up_btn:
                value++;
                break;
            case R.id.servo_dwn_btn:
                value--;
                break;

        }
        if (value <= servo.getMaxValue() && value >= 0) {
            servo.setValue(value);
            view.setValue(value);
            view.invalidate();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                if (lastCommandTime == 0) lastCommandTime = System.currentTimeMillis();
                if (System.currentTimeMillis() - lastCommandTime > servo.getDelay()) {
                    onClick(v);
                    lastCommandTime = System.currentTimeMillis();
                }
                break;
            case MotionEvent.ACTION_UP:
                lastCommandTime = 0;
                break;
        }
        return false;
    }
}