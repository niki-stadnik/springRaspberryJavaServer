package net.github.nikistadnik.springRaspberryJavaServer.smartDevices;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.experimental.Accessors;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomFan.BathroomFanState;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch.LightSwitchState;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
@Data
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DeviceRegistry {

    private final LightSwitchState lightSwitchState;
    private final BathroomFanState bathroomFanState;

    private final AtomicLong lastCall = new AtomicLong(0);
}
