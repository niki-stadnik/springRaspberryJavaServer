package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightDimmer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LightDimmerModel {

    private String name;
    private int value;

}
