package ru.gubkin.lk.arduinoworksheet.component;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Андрей on 10.05.2015.
 */
public class MessageHandler extends Handler {
    String TAG = "MESSAGE_HANDLER";
    ArrayList<MessageListener> listeners;

    public MessageHandler () {
        listeners = new ArrayList<>();
    }

    public void registerListeners (MessageListener listener) {
        listeners.add(listener);
    }

    @Override
    public void handleMessage(Message msg) {
        String read = (String) msg.obj;
        for (MessageListener listener: listeners) {
            Log.i(TAG, "Данные от Arduino: " + read);
        }
    };
}
