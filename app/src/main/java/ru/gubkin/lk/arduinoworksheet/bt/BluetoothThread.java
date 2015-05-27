package ru.gubkin.lk.arduinoworksheet.bt;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by root on 06.05.15.
 */
public class BluetoothThread extends Thread {
    private final static String TAG = "BT_THREAD";
    private final static String END_OF_LINE = "\n";
    private final OutputStream outputStream;
    private final InputStream inputStream;
    private ArrayList<Handler> handlers;


    protected BluetoothThread(BluetoothSocket copyBtSocket) throws IOException {
        outputStream = copyBtSocket.getOutputStream();
        inputStream = copyBtSocket.getInputStream();
        handlers = new ArrayList<>();
    }

    protected void sendData(String message) throws IOException {

        byte[] msgBuffer = message.getBytes();
        Log.d(TAG, "***Отправляем данные: " + message + "***");

        outputStream.write(msgBuffer);
        outputStream.flush();
    }

    @Override
    public void run() {

        final byte delimiter = 10; //This is the ASCII code for a newline character
        int readBufferPosition = 0;
        byte[] buffer = new byte[1024];

        while (true) {
            try {
                int bytesAvailable = inputStream.available();
                if (bytesAvailable > 0) {
                    byte[] packetBytes = new byte[bytesAvailable];
                    inputStream.read(packetBytes);
                    for (int i = 0; i < bytesAvailable; i++) {
                        byte b = packetBytes[i];
                        if (b == delimiter) {
                            byte[] encodedBytes = new byte[readBufferPosition];
                            System.arraycopy(buffer, 0, encodedBytes, 0, encodedBytes.length);
                            String data = new String(encodedBytes, "US-ASCII");
                            data = data.replaceAll("\r", "");
                            readBufferPosition = 0;
                            for (Handler handler : handlers) {
                                handler.obtainMessage(0, data).sendToTarget();
                            }
                        } else {
                            buffer[readBufferPosition++] = b;
                        }
                    }
                }
            } catch (IOException e) {
                String message = e.getMessage();
                Log.i(TAG, message);
                break;
            }
            /*try {
                bytes = inputStream.read(buffer);

            } catch (IOException e) {
                String message = e.getMessage();
                Log.i(TAG, message);
                break;
            }*/

        }

    }

    public void registerHandler(Handler handler) {
        handlers.add(handler);
    }
}
