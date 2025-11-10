package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class LightStatusChangedEvent extends ApplicationEvent {
    private final int light;
    private final boolean state;

    public LightStatusChangedEvent(Object source, int light, boolean state) {
        super(source);
        this.light = light;
        this.state = state;
    }

}