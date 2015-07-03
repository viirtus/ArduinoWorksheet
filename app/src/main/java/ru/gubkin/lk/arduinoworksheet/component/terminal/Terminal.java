package ru.gubkin.lk.arduinoworksheet.component.terminal;

import android.widget.TextView;

import java.util.Observable;

/**
 * Created by root on 29.06.15.
 */
public class Terminal extends Observable {
    private String text;
    private TextView view;

    public void setView(TextView view) {
        this.view = view;
    }

    public void postMessage(String message) {
        view.append("\n" + message);
    }
}
