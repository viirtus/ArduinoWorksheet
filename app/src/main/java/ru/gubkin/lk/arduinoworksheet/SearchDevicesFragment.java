package ru.gubkin.lk.arduinoworksheet;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
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
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.adapter.DevicesAdapter;
import ru.gubkin.lk.arduinoworksheet.bt.BluetoothHandler;

/**
 * Created by Андрей on 07.05.2015.
 */
public class SearchDevicesFragment extends Fragment {
    private DevicesAdapter btDeviceAdapter;

    private final BroadcastReceiver btReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                btDeviceAdapter.add(device);
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
        final Fragment me = this;
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        ListView list = (ListView) v.findViewById(R.id.bt_devices_lv);
        btDeviceAdapter = new DevicesAdapter(getActivity(), new ArrayList<BluetoothDevice>());
        list.setAdapter(btDeviceAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity) getActivity()).attachFragmentForDevice((BluetoothDevice)btDeviceAdapter.getItem(position));
//                getFragmentManager().beginTransaction().remove(me).commit();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        BluetoothHandler.startDiscovery();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        getActivity().unregisterReceiver(btReceiver);
    }

}
