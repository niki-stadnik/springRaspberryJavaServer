package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomFan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BathroomFanClientModel {

    private boolean bathFanCommand = false;
    private boolean auto = true;

}
