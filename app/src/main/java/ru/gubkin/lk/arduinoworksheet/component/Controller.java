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

    protected Controller(Context context) {
        this.context = context;
    }

    /**
     * Registering all necessary listeners to an UI components. Called when fragment fire onResume method
     */
    public abstract void registerListeners();

    /**
     * Unregister all listeners when fragment fire onPause method;
     */
    public abstract void unregisterListeners();


    /**
     * Getting a View for inflating to the component list
     * @param inflater used for inflate
     * @param convertView for recycling
     * @param parent parent
     * @return View with components inside
     */
    public abstract View getViewItem(LayoutInflater inflater, View convertView, ViewGroup parent);

    public abstract void notifyChange();


}
