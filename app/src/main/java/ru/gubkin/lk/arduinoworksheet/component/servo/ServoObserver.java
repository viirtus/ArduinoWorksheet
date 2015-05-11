package ru.gubkin.lk.arduinoworksheet.component.servo;

import java.util.Observable;
import java.util.Observer;

import ru.gubkin.lk.arduinoworksheet.db.ServoDBHandler;

/**
 * Created by Андрей on 08.05.2015.
 */
public class ServoObserver implements Observer {
    ServoController controller;
    ServoDBHandler dbHandler;

    public ServoObserver(ServoController controller, ServoDBHandler dbHandler) {
        this.controller = controller;
        this.dbHandler = dbHandler;
    }

    @Override
    public void update(Observable observable, Object data) {
        Servo servo = (Servo) observable;
        Integer key = (Integer) data;
        if (key.equals(Servo.CONTROL_KEY)) {
            controller.processServo(servo);
            dbHandler.updateServo(servo);
        }
        if (key.equals(Servo.UPDATE_KEY)) {
            dbHandler.updateServo(servo);
            controller.notifyChange();
        }
        if (key.equals(Servo.DELETE_KEY)) {
            dbHandler.deleteServo(servo);
            controller.deleteServo(servo);
            controller.notifyChange();
        }
    }
}
