package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorman;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoormanModel {

    private boolean doorButton;
    private boolean bell;
    private String rfid;

}
