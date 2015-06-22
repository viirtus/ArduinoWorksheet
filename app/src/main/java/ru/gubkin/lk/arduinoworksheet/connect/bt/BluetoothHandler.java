package ru.gubkin.lk.arduinoworksheet.connect.bt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import ru.gubkin.lk.arduinoworksheet.MainActivity;
import ru.gubkin.lk.arduinoworksheet.connect.ConnectionHandler;
import ru.gubkin.lk.arduinoworksheet.connect.MessageReceiver;
import ru.gubkin.lk.arduinoworksheet.connect.OutputScheduler;
import ru.gubkin.lk.arduinoworksheet.connect.MessageHandler;

/**
 * Created by root on 06.05.15.
 */
public class BluetoothHandler extends ConnectionHandler {
    private final static String TAG = "BT_HANDLER";
    private final static int REQUEST_ENABLE_BT = 0;
    private String mac;
    private BluetoothAdapter adapter;
    private BluetoothConnectionThread threadConnection;
    private String lastConnect;


    public BluetoothHandler(MainActivity context, String mac) {
        super(Looper.getMainLooper(), context);
        this.mac = mac;
        adapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static void startDiscovery(MainActivity activity) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            adapter.startDiscovery();
            if (!adapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    @Override
    public void connectRequest() {
        if (checkBtState()) {
            BluetoothDevice device = adapter.getRemoteDevice(mac);
            adapter.cancelDiscovery();
            threadConnection = new BluetoothConnectionThread(this, device);
            threadConnection.start();
            lastConnect = mac;
        }

    }

    private boolean checkBtState() {
        if (adapter != null) {
            if (adapter.isEnabled()) {
                Log.i(TAG, "BT adapter is already enabled");

            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(enableBtIntent, 0);
            }
            return true;
        }
        Log.i(TAG, "BT adapter not exist");
        return false;
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what != 0) {
            fallback(msg.what);
            return;
        }
        if (msg.obj != null) {
            BluetoothSocket socket = (BluetoothSocket) msg.obj;
            try {
                receiveThread = new MessageReceiver(this, socket.getInputStream());
                outputThread = new OutputScheduler(this, socket.getOutputStream());
                receiveThread.start();
                outputThread.start();

                activity.startMainFragment();
                threadConnection.interrupt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void registerMessageHandler(MessageHandler handler) {
        receiveThread.registerHandler(handler);
    }

    @Override
    public void sendData(String message){
        outputThread.setSchedule(message);
    }



}
