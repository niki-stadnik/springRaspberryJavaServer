package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorman;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;

@Getter
@Accessors(fluent = true, chain = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DoormanDoorRfidEvent extends ApplicationEvent {

    private final String rfid;

    public DoormanDoorRfidEvent(Object source, String rfid) {
        super(source);
        this.rfid = rfid;
    }
}
