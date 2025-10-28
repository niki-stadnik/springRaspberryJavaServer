package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.herbPot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class herbPotModel {

    private float temp1;
    private float temp2;
    private int moisture1;
    private int moisture2;
    private boolean watered;

}
