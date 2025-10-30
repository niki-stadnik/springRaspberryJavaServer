package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomFan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BathroomFanModel {

    private boolean auto = true;
    private Double bathTemp1 = null;
    private Double bathTemp2 = null;
    private Double bathHum1 = null;
    private Double bathHum2 = null;
    private boolean bathFan = false;

}
