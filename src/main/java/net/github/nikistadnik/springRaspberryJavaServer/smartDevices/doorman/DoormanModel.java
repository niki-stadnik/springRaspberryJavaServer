package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorman;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoormanModel {

    private boolean doorOpen;
    private boolean doorLock;
    private boolean doorButton;
    private boolean bell;
    private boolean rfid;

}
