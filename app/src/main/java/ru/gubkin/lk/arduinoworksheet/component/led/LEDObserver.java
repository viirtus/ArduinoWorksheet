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
    private LEDController controller;

    public LEDObserver(LedDBHandler db, LEDController controller) {
        this.db = db;
        this.controller = controller;
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data != null) {
            LED led = (LED) observable;
            Integer key = (Integer) data;
            if (key.equals(LED.DELETE_KEY)) {
                db.deleteLed(led);
                controller.removeItem(led);
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
}
