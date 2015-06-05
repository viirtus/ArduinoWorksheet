package ru.gubkin.lk.arduinoworksheet.component.list.device;

/**
 * Created by root on 05.06.15.
 */
public class DeviceItem {
    private final String name;
    private final String address;
    private final String port;

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
}
