package ru.gubkin.lk.arduinoworksheet.component.sensor;

import java.util.Observable;

import ru.gubkin.lk.arduinoworksheet.component.servo.RadialScaleView;
import ru.gubkin.lk.arduinoworksheet.util.MessageListener;

/**
 * Created by root on 11.05.15.
 */
public class Sensor extends Observable implements MessageListener {
    protected static final Integer DELETE_KEY = -1;
    protected static final Integer UPDATE_KEY = -2;

    private String name;
    private String startPattern;
    private String endPattern;
    private int id;
    private float maxValue;
    private float minValue;
    private float value;
    private RadialScaleView sensorView;

    public Sensor(String name, String startPattern, String endPattern, int id, float maxValue, float minValue){
        this.name = name;
        this.startPattern = startPattern;
        this.endPattern = endPattern;
        this.id = id;

        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setChanged();
        notifyObservers(UPDATE_KEY);
    }

    public String getStartPattern() {
//        return "Value of potentiometer: ";
        return startPattern;
    }

    public void setStartPattern(String startPattern) {
        this.startPattern = startPattern;
        setChanged();
        notifyObservers(UPDATE_KEY);
    }

    public String getEndPattern() {
        return endPattern;
    }

    @Override
    public void onReceiveMessage(String message) {
        if (sensorView != null) {
            try {
                float value = Float.parseFloat(message);

                this.value = value;
                if (value > maxValue) {
                    value = maxValue;
                }
                sensorView.setValue(value);
                sensorView.invalidate();
            } catch (NumberFormatException ignored) {

            }
        }
    }

    public void setEndPattern(String endPattern) {
        this.endPattern = endPattern;
        setChanged();
        notifyObservers(UPDATE_KEY);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
        setChanged();
        notifyObservers(UPDATE_KEY);
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setSensorView(RadialScaleView sensorView) {
        this.sensorView = sensorView;
        sensorView.setName(name);
        sensorView.setMaxValue(maxValue);
        sensorView.setValue(value);
        sensorView.invalidate();
    }

    public void destroy() {
        setChanged();
        notifyObservers(DELETE_KEY);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        Sensor s = (Sensor) o;
        return s.id == id;
    }
}
