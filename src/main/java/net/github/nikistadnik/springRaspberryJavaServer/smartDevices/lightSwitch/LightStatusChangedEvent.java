package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch;

import org.springframework.context.ApplicationEvent;

public class LightStatusChangedEvent extends ApplicationEvent {
    private final int light;
    private final boolean state;

    public LightStatusChangedEvent(Object source, int light, boolean state) {
        super(source);
        this.light = light;
        this.state = state;
    }

    public int getLight() {
        return light;
    }

    public boolean isState() {
        return state;
    }
}