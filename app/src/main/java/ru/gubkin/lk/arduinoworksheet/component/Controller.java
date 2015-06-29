package ru.gubkin.lk.arduinoworksheet.component;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;

import java.util.Observable;

import ru.gubkin.lk.arduinoworksheet.util.Util;

/**
 * Created by root on 05.05.15.
 */
public abstract class Controller<T extends Observable> {
    protected int deviceId;
    protected Context context;
    protected GridView gridView;
    protected RelativeLayout emptyHolder;
    protected Button button;
    protected ComponentObserver observer;
    private String title;


    protected Controller(Context context, String title, int deviceId) {
        this.context = context;
        this.title = title;
        this.deviceId = deviceId;
        this.observer = new ComponentObserver(this);
    }

    /**
     * recount height of gridView
     * @param height of one element in dp
     * @param columns number of columns in grid
     * @param n number of elements
     */
    @SuppressWarnings("SameParameterValue")
    protected void validateView(int height, int columns, int n) {
        if (n == 0) {
            emptyHolder.setVisibility(View.VISIBLE);
            emptyHolder.getLayoutParams().height = (int) (Util.convertDpToPixel(120, context));
        } else {
            emptyHolder.setVisibility(View.GONE);
            gridView.getLayoutParams().height = (int) ((Math.ceil(n * 1.0 / columns)) * Util.convertDpToPixel(height, context) + Util.convertDpToPixel(5, context));
        }
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

    public void setEmptyHolder(RelativeLayout emptyMessage) {
        this.emptyHolder = emptyMessage;
    }

    public abstract boolean isEmpty();

    /**
     * @return grid adapter for gridView
     */
    public abstract BaseAdapter getAdapter();

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
