package ru.gubkin.lk.arduinoworksheet.connect;

/**
 * Created by Андрей on 10.05.2015.
 */
public interface MessageListener {
    String getStartPattern();
    String getEndPattern();
    void onReceiveMessage(String message);
}
