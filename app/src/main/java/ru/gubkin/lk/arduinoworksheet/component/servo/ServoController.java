package ru.gubkin.lk.arduinoworksheet.component.servo;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.R;
import ru.gubkin.lk.arduinoworksheet.bt.BluetoothHandler;
import ru.gubkin.lk.arduinoworksheet.component.Controller;
import ru.gubkin.lk.arduinoworksheet.component.GroupHeader;
import ru.gubkin.lk.arduinoworksheet.db.ServoDBHandler;

/**
 * Created by Андрей on 07.05.2015.
 */
public class ServoController extends Controller {
    private View wrapper;
    private ArrayList<Servo> items;
    private BluetoothHandler handler;
    private ServoObserver observer;
    private ServoDBHandler dbHandler;
    private Button addButton;
    private LayoutInflater inflater;
    public ServoController(Context context, BluetoothHandler handler) {
        super(context);
        this.handler = handler;
        items = new ArrayList<>();
        dbHandler = new ServoDBHandler(context);
        observer = new ServoObserver(this, dbHandler);

        items = ServoFactory.getSavedServo(dbHandler, observer);
    }

    public void processServo(Servo servo) {

        String command = servo.getCommand() + servo.getValue();
        try {
            handler.sendData(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerListeners() {

    }

    @Override
    public View getViewItem(LayoutInflater inflater, View convertView, ViewGroup parent) {
        this.inflater = inflater;
        if (wrapper == null) {
            wrapper = inflater.inflate(R.layout.servo_list, parent, false);
            registerListeners();
            renderList();
        }
        return wrapper;
    }

    public void deleteServo(Servo servo) {
        items.remove(servo);
    }

    public void notifyChange() {
        renderList();
    }

    private void renderList() {
        LinearLayout layout = (LinearLayout) wrapper.findViewById(R.id.servo_lv);
        layout.removeAllViews();
        for(final Servo servo : items) {
            View row = inflater.inflate(R.layout.servo_item, layout, false);
            ImageButton btnUp = (ImageButton) row.findViewById(R.id.servo_up_btn);
            ImageButton btnDwn = (ImageButton) row.findViewById(R.id.servo_dwn_btn);
            RadialScaleView view = (RadialScaleView) row.findViewById(R.id.servo_one);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Dialog dialog = new ServoDialog(context, servo);
                    dialog.show();
                    return false;
                }
            });
            OnControlClickListener listener = new OnControlClickListener(view, servo);
            btnDwn.setOnTouchListener(listener);
            btnDwn.setOnClickListener(listener);
            btnUp.setOnTouchListener(listener);
            btnUp.setOnClickListener(listener);
            servo.setServoView(view);
            layout.addView(row);
        }

    }

    @Override
    public void expandView() {
        final float targetHeight = getFrameHeight();
        wrapper.getLayoutParams().height = 0;
        wrapper.setVisibility(View.VISIBLE);
        addButton.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
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
        addButton.setVisibility(View.GONE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    wrapper.setVisibility(View.GONE);
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

    @Override
    public void registerAddButton(Button button) {
        addButton = button;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Servo newOne = ServoFactory.getNew(dbHandler, observer);
                items.add(newOne);
                renderList();
            }
        });
    }

}
