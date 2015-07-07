package ru.gubkin.lk.arduinoworksheet.component.terminal;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.widget.TextView;

import java.util.Observable;

/**
 * Created by root on 29.06.15.
 */
public class Terminal extends Observable {
    private static final int MAX_LINES = 10;
    private String text = "";
    private TextView view;

    public void setView(TextView view) {
        this.view = view;
        view.setText(text);
    }

    public void postMessage(String message) {
//        prepareText(message);
        view.append(message + "\n");
    }
    private void prepareText(String message) {
        String[] lines = text.split("\n");
        for (int i = 0; i < lines.length && i < MAX_LINES; i++) {
            text += lines[i] + "\n";
        }
        text += message + "\n";
    }
}
