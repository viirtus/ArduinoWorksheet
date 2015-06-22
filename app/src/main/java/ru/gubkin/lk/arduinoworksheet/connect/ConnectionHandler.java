package ru.gubkin.lk.arduinoworksheet.connect;

import android.os.Handler;
import android.os.Looper;

import ru.gubkin.lk.arduinoworksheet.MainActivity;

public abstract class ConnectionHandler extends Handler {
    public final static int OUTPUT_EXCEPTION = -512;
    public final static int INPUT_EXCEPTION = -1024;
    public final static int CONNECTION_EXCEPTION = -2048;

    protected MessageReceiver receiveThread;
    protected OutputScheduler outputThread;

    protected final MainActivity activity;

    protected ConnectionHandler(Looper looper, MainActivity activity) {
        super(looper);
        this.activity = activity;
    }

    /**
     * Method use for sending string data to connected device
     * @param data to be send
     */
    public abstract void sendData(String data);

    /**
     * Registering message handler which can receive some data.
     * @param handler new handler
     */
    public abstract void registerMessageHandler(MessageHandler handler);

    /**
     * Creating a connection
     */
    public abstract void connectRequest();

    /**
     * fallback in case of any exception
     * @param what exception code
     */
    protected void fallback(int what) {
        outputThread.interrupt();
        receiveThread.interrupt();

        String message;
        switch (what) {
            case CONNECTION_EXCEPTION:
                message= "Ошибка соединения. Попробуйте еще раз.";
                break;
            case INPUT_EXCEPTION:
                message = "Ошибка при чтении входящего потока.";
                break;
            case OUTPUT_EXCEPTION:
                message = "Ошибка при отравке сообщения.";
                break;
            default:
                message = "Неизвестная ошибка.";
        }
        activity.connectionFallback(message);
    }

}
