package ru.gubkin.lk.arduinoworksheet;

import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.shamanland.fab.FloatingActionButton;

import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.component.list.device.DeviceController;
import ru.gubkin.lk.arduinoworksheet.component.list.device.DeviceItem;
import ru.gubkin.lk.arduinoworksheet.component.list.device.DeviceType;
import ru.gubkin.lk.arduinoworksheet.component.list.device.ListItem;
import ru.gubkin.lk.arduinoworksheet.connect.bt.BluetoothHandler;

/**
 * Created by Андрей on 07.05.2015.
 */
public class SearchDevicesFragment extends Fragment {
    private DeviceController controller;
    private FloatingActionButton button;
    private final BroadcastReceiver btReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                DeviceItem item = new DeviceItem(DeviceItem.DEFAULT_ID, DeviceType.BLUETOOTH, device.getName(), device.getAddress());
                controller.add(item, false);
            }
        }
    };

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        IntentFilter intent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(btReceiver, intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        ListView list = (ListView) v.findViewById(R.id.bt_devices_lv);
        controller = new DeviceController(getActivity());
        controller.setList(list);
        button = (FloatingActionButton)(getActivity()).findViewById(R.id.add_f_button);
        return v;
    }
    

    @Override
    public void onResume() {
        super.onResume();
        controller.registerListeners();
        BluetoothHandler.startDiscovery((MainActivity) getActivity());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceItem item = new DeviceItem(DeviceItem.DEFAULT_ID, DeviceType.TCP, "Новое устройство", "192.168.0.2", "2501");
                controller.add(item, true);
                controller.updateDialog(item);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (controller != null) {
            controller.unregisterListeners();
        }

        getActivity().unregisterReceiver(btReceiver);
    }

}
