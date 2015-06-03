package ru.gubkin.lk.arduinoworksheet.component.led;

import android.util.Log;

import java.util.Observable;

import ru.gubkin.lk.arduinoworksheet.R;
import ru.gubkin.lk.arduinoworksheet.util.MessageListener;

/**
 * Created by Андрей on 01.05.2015.
 */
public class LED extends Observable implements MessageListener {
    protected static final Integer DELETE_KEY = -1;
    protected static final Integer UPDATE_KEY = -2;
    protected static final Integer TOGGLE_KEY = -3;

    static final String LED_ON = "1";
    static final String LED_OFF = "0";

    private LedColors color;

    private int id;

    private boolean isActive;

    private String title;

    private String commandOn;
    private String commandOff;

    LED() {
        this(LedColors.UNDEFINED, true, "Добавить", -1, "NULL", "NULL");
    }

    LED(LedColors c, boolean active, String title, int id, String commandOn, String commandOff) {
        this.color = c;
        this.isActive = active;
        this.title = title;
        this.id = id;
        this.commandOn = commandOn;
        this.commandOff = commandOff;
    }

    public enum LedColors {
        UNDEFINED("add_new", "Добавить", -1, 0),
        RED("Red", "Красный", 0, 0xffB71C1C),
        GREEN("Green", "Зеленый", 1, 0xff00695C),
        BLUE("Blue", "Синий", 2, 0xff0277BD);

        private final String engName;
        private final String ruName;

        private final int color;
        private final int position;

        LedColors(String engName, String ruName, int position, int color) {
            this.engName = engName;
            this.ruName = ruName;
            this.position = position;
            this.color = color;
        }

        public String getEngName() {
            return engName;
        }

        public String getRuName() {
            return ruName;
        }

        public int getColorId() {
            return position;
        }

        public int getColor() {
            return color;
        }


    }

    public int getImageResourse() {
        if (color != LedColors.UNDEFINED) {
            if (isActive) {
                switch (color) {
                    case RED:
                        return R.drawable.red_led;
                    case GREEN:
                        return R.drawable.green_led;
                    case BLUE:
                        return R.drawable.blue_led;
                }
            } else {
                return R.drawable.white_led;
            }
        }
        return R.drawable.ic_add_circle_outline;
    }


    public void toggleState(boolean forceToggle) {
        isActive = !isActive;
        setChanged();
        if (forceToggle) {
            notifyObservers(TOGGLE_KEY);
        } else {
            notifyObservers(UPDATE_KEY);
        }
    }

    public boolean isActive() {
        return isActive;
    }


    public LedColors getColor() {
        return color;
    }

    public void setColor(int id) {
        setColor(LEDFactory.idToEnumColor(id));
    }

    private void setColor(LedColors color) {
        this.color = color;
        setChanged();
        notifyObservers(UPDATE_KEY);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        setChanged();
        notifyObservers(UPDATE_KEY);
    }

    public String getCommandOn() {
        return commandOn;
    }

    public void setCommandOn(String commandOn) {
        this.commandOn = commandOn;
        setChanged();
        notifyObservers(UPDATE_KEY);
    }

    public String getCommandOff() {
        return commandOff;
    }

    public void setCommandOff(String commandOff) {
        this.commandOff = commandOff;
        setChanged();
        notifyObservers(UPDATE_KEY);
    }

    public void destroy() {
        setChanged();
        notifyObservers(DELETE_KEY);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
            notifyObservers(-777);
        }
        if (message.equals(commandOff)) {
            isActive = false;
            setChanged();
            notifyObservers(-777);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        LED led = (LED) o;

        return id == led.id;
    }
}

