package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomFan;

import lombok.Data;

@Data
public class BathroomFanModel {

    private Double bathTemp = null;
    private Double bathHum = null;
    private Double bathLight = null;
    private boolean bathFan = false;

    public BathroomFanModel(Double bathTemp, Double bathHum, Double bathLight, boolean bathFan) {
        this.bathTemp = bathTemp;
        this.bathHum = bathHum;
        this.bathLight = bathLight;
        this.bathFan = bathFan;
    }

    public BathroomFanModel() {
    }


    public Double getBathTemp() {
        return bathTemp;
    }

    public void setBathTemp(Double bathTemp) {
        this.bathTemp = bathTemp;
    }

    public Double getBathHum() {
        return bathHum;
    }

    public void setBathHum(Double bathHum) {
        this.bathHum = bathHum;
    }

    public Double getBathLight() {
        return bathLight;
    }

    public void setBathLight(Double bathLight) {
        this.bathLight = bathLight;
    }

    public boolean isBathFan() {
        return bathFan;
    }

    public void setBathFan(boolean bathFan) {
        this.bathFan = bathFan;
    }
}
