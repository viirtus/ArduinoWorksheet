package ru.gubkin.lk.arduinoworksheet.connect;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import ru.gubkin.lk.arduinoworksheet.MainActivity;
import ru.gubkin.lk.arduinoworksheet.util.MessageHandler;

/**
 * Created by ������ on 30.05.2015.
 */
public abstract class ConnectionHandler extends Handler {

    protected final MainActivity activity;

    protected ConnectionHandler(Looper looper, MainActivity activity) {
        super(looper);
        this.activity = activity;
    }

    /**
     * Method use for sending string data to connected device
     * @param data to be send
     */
    public abstract void sendData(String data) throws IOException;

    /**
     * Registering message handler which can receive some data.
     * @param handler new handler
     */
    public abstract void registerMessageHandler(MessageHandler handler);

    public abstract void connectRequest();

}
