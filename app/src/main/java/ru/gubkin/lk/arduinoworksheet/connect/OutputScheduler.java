package ru.gubkin.lk.arduinoworksheet.connect;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by ROOOT on 11.06.2015.
 */
public class OutputScheduler extends Thread {
    private final static String TAG = "OUTPUT_THREAD";

    private Handler handler;
    private OutputStream outputStream;
    private String schedule = "";
    private OutputListener listener;

    public OutputScheduler (Handler handler, OutputStream outputStream) {
        this.handler = handler;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            if (!schedule.isEmpty()) {
                byte[] msgBuffer = schedule.getBytes();
                Log.d(TAG, "***SENDING: " + schedule + "***");

                try {
                    outputStream.write(msgBuffer);
                    outputStream.flush();
                    if (listener != null) {
                        listener.onOutputMessage(schedule);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    fallback();
                }
                schedule = "";
            }
        }
    }

    public void fallback() {
        handler.obtainMessage(ConnectionHandler.OUTPUT_EXCEPTION).sendToTarget();
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public void setListener(OutputListener listener) {

        this.listener = listener;
    }
}
