package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Data
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LightSwitchState {

    private final AtomicBoolean[] light = new AtomicBoolean[8];

    @PostConstruct
    public void LightInit() {
        for (int i = 0; i < light.length; i++) {
            light[i] = new AtomicBoolean(false);
        }
    }
}
