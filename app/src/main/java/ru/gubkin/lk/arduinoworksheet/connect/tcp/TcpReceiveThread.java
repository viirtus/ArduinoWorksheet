package ru.gubkin.lk.arduinoworksheet.connect.tcp;

import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by root on 03.06.15.
 */
public class TcpReceiveThread extends Thread {
    private Handler handler;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private ArrayList<Handler> handlers;

    TcpReceiveThread(Socket socket) throws IOException {
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
        this.handlers = new ArrayList<>();
    }

    public void sendData(String data) {
        try {
            outputStream.writeUTF(data);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while (true) {
            try {
                while ((line = reader.readLine()) != null) {
                    Log.i("RECEIVED", line);
                    for (Handler handler : handlers) {
                        handler.obtainMessage(0, line).sendToTarget();
                    }
                }
            } catch (IOException e) {
                //invoke Activity
                break;
            }
        }
    }

    public void registerHandler(Handler handler) {
        handlers.add(handler);
    }
}
