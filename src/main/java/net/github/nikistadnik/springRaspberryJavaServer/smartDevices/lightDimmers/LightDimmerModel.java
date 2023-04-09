package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightDimmers;

import lombok.Data;

@Data
public class LightDimmerModel {

    private String name;
    private int value;

    public LightDimmerModel() {
    }

    public LightDimmerModel(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
