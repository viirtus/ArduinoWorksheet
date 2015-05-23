package ru.gubkin.lk.arduinoworksheet.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import ru.gubkin.lk.arduinoworksheet.MainActivity;
import ru.gubkin.lk.arduinoworksheet.util.Util;

/**
 * Created by root on 05.05.15.
 */
public abstract class Controller {
    protected Context context;
    protected View wrapper;
    protected boolean expanded = false;

    public Controller(Context context) {
        this.context = context;
    }

    public abstract void registerListeners();
    public abstract View getViewItem(LayoutInflater inflater, View convertView, ViewGroup parent);

    public abstract void notifyChange();
    public int getFrameHeight() {
        return Util.getActivityHeight((MainActivity) context) - (int) Util.convertDpToPixel(50, context);
    }


}
