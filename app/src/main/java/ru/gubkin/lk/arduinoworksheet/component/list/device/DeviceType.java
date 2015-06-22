package ru.gubkin.lk.arduinoworksheet.component.list.device;

/**
 * Created by root on 22.06.15.
 */
public enum DeviceType {
    BLUETOOTH(0),
    TCP(1);
    private int id;

    DeviceType(int id) {

        this.id = id;
    }

    public int getId() {
        return id;
    }
}
