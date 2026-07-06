package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorman;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;

@Getter
@Accessors(fluent = true, chain = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DoormanDoorButtonEvent extends ApplicationEvent {

    private final double held;

    public DoormanDoorButtonEvent(Object source, double held) {
        super(source);
        this.held = held;
    }
}
