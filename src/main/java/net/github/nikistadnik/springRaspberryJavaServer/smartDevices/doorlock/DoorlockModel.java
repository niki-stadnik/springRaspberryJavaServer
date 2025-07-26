package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorlock;


import lombok.Data;

@Data
public class DoorlockModel {

    private int position;

    public DoorlockModel() {
    }

    public DoorlockModel(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
