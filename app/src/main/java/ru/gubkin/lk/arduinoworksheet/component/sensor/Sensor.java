package ru.gubkin.lk.arduinoworksheet.component.sensor;

import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Observable;

import ru.gubkin.lk.arduinoworksheet.component.ComponentObserver;
import ru.gubkin.lk.arduinoworksheet.component.servo.RadialScaleView;
import ru.gubkin.lk.arduinoworksheet.connect.MessageListener;
import ru.gubkin.lk.arduinoworksheet.util.Util;

/**
 * Created by root on 11.05.15.
 */
public class Sensor extends Observable implements MessageListener {

    private String name;
    private String startPattern;
    private String endPattern;
    /**
     * id of row record in db
     */
    private int id;

    /**
     * id of row record in device table
     */
    private int deviceId;

    private float maxValue;
    private float minValue;
    private float value;
    private SensorDisplayView sensorView;
    private RadialScaleView sensorRadialView;
    private TextView tv;
    private RelativeLayout layout;

    public Sensor(int id, int deviceId, String name, String startPattern, String endPattern, float maxValue, float minValue) {
        this.deviceId = deviceId;
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
        notifyObservers(ComponentObserver.UPDATE_KEY);
    }

    public String getStartPattern() {
        return startPattern;
    }

    public void setStartPattern(String startPattern) {
        this.startPattern = startPattern;
        setChanged();
        notifyObservers(ComponentObserver.UPDATE_KEY);
    }

    public String getEndPattern() {
        return endPattern;
    }

    public void setEndPattern(String endPattern) {
        this.endPattern = endPattern;
        setChanged();
        notifyObservers(ComponentObserver.UPDATE_KEY);
    }

    @Override
    public void onReceiveMessage(String message) {
        try {
            float value = Float.parseFloat(message);
            int color = Util.valueToColor(value, minValue, maxValue);

            this.value = value;
            if (value > maxValue) {
                value = maxValue;
            }
            if (value < minValue) {
                value = minValue;
            }
            if (sensorView != null) {
                sensorView.setValue(value);
                sensorView.invalidate();
            }

            if (sensorRadialView != null) {
                sensorRadialView.setValue(value);
                sensorRadialView.setArrowColor(color);
                sensorRadialView.invalidate();
            }

            if (tv != null) {
                tv.setText(String.valueOf(value));
            }
            validateLayout();
        } catch (NumberFormatException ignored) {

        }
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
        notifyObservers(ComponentObserver.UPDATE_KEY);
        validateLayout();
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

    public void setSensorView(SensorDisplayView sensorView) {
        this.sensorView = sensorView;
        sensorView.setMaxValue(maxValue);
        sensorView.setMinValue(minValue);
        sensorView.setValue(value);
        sensorView.invalidate();
    }

    public void setTextView(TextView tv) {
        this.tv = tv;

    }

    private void validateLayout() {

        int darkColor = Util.valueToDarkColor(value, minValue, maxValue);
        if (layout != null)
            layout.setBackgroundColor(darkColor);
    }

    public void setLayout(RelativeLayout layout) {
        this.layout = layout;
        validateLayout();
    }

    public void setSensorRadialView(RadialScaleView sensorRadialView) {
        this.sensorRadialView = sensorRadialView;
        int color = Util.valueToColor(value, minValue, maxValue);
        sensorRadialView.setMaxValue(maxValue);
        sensorRadialView.setMinValue(minValue);
        sensorRadialView.setValue(value);
        sensorRadialView.setName(name);
        sensorRadialView.setArrowColor(color);
        sensorRadialView.invalidate();
    }

    public void destroy() {
        setChanged();
        notifyObservers(ComponentObserver.DELETE_KEY);
    }

    public int getDeviceId() {
        return deviceId;
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        Sensor s = (Sensor) o;
        return s.id == id;
    }
}
