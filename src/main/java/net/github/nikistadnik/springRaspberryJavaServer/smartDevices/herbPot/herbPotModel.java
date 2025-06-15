package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.herbPot;

import lombok.Data;

@Data
public class herbPotModel {

    private float temp1;
    private float temp2;
    private int moisture1;
    private int moisture2;
    private boolean watered;

    public herbPotModel(){
    }

    public herbPotModel(float temp1, float temp2, int moisture1, int moisture2, boolean watered) {
        this.temp1 = temp1;
        this.temp2 = temp2;
        this.moisture1 = moisture1;
        this.moisture2 = moisture2;
        this.watered = watered;
    }

    public float getTemp1() {
        return temp1;
    }

    public void setTemp1(float temp1) {
        this.temp1 = temp1;
    }

    public float getTemp2() {
        return temp2;
    }

    public void setTemp2(float temp2) {
        this.temp2 = temp2;
    }

    public int getMoisture1() {
        return moisture1;
    }

    public void setMoisture1(int moisture1) {
        this.moisture1 = moisture1;
    }

    public int getMoisture2() {
        return moisture2;
    }

    public void setMoisture2(int moisture2) {
        this.moisture2 = moisture2;
    }

    public boolean isWatered() {
        return watered;
    }

    public void setWatered(boolean watered) {
        this.watered = watered;
    }
}
