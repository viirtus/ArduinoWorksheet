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
    private ArrayList<ListItem> items;
    private ListView list;
    private DeviceController controller;
    private final BroadcastReceiver btReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                DeviceItem item = new DeviceItem(0, DeviceType.BLUETOOTH, device.getName(), device.getAddress());
                controller.add(item);
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
        list = (ListView) v.findViewById(R.id.bt_devices_lv);
        controller = new DeviceController(getActivity());

        list.setAdapter(controller.getAdapter());

        return v;
    }

    /*private ArrayList<ListItem> getDeviceList() {
        ArrayList<ListItem> items = new ArrayList<>();

    }*/

    @Override
    public void onResume() {
        super.onResume();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem item = items.get(position);
                if (item.getViewType() == ListItem.ItemType.DEVICE.ordinal()) {
                    ((MainActivity) getActivity()).tryToConnectBluetooth(item.getInfo());
                } else {
                    ((MainActivity) getActivity()).tryToConnectTcp(item.getInfo());
                }
//                getFragmentManager().beginTransaction().remove(me).commit();
            }
        });

        BluetoothHandler.startDiscovery((MainActivity) getActivity());
    }
    @Override
    public void onDestroy(){
        super.onDestroy();

        list.setOnItemClickListener(null);

        getActivity().unregisterReceiver(btReceiver);
    }

}
