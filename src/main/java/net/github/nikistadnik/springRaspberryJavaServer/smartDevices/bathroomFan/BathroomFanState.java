package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomFan;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

@Component
@Data
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class BathroomFanState {

    private Double bathTemp1 = null;
    private Double bathTemp2 = null;
    private Double bathHum1 = null;
    private Double bathHum2 = null;
    private boolean bathFan1 = false;
    private boolean bathFan2 = false;
    private boolean auto1 = true;
    private boolean auto2 = true;
    private Double button = null;
    private boolean[] requestedFanState = {false, false};
    private int minHum1 = 55;
    private int maxHum1 = 70;
    private int minHum2 = 70;
    private int maxHum2 = 80;
}
