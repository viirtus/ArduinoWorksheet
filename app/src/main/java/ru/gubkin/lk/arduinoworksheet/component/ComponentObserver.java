package ru.gubkin.lk.arduinoworksheet.component;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by ������ on 19.06.2015.
 */
public class ComponentObserver<T> implements Observer {

    public static final Integer PROCESS_KEY = 2;
    public static final Integer UPDATE_KEY = 4;
    public static final Integer DELETE_KEY = 8;
    public static final Integer NOTIFY_KEY = 16;

    private Controller<Observable> controller;

    public ComponentObserver(Controller<Observable> controller) {
        this.controller = controller;
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data != null) {
            Integer key = (Integer) data;
            if (key.equals(PROCESS_KEY)) {
                controller.processComponent(observable);
            }
            if (key.equals(UPDATE_KEY)) {
                controller.updateComponent(observable);
            }
            if (key.equals(DELETE_KEY)) {
                controller.deleteComponent(observable);
            }
            if (key.equals(NOTIFY_KEY)) {
                controller.notifyChange();
            }
        }
    }
}
