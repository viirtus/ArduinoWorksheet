package ru.gubkin.lk.arduinoworksheet.component.sensor;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.R;
import ru.gubkin.lk.arduinoworksheet.bt.BluetoothHandler;
import ru.gubkin.lk.arduinoworksheet.component.Controller;
import ru.gubkin.lk.arduinoworksheet.component.servo.OnControlClickListener;
import ru.gubkin.lk.arduinoworksheet.component.servo.RadialScaleView;
import ru.gubkin.lk.arduinoworksheet.component.servo.Servo;
import ru.gubkin.lk.arduinoworksheet.component.servo.ServoDialog;
import ru.gubkin.lk.arduinoworksheet.db.SensorDBHandler;
import ru.gubkin.lk.arduinoworksheet.util.MessageHandler;

/**
 * Created by root on 11.05.15.
 */
public class SensorController extends Controller {
    private final BluetoothHandler bluetoothHandler;
    private ArrayList<Sensor> items;
    private View wrapper;
    private LayoutInflater inflater;
    private SensorDBHandler dbHandler;
    private SensorObserver observer;
    private MessageHandler messageHandler;
    private Button button;
    private LinearLayout layout;

    public SensorController(Context context, BluetoothHandler bluetoothHandler) {
        super(context);
        this.bluetoothHandler = bluetoothHandler;
        dbHandler = new SensorDBHandler(context);
        observer = new SensorObserver(dbHandler, this);
        messageHandler = new MessageHandler();
        bluetoothHandler.registerMessageHandler(messageHandler);

        items = SensorFactory.getSavedServo(dbHandler, messageHandler, observer);
    }

    @Override
    public void registerListeners() {

    }

    @Override
    public View getViewItem(LayoutInflater inflater, View convertView, ViewGroup parent) {
        if (wrapper == null) {
            this.inflater = inflater;
            wrapper = inflater.inflate(R.layout.linear_list, parent, false);
            registerListeners();
            renderList();
        }
        return wrapper;
    }

    private void renderList() {
        layout = (LinearLayout) wrapper.findViewById(R.id.wrapper_lv);
        layout.removeAllViews();
        for(final Sensor sensor : items) {
            View row = inflater.inflate(R.layout.sensor_item, layout, false);
            RadialScaleView view = (RadialScaleView) row.findViewById(R.id.sensor_one);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    SensorDialog dialog = new SensorDialog(context, sensor);
                    dialog.show();
                    return false;
                }
            });
            sensor.setSensorView(view);
            layout.addView(row);
        }
    }

    @Override
    public void expandView() {
        final float targetHeight = getFrameHeight();
        wrapper.getLayoutParams().height = 0;
        wrapper.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    for (int i = 0; i < items.size(); i ++) {
                        layout.getChildAt(i).setVisibility(View.VISIBLE);
                    }
                }
                wrapper.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                wrapper.requestLayout();
            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / context.getResources().getDisplayMetrics().density));
        wrapper.startAnimation(a);
    }

    @Override
    public void collapsedView() {
        final float targetHeight = getFrameHeight();
        button.setVisibility(View.GONE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    wrapper.setVisibility(View.GONE);
                    for (int i = 0; i < items.size(); i ++) {
                        layout.getChildAt(i).setVisibility(View.GONE);
                    }
                } else {
                    wrapper.getLayoutParams().height = (int) (targetHeight * (1 - interpolatedTime));
                    wrapper.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / context.getResources().getDisplayMetrics().density));
        wrapper.startAnimation(a);
    }

    public void notifyChange() {
        renderList();
        expandView();
    }

    public void deleteSensor(Sensor sensor) {
        items.remove(sensor);
    }

    @Override
    public void registerAddButton(Button button) {
        this.button = button;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.add(SensorFactory.getNew(dbHandler, messageHandler, observer));
                notifyChange();
            }
        });
    }
}
