package ru.gubkin.lk.arduinoworksheet.connect.tcp;

import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.net.Socket;

import ru.gubkin.lk.arduinoworksheet.MainActivity;
import ru.gubkin.lk.arduinoworksheet.connect.ConnectionHandler;
import ru.gubkin.lk.arduinoworksheet.util.MessageHandler;

/**
 * Created by root on 03.06.15.
 */
public class TcpHandler extends ConnectionHandler {

    private final String ip;
    private final int port;
    private Socket socket;
    private TcpReceiveThread receiveThread;

    public TcpHandler(MainActivity activity, String ip, int port) {
        super(Looper.getMainLooper(), activity);
        this.ip = ip;
        this.port = port;
    }

    public void connectRequest() {
        new TcpConnectionThread(ip, port, this).start();
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.obj != null) {
            socket = (Socket) msg.obj;
            try {
                receiveThread = new TcpReceiveThread(socket);
                receiveThread.start();
                activity.startMainFragment();
            } catch (IOException e) {
                activity.connectFallback();
            }
        }
    }

    @Override
    public void sendData(String data) throws IOException {
        receiveThread.sendData(data);
    }

    @Override
    public void registerMessageHandler(MessageHandler handler) {
        receiveThread.registerHandler(handler);
    }
}
