package ru.gubkin.lk.arduinoworksheet.component.led;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ru.gubkin.lk.arduinoworksheet.adapter.LedGridAdapter;
import ru.gubkin.lk.arduinoworksheet.db.LedDBHandler;
import ru.gubkin.lk.arduinoworksheet.util.MessageHandler;

/**processLed
 * Created by Андрей on 02.05.2015.
 */
public class LEDObserver implements Observer {
    private LedDBHandler db;
    private LedGridAdapter gridAdapter;
    private LEDController controller;

    private ArrayList<LED> displayedLeds;

    public LEDObserver(LedDBHandler db, LEDController controller) {
        this.db = db;
        this.controller = controller;
    }

    public void setAdapter(LedGridAdapter adapter) {
        gridAdapter = adapter;
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data != null) {
            LED led = (LED) observable;
            Integer key = (Integer) data;
            if (key.equals(LED.DELETE_KEY)) {
                db.deleteLed(led);
                displayedLeds.remove(led);
            }
            if (key.equals(LED.UPDATE_KEY)) {
                db.updateLed(led);
            }
            if (key.equals(LED.TOGGLE_KEY)) {
                controller.processLed(led);
            }

        }
        controller.notifyChange();
    }

    public void setDisplayedLeds(ArrayList<LED> displayedLeds) {
        this.displayedLeds = displayedLeds;
    }
}
