package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorman;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;

@Getter
@Accessors(fluent = true, chain = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DoormanDoorBellEvent extends ApplicationEvent {

    private final double held;

    public DoormanDoorBellEvent(Object source, double held) {
        super(source);
        this.held = held;
    }
}
