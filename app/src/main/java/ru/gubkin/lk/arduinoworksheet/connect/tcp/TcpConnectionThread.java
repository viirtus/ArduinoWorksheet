package ru.gubkin.lk.arduinoworksheet.connect.tcp;

import android.os.Handler;

import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by root on 03.06.15.
 */
public class TcpConnectionThread extends Thread {
    private final String ip;
    private final int port;
    private Handler handler;

    protected TcpConnectionThread(String ip, int port, Handler handler) {
        this.ip = ip;
        this.port = port;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            InetAddress inetAddress = InetAddress.getByName(ip);
            Socket socket = new Socket(inetAddress, port);
            handler.obtainMessage(0, socket).sendToTarget();
        } catch (Exception e) {
            handler.obtainMessage(0, null).sendToTarget();
        }
    }
}
