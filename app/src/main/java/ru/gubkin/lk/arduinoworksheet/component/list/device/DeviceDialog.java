package ru.gubkin.lk.arduinoworksheet.component.list.device;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import ru.gubkin.lk.arduinoworksheet.R;

/**
 * Created by root on 05.06.15.
 */
public class DeviceDialog extends Dialog {

    private static final String TITLE = "Устройство";
    private final DeviceItem device;
    private final EditText deviceName;
    private final EditText deviceIp;
    private final EditText devicePort;
    private final AlertDialog dialog;

    public DeviceDialog(Context context, DeviceItem device) {
        super(context);
        this.device = device;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.device_dialog, null);
        deviceName = (EditText) v.findViewById(R.id.device_name_et);
        deviceIp = (EditText) v.findViewById(R.id.device_ip_et);
        devicePort = (EditText) v.findViewById(R.id.device_port_et);
        builder.setView(v);
        addButtons(builder);
        initInputs();
        dialog = builder.create();
    }

    private void addButtons(AlertDialog.Builder builder) {
        final Dialog me = this;
        builder.setNegativeButton("Удалить", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                device.destroy();
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
                String inputName = deviceName.getText().toString();
                String inputIp = deviceIp.getText().toString();
                String inputPort = devicePort.getText().toString();

                if (!inputName.equals(device.getName())) {
                    device.setName(inputName);
                }
                if (!inputIp.equals(device.getAddress())) {
                    device.setAddress(inputIp);
                }
                if (!inputPort.equals(device.getPort())) {
                    device.setPort(inputPort);
                }
                me.hide();
            }
        });
    }

    private void initInputs() {
        deviceName.setText(device.getName());
        devicePort.setText(device.getPort());
        deviceIp.setText(device.getAddress());
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
