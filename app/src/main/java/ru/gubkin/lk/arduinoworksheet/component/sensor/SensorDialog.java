package ru.gubkin.lk.arduinoworksheet.component.sensor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import ru.gubkin.lk.arduinoworksheet.R;
import ru.gubkin.lk.arduinoworksheet.component.servo.Servo;

/**
 * Created by Андрей on 09.05.2015.
 */
public class SensorDialog extends Dialog {
    static private final String TITLE = "Настройки";

    private final Sensor sensor;
    private Dialog dialog;
    private EditText titleEt;
    private EditText startEt;
    private EditText endEt;
    private EditText maxEt;

    public SensorDialog(Context context, Sensor sensor) {
        super(context);
        this.sensor = sensor;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.sensor_dialog, null);
        titleEt = (EditText) v.findViewById(R.id.sensor_title_et);
        startEt = (EditText) v.findViewById(R.id.sensor_start_et);
        endEt = (EditText) v.findViewById(R.id.sensor_end_et);
        maxEt = (EditText) v.findViewById(R.id.sensor_max_et);
        builder.setView(v);
        addButtons (builder);
        initInputs();
        dialog = builder.create();
    }

    private void addButtons (AlertDialog.Builder builder) {
        final Dialog me = this;
        builder.setNegativeButton("Удалить", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sensor.destroy();
                me.hide();
            }
        });
        builder.setNeutralButton("Отмена", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                me.hide();
            }
        });
        builder.setPositiveButton("Ок", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputTitle = titleEt.getText().toString();
                String inputStart = startEt.getText().toString();
                String inputEnd = endEt.getText().toString();

                float inputMaxValue;
                try {
                    inputMaxValue = Integer.parseInt(maxEt.getText().toString());

                } catch (Exception e) {
                    inputMaxValue = sensor.getMaxValue();
                }

                if (!inputTitle.equals(sensor.getName())) {
                    sensor.setName(inputTitle);
                }
                if (!inputStart.equals(sensor.getStartPattern())) {
                    sensor.setStartPattern(inputStart);
                }
                if (!inputStart.equals(sensor.getEndPattern())) {
                    sensor.setEndPattern(inputEnd);
                }
                if (inputMaxValue != sensor.getMaxValue()) {
                    sensor.setMaxValue(inputMaxValue);
                }
                me.hide();
            }
        });
    }



    private void initInputs() {
        titleEt.setText(sensor.getName());
        startEt.setText(sensor.getStartPattern());
        endEt.setText(sensor.getEndPattern());
        maxEt.setText(String.valueOf(sensor.getMaxValue()));
    }

    @Override
    public void show() {
        dialog.show();
    }

    @Override
    public void hide() {
        dialog.hide();
    }

}
