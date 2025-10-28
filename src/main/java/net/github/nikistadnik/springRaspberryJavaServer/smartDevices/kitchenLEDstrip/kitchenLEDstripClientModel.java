package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenLEDstrip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class kitchenLEDstripClientModel {

    private int command;
    private int duty;
    private int time;

}
