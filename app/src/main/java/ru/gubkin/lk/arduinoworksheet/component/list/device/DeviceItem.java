package ru.gubkin.lk.arduinoworksheet.component.list.device;

import java.util.Observable;

import ru.gubkin.lk.arduinoworksheet.R;
import ru.gubkin.lk.arduinoworksheet.component.ComponentObserver;

import static ru.gubkin.lk.arduinoworksheet.component.list.device.DeviceType.*;

/**
 * Created by root on 05.06.15.
 */
public class DeviceItem extends Observable {
    public static final int DEFAULT_ID = -1;
    private int id;
    private DeviceType type;

    private String name;
    private String info;
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
        this.info = info;
        String[] spl = info.split(":");
        this.address = spl[0];
        this.port = spl[1];
    }



    public String getName() {
        return name;
    }

    public String getInfo() {
        if (info != null) return info;

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

    public void setName(String name) {
        this.name = name;
        setChanged();
        notifyObservers(ComponentObserver.UPDATE_KEY);
    }

    public void setAddress(String address) {
        this.address = address;
        setChanged();
        notifyObservers(ComponentObserver.UPDATE_KEY);
    }

    public void setPort(String port) {
        this.port = port;
        setChanged();
        notifyObservers(ComponentObserver.UPDATE_KEY);
    }

    public void destroy() {
        setChanged();
        notifyObservers(ComponentObserver.DELETE_KEY);
    }

    public int getImageResource() {
       if(id == DEFAULT_ID){
           return R.drawable.ic_bluetooth_black_48dp;
       }
        switch (type) {
            case TCP: return R.drawable.ic_http_black_48dp;
            default: return R.drawable.ic_bluetooth_black_48dp;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;

        if (o == this) return true;

        DeviceItem item = (DeviceItem) o;
        return info != null && item.info != null && info.equals(item.info);

    }
}
