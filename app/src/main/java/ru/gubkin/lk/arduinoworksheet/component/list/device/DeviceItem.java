package ru.gubkin.lk.arduinoworksheet.component.list.device;

import java.util.Observable;

/**
 * Created by root on 05.06.15.
 */
public class DeviceItem extends Observable {
    private int id;
    private DeviceType type;
    private String name;
    private String address;
    private String port;

    public DeviceItem(int id, DeviceType type, String name, String address, String port) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.address = address;
        this.port = port;
    }

    public DeviceItem(int id, DeviceType type, String name, String info) {
        this.id = id;
        this.type = type;
        this.name = name;
        String[] info_ = info.split(":");
        this.address = info_[0];
        this.port = info_[1];
    }



    public String getName() {
        return name;
    }

    public String getInfo() {
        return address + ":" + port;
    }

    public DeviceType getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public String getPort () {
        return port;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
