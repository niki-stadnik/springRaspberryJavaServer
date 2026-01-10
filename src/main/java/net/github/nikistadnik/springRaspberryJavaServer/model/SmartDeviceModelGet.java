package net.github.nikistadnik.springRaspberryJavaServer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmartDeviceModelGet {

    //private int command;
    private Object payload;
}

