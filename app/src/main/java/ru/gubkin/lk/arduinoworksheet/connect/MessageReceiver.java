package ru.gubkin.lk.arduinoworksheet.connect;

import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Андрей on 11.06.2015.
 */
public class MessageReceiver extends Thread {
    private final static String TAG = "RECEIVER_THREAD";
    private ArrayList<Handler> handlers;
    private final Handler handler;
    private final InputStream inputStream;

    public MessageReceiver (Handler handler, InputStream inputStream) {
        this.handler = handler;
        this.inputStream = inputStream;
        handlers = new ArrayList<>();
    }

    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while (!isInterrupted()) {
            try {
                while ((line = reader.readLine()) != null) {
                    Log.i(TAG, line);
                    for (Handler handler : handlers) {
                        handler.obtainMessage(0, line).sendToTarget();
                    }
                }
            } catch (IOException e) {
                handler.obtainMessage(ConnectionHandler.INPUT_EXCEPTION).sendToTarget();
                break;
            }
        }
    }

    public void registerHandler(Handler handler) {
        handlers.add(handler);
    }

}
