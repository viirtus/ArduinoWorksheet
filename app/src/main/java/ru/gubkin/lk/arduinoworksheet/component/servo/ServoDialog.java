package ru.gubkin.lk.arduinoworksheet.component.servo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import ru.gubkin.lk.arduinoworksheet.R;

/**
 * Created by Андрей on 09.05.2015.
 */
public class ServoDialog  extends Dialog {
    static private final String TITLE = "Настройки";

    private final Context context;
    private final Servo servo;
    private Dialog dialog;
    private EditText titleEt;
    private EditText commandEt;
    private EditText maxValueEt;
    private EditText delayEt;

    public ServoDialog(Context context, Servo servo) {
        super(context);
        this.context = context;
        this.servo = servo;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.servo_dialog, null);
        titleEt = (EditText) v.findViewById(R.id.servo_title_et);
        commandEt = (EditText) v.findViewById(R.id.led_command_on_et);
        maxValueEt = (EditText) v.findViewById(R.id.servo_max_value_et);
        delayEt = (EditText) v.findViewById(R.id.servo_delay_et);
        builder.setView(v);
        addButtons(builder);
        initInputs();
        dialog = builder.create();
    }

    private void addButtons (AlertDialog.Builder builder) {
        final Dialog me = this;
        builder.setNegativeButton("Удалить", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                servo.destroy();
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
                String inputCommand = commandEt.getText().toString();

                int inputMaxValue;
                int inputDelay;
                try {
                    inputDelay = Integer.parseInt(delayEt.getText().toString());
                    inputMaxValue = Integer.parseInt(maxValueEt.getText().toString());

                } catch (Exception e) {
                    inputMaxValue = servo.getMaxValue();
                    inputDelay = servo.getDelay();
                }

                if (!inputTitle.equals(servo.getName())) {
                    servo.setName(inputTitle);
                }
                if (!inputCommand.equals(servo.getCommand())) {
                    servo.setCommand(inputCommand);
                }
                if (inputMaxValue != servo.getMaxValue()) {
                    servo.setMaxValue(inputMaxValue);
                }
                if (inputDelay != servo.getDelay()) {
                    servo.setDelay(inputDelay);
                }
                me.hide();
            }
        });
    }



    private void initInputs() {
        titleEt.setText(servo.getName());
        commandEt.setText(servo.getCommand());
        maxValueEt.setText(String.valueOf(servo.getMaxValue()));
        delayEt.setText(String.valueOf(servo.getDelay()));
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
