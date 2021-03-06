package ru.gubkin.lk.arduinoworksheet.component.servo;

import android.widget.Button;

import java.util.Observable;

import ru.gubkin.lk.arduinoworksheet.component.ComponentObserver;

/**
 * Created by Андрей on 08.05.2015.
 */
public class Servo extends Observable {

    /**
     * id of row record in db
     */
    private int id;

    /**
     * id of row record in device table
     */
    private int deviceId;


    private int delay;

    private String name;

    private String command;
    private int maxValue;
    private int value;
    private RadialScaleView view;
    private Button btnUp;
    private Button btnDwn;

    public Servo(int id, int deviceId, String name, String command, int maxValue, int value, int delay) {
        this.id = id;
        this.deviceId = deviceId;
        this.name = name;
        this.command = command;
        this.maxValue = maxValue;
        this.value = value;
        this.delay = delay;
    }

    public RadialScaleView getView() {
        return view;
    }

    public void setServoView(final RadialScaleView view) {
        this.view = view;
        view.setMaxValue(maxValue);
        view.setValue(value);
        view.setName(name);
        view.invalidate();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
        setChanged();
        notifyObservers(ComponentObserver.UPDATE_KEY);
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        setChanged();
        notifyObservers(ComponentObserver.UPDATE_KEY);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {

        this.value = value;
        setChanged();
        notifyObservers(ComponentObserver.PROCESS_KEY);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setChanged();
        notifyObservers(ComponentObserver.UPDATE_KEY);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
        setChanged();
        notifyObservers(ComponentObserver.UPDATE_KEY);
    }

    public void unregisterListeners() {
        btnDwn.setOnClickListener(null);
        btnUp.setOnClickListener(null);
    }

    public void destroy() {
        setChanged();
        notifyObservers(ComponentObserver.DELETE_KEY);
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Servo servo = (Servo) o;
        return servo.id == this.id;
    }
}
