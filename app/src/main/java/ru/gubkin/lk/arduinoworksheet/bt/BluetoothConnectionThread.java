package ru.gubkin.lk.arduinoworksheet.bt;

import android.os.Handler;

/**
 * Created by Андрей on 07.05.2015.
 */
public class BluetoothConnectionThread extends Thread {
    private Handler handler;
    public BluetoothConnectionThread (Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {

    }
}
