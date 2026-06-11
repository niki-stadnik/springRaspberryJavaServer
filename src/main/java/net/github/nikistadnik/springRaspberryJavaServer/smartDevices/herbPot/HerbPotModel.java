package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.herbPot;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class HerbPotModel {

    private float temp1;
    private float temp2;
    private int moisture1;
    private int moisture2;
    private boolean herbLight;
}
