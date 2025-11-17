package net.github.nikistadnik.springRaspberryJavaServer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LEDstripClientModel {

    private int command;
    private int duty;
    private int time;

}
