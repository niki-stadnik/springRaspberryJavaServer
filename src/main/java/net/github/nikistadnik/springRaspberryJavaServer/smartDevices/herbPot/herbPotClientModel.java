package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.herbPot;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class herbPotClientModel {

    private int water;
    private boolean restart;

}
