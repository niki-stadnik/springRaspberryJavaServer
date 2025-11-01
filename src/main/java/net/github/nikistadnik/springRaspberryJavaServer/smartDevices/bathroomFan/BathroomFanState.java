package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomFan;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
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
    private boolean bathFan = false;
    private boolean auto = true;
}
