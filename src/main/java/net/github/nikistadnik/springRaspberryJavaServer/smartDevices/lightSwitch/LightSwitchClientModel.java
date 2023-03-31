package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch;

import lombok.Data;

@Data
public class LightSwitchClientModel {

    private int light;
    private boolean state;

    public LightSwitchClientModel() {
    }

    public LightSwitchClientModel(int light, boolean state) {
        this.light = light;
        this.state = state;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
