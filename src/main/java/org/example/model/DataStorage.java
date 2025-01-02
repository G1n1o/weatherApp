package org.example.model;

import java.io.Serializable;

public class DataStorage implements Serializable {

    private final String cityName;

    public DataStorage(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }
}
