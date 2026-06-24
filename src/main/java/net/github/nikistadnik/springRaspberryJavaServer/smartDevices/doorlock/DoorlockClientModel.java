package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorlock;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoorlockClientModel {

    private int move;
    private int acc;
    private int minSpeed;
    private int maxSpeed;
    private int i11;
    private int i12;
    private int i21;
    private int i22;
    private int i31;
    private int i32;

}
