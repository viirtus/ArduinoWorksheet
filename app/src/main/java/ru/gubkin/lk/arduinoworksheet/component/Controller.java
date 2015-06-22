package ru.gubkin.lk.arduinoworksheet.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import java.util.Observable;
import java.util.Observer;

import ru.gubkin.lk.arduinoworksheet.MainActivity;
import ru.gubkin.lk.arduinoworksheet.util.Util;

/**
 * Created by root on 05.05.15.
 */
public abstract class Controller<T extends Observable> {
    private String title;
    protected Context context;
    protected GridView gridView;
    protected Button button;

    protected ComponentObserver observer;


    protected Controller(Context context, String title) {
        this.context = context;
        this.title = title;
        this.observer = new ComponentObserver(this);
    }

    /**
     * recount height of gridView
     * @param height of one element in dp
     * @param columns number of columns in grid
     * @param n number of elements
     */
    @SuppressWarnings("SameParameterValue")
    protected void initHeight(int height, int columns, int n) {
        gridView.getLayoutParams().height = (int) ((Math.ceil(n * 1.0 / columns)) * Util.convertDpToPixel(height, context) + Util.convertDpToPixel(5, context)) ;
    }

    /**
     * @return title of a controller
     */
    public String getTitle() {
        return title;
    }


    public void setGridView(GridView gridView) {
        this.gridView = gridView;
    }

    public void setAddButton(Button button) {
        this.button = button;
    }

    /**
     * @return grid adapter for gridView
     */
    public abstract BaseAdapter getGridAdapter();

    /**
     * Registering all necessary listeners to an UI components. Called when fragment fire onResume method
     */
    public abstract void registerListeners();

    /**
     * Unregister all listeners when fragment fire onPause method;
     */
    public abstract void unregisterListeners();


    /**
     * update all adapters
     */
    public abstract void notifyChange();

    /**
     * Update passed object
     * @param o components that needed for update
     */
    public abstract void updateComponent(T o);

    /**
     * Delete passed object
     * @param o object for delete
     */
    public abstract void deleteComponent(T o);

    /**
     * Invoke when necessary transfer data to Arduino
     * @param o
     */
    public abstract void processComponent(T o);
}
