package ru.gubkin.lk.arduinoworksheet.bt;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by root on 06.05.15.
 */
public class BluetoothThread extends Thread {
    private final static String TAG = "BT_THREAD";
    private final static String END_OF_LINE = "\n";
    private final BluetoothSocket bluetoothSocket;
    private final OutputStream outputStream;
    private final InputStream inputStream;
    private final Handler handler;

    protected BluetoothThread(BluetoothSocket copyBtSocket, Handler handler) throws IOException {
        this.bluetoothSocket = copyBtSocket;
        outputStream = copyBtSocket.getOutputStream();
        inputStream = copyBtSocket.getInputStream();
        this.handler = handler;
    }

    protected void sendData(String message) throws IOException {

        byte[] msgBuffer = message.getBytes();
        Log.d(TAG, "***Отправляем данные: " + message + "***");

        outputStream.write(msgBuffer);
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
                            final String data = new String(encodedBytes, "US-ASCII");
                            readBufferPosition = 0;
                            handler.obtainMessage(0, data).sendToTarget();
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
}
