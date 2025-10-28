package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomFan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BathroomFanModel {

    private Double bathTemp = null;
    private Double bathHum = null;
    private Double bathLight = null;
    private boolean bathFan = false;

}
