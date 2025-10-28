package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LightSwitchClientModel {

    private int light;
    private boolean state;

}
