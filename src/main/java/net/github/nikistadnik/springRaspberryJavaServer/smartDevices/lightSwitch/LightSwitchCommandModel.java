package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LightSwitchCommandModel {

    private boolean pulse0;
    private boolean pulse1;
    private boolean pulse2;
    private boolean pulse3;
    private boolean pulse4;
    private boolean pulse5;
    private boolean pulse6;
    private boolean pulse7;

}
