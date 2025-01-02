package org.example.model;

import java.io.Serializable;

public class DataStorage implements Serializable {

    private String cityName;

    public DataStorage(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
