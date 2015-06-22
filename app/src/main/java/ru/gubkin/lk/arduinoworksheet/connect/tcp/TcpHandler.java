package ru.gubkin.lk.arduinoworksheet.connect.tcp;

import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.net.Socket;

import ru.gubkin.lk.arduinoworksheet.MainActivity;
import ru.gubkin.lk.arduinoworksheet.connect.ConnectionHandler;
import ru.gubkin.lk.arduinoworksheet.connect.MessageReceiver;
import ru.gubkin.lk.arduinoworksheet.connect.OutputScheduler;
import ru.gubkin.lk.arduinoworksheet.connect.MessageHandler;

/**
 * Created by root on 03.06.15.
 */
public class TcpHandler extends ConnectionHandler {

    private final String ip;
    private final int port;
    private Socket socket;

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
        if (msg.what != 0) {
            fallback(msg.what);
            return;
        }

        if (msg.obj != null) {
            socket = (Socket) msg.obj;
            try {
                receiveThread = new MessageReceiver(this, socket.getInputStream());
                outputThread = new OutputScheduler(this, socket.getOutputStream());
                receiveThread.start();
                outputThread.start();
                activity.startMainFragment();
            } catch (IOException e) {
                fallback(0);
            }
        }
    }

    @Override
    public void sendData(String data){
        outputThread.setSchedule(data);
    }

    @Override
    public void registerMessageHandler(MessageHandler handler) {
        receiveThread.registerHandler(handler);
    }
}
