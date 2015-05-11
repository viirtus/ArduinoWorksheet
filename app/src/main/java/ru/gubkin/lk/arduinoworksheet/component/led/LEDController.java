package ru.gubkin.lk.arduinoworksheet.component.led;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import java.io.IOException;
import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.R;
import ru.gubkin.lk.arduinoworksheet.adapter.LedGridAdapter;
import ru.gubkin.lk.arduinoworksheet.bt.BluetoothHandler;
import ru.gubkin.lk.arduinoworksheet.component.Controller;
import ru.gubkin.lk.arduinoworksheet.db.LedDBHandler;

/**
 * Created by root on 05.05.15.
 */
public class LEDController extends Controller {
    private View wrapper;
    private GridView ledGrid;
    private LedGridAdapter ledGridAdapter;
    private ArrayList<LED> leds;
    private LedDBHandler ledDb;
    private LEDObserver ledObserver;
    private BluetoothHandler bluetoothHandler;
    private Button addButton;

    public LEDController(Context context, BluetoothHandler handler) {
        super(context);
        ledDb = new LedDBHandler(context);
        ledObserver = new LEDObserver(ledDb, this);
        leds = LEDFactory.getSavedLed(ledDb, ledObserver);
        ledGridAdapter = new LedGridAdapter(context, leds);
        bluetoothHandler = handler;
    }

    protected void processLed(LED l) {

        String command;
        if (l.isActive()) command = l.getCommandOn();
        else command = l.getCommandOff();
        try {
            bluetoothHandler.sendData(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerListeners() {
        ledGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LED led = (LED) ledGridAdapter.getItem(position);
                led.toggleState(true);
            }
        });
        ledGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                LED led = (LED) ledGridAdapter.getItem(position);
                Dialog dialog = new LEDDialog(context, led);
                dialog.show();
                return true;
            }
        });
    }

    @Override
    public View getViewItem(LayoutInflater inflater, View convertView, ViewGroup parent) {
        if (wrapper == null) {
            wrapper = inflater.inflate(R.layout.led_grid, parent, false);
            ledGrid = (GridView) wrapper.findViewById(R.id.led_grid);
            ledGrid.setAdapter(ledGridAdapter);
            ledObserver.setAdapter(ledGridAdapter);
            ledObserver.setDisplayedLeds(leds);
            registerListeners();
        }

        return wrapper;
    }

    @Override
    public void expandView() {

//        wrapper.measure(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);

        final float targetHeight = getFrameHeight();
        addButton.setVisibility(View.VISIBLE);

        wrapper.getLayoutParams().height = 0;
        ledGrid.getLayoutParams().height = 0;

        wrapper.setVisibility(View.VISIBLE);
        ledGrid.setVisibility(View.VISIBLE);

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                wrapper.getLayoutParams().height = (int) (targetHeight * interpolatedTime);
                ledGrid.getLayoutParams().height = (int) (targetHeight * interpolatedTime);
                wrapper.requestLayout();
                ledGrid.requestLayout();

            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / context.getResources().getDisplayMetrics().density));
        wrapper.startAnimation(a);
        ledGrid.startAnimation(a);
    }

    @Override
    public void collapsedView() {
        wrapper.measure(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);

        final float startHeight = getFrameHeight();
        addButton.setVisibility(View.GONE);

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    wrapper.setVisibility(View.GONE);
                    ledGrid.setVisibility(View.GONE);
                    wrapper.clearAnimation();
                } else {
                    wrapper.getLayoutParams().height = (int) (startHeight * (1 - interpolatedTime));
                    wrapper.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (startHeight / context.getResources().getDisplayMetrics().density));
        wrapper.startAnimation(a);
    }

    @Override
    public void registerAddButton(Button button) {
        addButton = button;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leds.add(LEDFactory.getNew(ledDb, ledObserver));
                ledGridAdapter.notifyDataSetChanged();
            }
        });
    }


}
