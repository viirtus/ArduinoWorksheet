package ru.gubkin.lk.arduinoworksheet.connect;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Андрей on 10.05.2015.
 */
public class MessageHandler extends Handler {
    String TAG = "MESSAGE_HANDLER";
    ArrayList<MessageListener> listeners;

    public MessageHandler () {
        listeners = new ArrayList<>();
    }

    public void registerListeners (MessageListener listener) {
        listeners.add(listener);
    }

    @Override
    public void handleMessage(Message msg) {
        String read = (String) msg.obj;
        for (MessageListener listener: listeners) {
//            Log.i(TAG, "Данные от Arduino: " + read);
            String value = findValue(read, listener);
//            Log.i(TAG, "Найдено в строке по правилам: " + value);
            listener.onReceiveMessage(value);
        }
    }

    private String findValue(String line, MessageListener sensor) {

        String startWith = sensor.getStartPattern();
        String endWith = sensor.getEndPattern();
        String pattern_ = startWith + "(.*?)" + endWith;
        Pattern pattern = Pattern.compile(pattern_);
        Matcher matcher = pattern.matcher(line);
        String out = "";
        while(matcher.find()) {
            out = matcher.group(1);
            if (out.isEmpty()) {
                int end = matcher.end();
                out = line.substring(end);

            }
        }
        if (out.isEmpty()) {
            return line;
        }
//        String pattern = Pattern.quote(pattern1)
        return out;
    }
}
