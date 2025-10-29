package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenLEDstrip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class kitchenLEDstripCommamdModel {

    private int from;
    private int to;
    private int over;
}
