package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.herbPot;


import lombok.Data;

@Data
public class herbPotClientModel {

    private int water;
    private boolean restart;

    public herbPotClientModel() {
    }

    public herbPotClientModel(int water, boolean restart) {
        this.water = water;
        this.restart = restart;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public boolean isRestart() {
        return restart;
    }

    public void setRestart(boolean restart) {
        this.restart = restart;
    }
}
