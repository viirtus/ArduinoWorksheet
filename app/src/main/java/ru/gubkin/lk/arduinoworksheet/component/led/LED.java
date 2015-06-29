package ru.gubkin.lk.arduinoworksheet.component.led;

import java.util.Observable;

import ru.gubkin.lk.arduinoworksheet.R;
import ru.gubkin.lk.arduinoworksheet.component.ComponentObserver;
import ru.gubkin.lk.arduinoworksheet.connect.MessageListener;

/**
 * Created by Андрей on 01.05.2015.
 */
public class LED extends Observable implements MessageListener {

    private LedColors color;

    /**
     * id of row record in db
     */
    private int id;

    /**
     * id of row record in device table
     */
    private int deviceId;

    private boolean isActive;

    private String title;

    private String commandOn;
    private String commandOff;

    LED(int id, int deviceId, LedColors c, boolean active, String title, String commandOn, String commandOff) {
        this.deviceId = deviceId;
        this.color = c;
        this.isActive = active;
        this.title = title;
        this.id = id;
        this.commandOn = commandOn;
        this.commandOff = commandOff;
    }

    public int getImageResource() {
        if (isActive)  return color.getRes();
        else return LedColors.OFF.getRes();
    }

    public void toggleState(boolean forceToggle) {
        isActive = !isActive;
        setChanged();
        if (forceToggle) {
            notifyObservers(ComponentObserver.PROCESS_KEY);
        } else {
            notifyObservers(ComponentObserver.UPDATE_KEY);
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public LedColors getColor() {
        return color;
    }

    private void setColor(LedColors color) {
        this.color = color;
        setChanged();
        notifyObservers(ComponentObserver.UPDATE_KEY);
    }

    public void setColor(int id) {
        setColor(LEDFactory.idToEnumColor(id));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        setChanged();
        notifyObservers(ComponentObserver.UPDATE_KEY);
    }

    public String getCommandOn() {
        return commandOn;
    }

    public void setCommandOn(String commandOn) {
        this.commandOn = commandOn;
        setChanged();
        notifyObservers(ComponentObserver.UPDATE_KEY);
    }

    public String getCommandOff() {
        return commandOff;
    }

    public void setCommandOff(String commandOff) {
        this.commandOff = commandOff;
        setChanged();
        notifyObservers(ComponentObserver.UPDATE_KEY);
    }

    public void destroy() {
        setChanged();
        notifyObservers(ComponentObserver.DELETE_KEY);
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

    public int getBackground() {
        if (isActive) {
            return color.getColor();
        }
        return 0xff546e7a;
    }

    @Override
    public String getStartPattern() {
        return "";
    }

    @Override
    public String getEndPattern() {
        return "";
    }

    @Override
    public void onReceiveMessage(String message) {

        if (message.equals(commandOn)) {
            isActive = true;
            setChanged();

            //We need just adapter refresh
            notifyObservers(ComponentObserver.NOTIFY_KEY);
        }
        if (message.equals(commandOff)) {
            isActive = false;
            setChanged();
            notifyObservers(ComponentObserver.NOTIFY_KEY);
        }
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        LED led = (LED) o;

        return id == led.id;
    }

    public enum LedColors {
        RED(0, 0xffB71C1C, R.drawable.red_led),
        GREEN(1, 0xff00695C, R.drawable.green_led),
        BLUE(2, 0xff0277BD, R.drawable.blue_led),
        OFF(3, 0, R.drawable.white_led);


        private final int color;
        private final int id;
        private final int res;

        LedColors(int id, int color, int res) {
            this.id = id;
            this.color = color;
            this.res = res;
        }

        public int getColorId() {
            return id;
        }

        public int getColor() {
            return color;
        }


        public int getRes() {
            return res;
        }
    }

}

