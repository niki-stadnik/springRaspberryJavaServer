package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomFan;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class BathroomFanClientModel {

    private int[] modeFan = {0, 0};
    /*
    0=no action
    1=fan off
    2=fan on
    3=fan auto
     */
    private int minHum1 = 0;
    private int maxHum1 = 0;
    private int minHum2 = 0;
    private int maxHum2 = 0;

}
