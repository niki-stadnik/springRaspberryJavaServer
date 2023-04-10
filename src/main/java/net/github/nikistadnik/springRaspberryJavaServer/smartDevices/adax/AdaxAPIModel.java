package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.adax;

public class AdaxAPIModel {

    private int temp;

    private int id;

    public AdaxAPIModel() {
    }

    public AdaxAPIModel(int temp, int id) {
        this.temp = temp;
        this.id = id;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
