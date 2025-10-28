package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LightSwitchModel {

    //private Double fuseBoxTemp;
    //private Double fuseBoxHum;
    //private boolean fuseBoxFan;
    private boolean light0;
    private boolean light1;
    private boolean light2;
    private boolean light3;
    private boolean light4;
    private boolean light5;
    private boolean light6;
    private boolean light7;

}
