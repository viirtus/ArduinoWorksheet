package ru.gubkin.lk.arduinoworksheet.component.sensor;

import java.util.Observable;
import java.util.Observer;

import ru.gubkin.lk.arduinoworksheet.db.SensorDBHandler;

/**
 * Created by root on 12.05.15.
 */
public class SensorObserver implements Observer {

    private SensorDBHandler dbHandler;
    private SensorController controller;

    public SensorObserver (SensorDBHandler dbHandler, SensorController controller) {
        this.dbHandler = dbHandler;
        this.controller = controller;
    }

    @Override
    public void update(Observable observable, Object data) {
        Sensor sensor = (Sensor) observable;
        Integer key = (Integer) data;
        if (key.equals (Sensor.UPDATE_KEY)) {
            dbHandler.updateSensor(sensor);
            controller.notifyChange();
        }
        if (key.equals(Sensor.DELETE_KEY)) {
            dbHandler.deleteSensor(sensor);
            controller.deleteSensor(sensor);
            controller.notifyChange();
        }
    }
}
