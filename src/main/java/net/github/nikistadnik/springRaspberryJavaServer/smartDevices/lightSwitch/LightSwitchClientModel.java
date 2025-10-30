package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LightSwitchClientModel {

    @Accessors(fluent = true)
    private boolean[] stateLight;
    @Accessors(fluent = true)
    private boolean[] switchStateOf;

}
