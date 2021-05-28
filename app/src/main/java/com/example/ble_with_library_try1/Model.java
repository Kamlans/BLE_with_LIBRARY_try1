package com.example.ble_with_library_try1;

public class Model {

    private String name , mac;

    public Model(String name , String  mac) {

        this.name = name;
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
