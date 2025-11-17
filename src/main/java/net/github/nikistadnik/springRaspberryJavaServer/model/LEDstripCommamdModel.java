package net.github.nikistadnik.springRaspberryJavaServer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LEDstripCommamdModel {

    private int from;
    private int to;
    private int over;
}
