package ru.gubkin.lk.arduinoworksheet.bt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import ru.gubkin.lk.arduinoworksheet.MainActivity;

/**
 * Created by root on 06.05.15.
 */
public class BluetoothHandler extends Handler {
    private static final UUID _UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final static String TAG = "BT_HANDLER";
    private Context context;
    private BluetoothAdapter adapter;
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private BluetoothThread thread;
    private String lastConnect;
    public BluetoothHandler(Context context) {
        super(Looper.getMainLooper());
        this.context = context;
        adapter = BluetoothAdapter.getDefaultAdapter();
    }

    public boolean tryToConnect(String mac) throws IOException {
        if (checkBtState()) {
            device = adapter.getRemoteDevice(mac);
            socket = device.createRfcommSocketToServiceRecord(_UUID);

            adapter.cancelDiscovery();
            try {
                socket.connect();
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Log.e(TAG, "trying fallback...");

                    socket = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(device, 1);
                    socket.connect();

                    Log.e(TAG, "Connected");
                } catch (Exception e2) {
                    socket.close();
                    Log.e(TAG, "Couldn't establish Bluetooth connection!");
                    return false;
                }
            }
            thread = new BluetoothThread(socket, this);
            thread.start();
            lastConnect = mac;
            return true;
        }
        return false;
    }

    private boolean checkBtState() {
        if (adapter != null) {
            if (adapter.isEnabled()) {
                Log.i(TAG, "BT adapter is already enabled");

            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                ((MainActivity) context).startActivityForResult(enableBtIntent, 0);
            }
            return true;
        }
        Log.i(TAG, "BT adapter not exist");
        return false;
    }

    public static void startDiscovery() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            adapter.startDiscovery();
        }
    }

    @Override
    public void handleMessage(Message msg) {
        String read = (String) msg.obj;
        Log.i(TAG, "Данные от Arduino: " + read);
    };


    public void sendData(String message) throws IOException {
        if (thread != null) {
            thread.sendData(message);
        } else {
            tryToConnect(lastConnect);
        }
    }



}
