package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomFan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BathroomFanToClientModel {

    private boolean auto1 = true;
    private boolean auto2 = true;
    private Double bathTemp1 = null;
    private Double bathTemp2 = null;
    private Double bathHum1 = null;
    private Double bathHum2 = null;
    private boolean bathFan1 = false;
    private boolean bathFan2 = false;
    private int minHum1 = 0;
    private int maxHum1 = 0;
    private int minHum2 = 0;
    private int maxHum2 = 0;
}
