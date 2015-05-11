package ru.gubkin.lk.arduinoworksheet.component.led;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import ru.gubkin.lk.arduinoworksheet.R;

/**
 * Created by Андрей on 03.05.2015.
 */
public class LEDDialog extends Dialog {
    static private final String TITLE = "Настройки";
    private LED led;
    private Dialog dialog;
    private EditText titleEt;
    private EditText dataKeyOnEt;
    private EditText dataKeyOffEt;
    private Spinner colorSpinner;
    private Switch stateSwitch;

    public LEDDialog(Context context, LED led) {
        super(context);
        this.led = led;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.led_dialog, null);

        titleEt = (EditText) v.findViewById(R.id.servo_title_et);
        dataKeyOnEt= (EditText) v.findViewById(R.id.led_command_on_et);
        dataKeyOffEt= (EditText) v.findViewById(R.id.led_command_off_et);
        colorSpinner = (Spinner) v.findViewById(R.id.led_color_spinner);
        stateSwitch = (Switch) v.findViewById(R.id.led_state_switch);

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
                led.destroy();
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
                String inputDataKeyOn = dataKeyOnEt.getText().toString();
                String inputDataKeyOff = dataKeyOffEt.getText().toString();
                int inputColor = colorSpinner.getSelectedItemPosition();
                boolean inputState = stateSwitch.isChecked();

                if (!inputTitle.equals(led.getTitle())) {
                    led.setTitle(inputTitle);
                }
                if (!inputDataKeyOn.equals(led.getCommandOn())) {
                    led.setCommandOn(inputDataKeyOn);
                }
                if (!inputDataKeyOff.equals(led.getCommandOff())) {
                    led.setCommandOff(inputDataKeyOff);
                }
                if (inputColor != led.getColor().getColorId()) {
                    led.setColor(inputColor);
                }
                if (inputState != led.isActive()) {
                    led.toggleState(false);
                }
                me.hide();
            }
        });
    }

    private void initInputs() {
        titleEt.setText(led.getTitle());
        dataKeyOnEt.setText(led.getCommandOn());
        dataKeyOffEt.setText(led.getCommandOff());
        colorSpinner.setSelection(led.getColor().getColorId());
        stateSwitch.setChecked(led.isActive());
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
