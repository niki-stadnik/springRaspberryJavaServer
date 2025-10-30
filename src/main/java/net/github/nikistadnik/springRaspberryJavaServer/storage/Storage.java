package net.github.nikistadnik.springRaspberryJavaServer.storage;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;


enum Mode {ON, OFF, AUTO}

@NoArgsConstructor
@Entity
@Table(name = "storage")
public class Storage {

    @Getter @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //from bathroom fan
    @Getter @Setter
    @Column(name = "bath_temp")
    private Double bathTemp;
    @Getter @Setter
    @Column(name = "bath_hum")
    private Double bathHum;
    @Getter @Setter
    @Column(name = "bath_light")
    private Double bathLight;
    @Getter @Setter
    @Column(name = "bath_fan")
    private boolean bathFan;

    //"bathroomFanMode", "auto"
    //"BathroomFanDelay", 30
    private static Double bathroomFanDelay;
    private static Mode bathroomFanMode = Mode.AUTO;


    private static Map mapStorage = new LinkedHashMap();


    @Override
    public String toString(){
        return "{\"id\":" + id + ",\"bathTemp\":" + bathTemp + ",\"bathHum\":" + bathHum + ",\"bathLight\":" + bathLight + ",\"bathFan\":" + bathFan + "}";
    }


    public Storage(
            Double bathTemp1,
            Double bathTemp2,
            Double bathHum1,
            Double bathHum2,
            boolean bathFan
    ) {
        this.bathTemp = bathTemp;
        this.bathHum = bathHum;
        this.bathLight = bathLight;
        this.bathFan =bathFan;
    }
}