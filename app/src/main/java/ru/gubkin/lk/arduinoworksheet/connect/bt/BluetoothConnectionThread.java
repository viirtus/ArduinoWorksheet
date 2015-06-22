package ru.gubkin.lk.arduinoworksheet.connect.bt;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import ru.gubkin.lk.arduinoworksheet.connect.ConnectionHandler;

/**
 * Created by Андрей on 07.05.2015.
 */
class BluetoothConnectionThread extends Thread {
    private static final UUID _UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String TAG = "BT_CONNECTION";

    private Handler handler;
    private BluetoothDevice device;
    private BluetoothSocket socket;

    public BluetoothConnectionThread(Handler handler, BluetoothDevice device) {
        this.handler = handler;
        this.device = device;

        BluetoothSocket tmp = null;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(_UUID);
        } catch (IOException ignored) {
        }
        socket = tmp;

    }

    @Override
    public void run() {
        if (!interrupted()) {
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
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    Log.e(TAG, "Couldn't establish Bluetooth connection!");
                }
            }
            if (socket.isConnected()) {
                handler.obtainMessage(0, socket).sendToTarget();
            } else {
                handler.obtainMessage(ConnectionHandler.CONNECTION_EXCEPTION).sendToTarget();
            }
        }
    }

    public void nullHandler() {
        handler = null;
    }
}
