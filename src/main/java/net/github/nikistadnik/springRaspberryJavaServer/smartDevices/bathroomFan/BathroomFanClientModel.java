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

    private boolean bathFanCommand = false;
    private boolean auto = true;

}
