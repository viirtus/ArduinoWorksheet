package ru.gubkin.lk.arduinoworksheet.component;

/**
 * Created by Андрей on 10.05.2015.
 */
public abstract class MessageListener {
    abstract String getStartPattern();
    abstract String getEndPattern();
    abstract void onReceiveMessage(String message);
}
