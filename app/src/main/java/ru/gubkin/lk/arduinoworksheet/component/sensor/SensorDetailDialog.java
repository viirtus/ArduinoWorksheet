package ru.gubkin.lk.arduinoworksheet.component.sensor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;

import ru.gubkin.lk.arduinoworksheet.MainActivity;
import ru.gubkin.lk.arduinoworksheet.R;
import ru.gubkin.lk.arduinoworksheet.component.servo.RadialScaleView;
import ru.gubkin.lk.arduinoworksheet.util.Util;

/**
 * Created by Андрей on 24.05.2015.
 */
public class SensorDetailDialog extends Dialog {
    private Sensor sensor;
    private Dialog dialog;

    public SensorDetailDialog(Context context, Sensor sensor) {
        super(context, R.style.DialogSlideAnim);
        this.sensor = sensor;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.sensor_item_radiate, null);
        builder.setTitle(sensor.getName());
        sensor.setSensorRadialView((RadialScaleView) v.findViewById(R.id.sensor_one));
        builder.setView(v);
        dialog = builder.create();
    }

    @Override
    public void show() {
        dialog.show();
    }

    @Override
    public void hide() {
        dialog.hide();
        sensor.setSensorRadialView(null);
    }
}
