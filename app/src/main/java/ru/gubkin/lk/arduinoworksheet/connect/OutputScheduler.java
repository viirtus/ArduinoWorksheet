package ru.gubkin.lk.arduinoworksheet.connect;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Андрей on 11.06.2015.
 */
public class OutputScheduler extends Thread {
    private final static String TAG = "OUTPUT_THREAD";

    private Handler handler;
    private OutputStream outputStream;
    private String schedule = "";

    public OutputScheduler (Handler handler, OutputStream outputStream) {
        this.handler = handler;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            if (!schedule.isEmpty()) {
                byte[] msgBuffer = schedule.getBytes();
                Log.d(TAG, "***Отправляем данные: " + schedule + "***");

                try {
                    outputStream.write(msgBuffer);
                    outputStream.flush();
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
}
