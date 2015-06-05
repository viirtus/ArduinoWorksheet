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

import ru.gubkin.lk.arduinoworksheet.adapter.DevicesAdapter;
import ru.gubkin.lk.arduinoworksheet.component.ListItem;
import ru.gubkin.lk.arduinoworksheet.component.list.device.DeviceListItem;
import ru.gubkin.lk.arduinoworksheet.component.list.device.HeaderListItem;
import ru.gubkin.lk.arduinoworksheet.connect.bt.BluetoothHandler;

/**
 * Created by Андрей on 07.05.2015.
 */
public class SearchDevicesFragment extends Fragment {
    private DevicesAdapter btDeviceAdapter;
    private ArrayList<ListItem> items;
    private final BroadcastReceiver btReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                DeviceListItem listItem = new DeviceListItem(device.getName(), device.getAddress(), ListItem.ItemType.ITEM_BLUETOOTH);
                items.add(1, listItem);
                btDeviceAdapter.notifyDataSetChanged();
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
        items = new ArrayList<>();
        items.add(new HeaderListItem("Bluetooth"));
        items.add(new HeaderListItem("TCP/IP"));
        items.add(new DeviceListItem("Arduino home", "169.254.75.8:2501", ListItem.ItemType.ITEM_TCP));
        btDeviceAdapter = new DevicesAdapter(getActivity(), items);
        list.setAdapter(btDeviceAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem item = items.get(position);
                if (item.getViewType() == ListItem.ItemType.ITEM_BLUETOOTH.ordinal()) {
                    ((MainActivity) getActivity()).tryToConnectBluetooth(item.getInfo());
                } else {
                    ((MainActivity) getActivity()).tryToConnectTcp(item.getInfo());
                }
//                getFragmentManager().beginTransaction().remove(me).commit();
            }
        });
        return v;
    }

    /*private ArrayList<ListItem> getDeviceList() {
        ArrayList<ListItem> items = new ArrayList<>();

    }*/

    @Override
    public void onResume() {
        super.onResume();

        BluetoothHandler.startDiscovery((MainActivity) getActivity());
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        getActivity().unregisterReceiver(btReceiver);
    }

}
