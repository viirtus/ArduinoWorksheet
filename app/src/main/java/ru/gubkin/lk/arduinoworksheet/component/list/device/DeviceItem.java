package ru.gubkin.lk.arduinoworksheet.component.list.device;

import java.util.Observable;

/**
 * Created by root on 05.06.15.
 */
public class DeviceItem extends Observable {
    private int id;
    private String name;
    private String address;
    private String port;

    public DeviceItem(String name, String address, String port) {

        this.name = name;
        this.address = address;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return address + ":" + port;
    }

    public String getAddress() {
        return address;
    }

    public String getPort () {
        return port;
    }



}
