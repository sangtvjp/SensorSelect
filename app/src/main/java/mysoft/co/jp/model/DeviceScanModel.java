package mysoft.co.jp.model;

import android.widget.ImageView;

import java.io.Serializable;

public class DeviceScanModel implements Serializable {
    private String deviceName;
    private String address;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

}
