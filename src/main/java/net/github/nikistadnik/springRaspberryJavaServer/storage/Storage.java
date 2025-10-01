package net.github.nikistadnik.springRaspberryJavaServer.storage;

import jakarta.persistence.*;

import java.util.LinkedHashMap;
import java.util.Map;


enum Mode {ON, OFF, AUTO}


@Entity
@Table(name = "storage")
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //from bathroom fan
    @Column(name = "bath_temp")
    private Double bathTemp;
    @Column(name = "bath_hum")
    private Double bathHum;
    @Column(name = "bath_light")
    private Double bathLight;
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


    public Storage() {
    }

    public Storage(
            Double bathTemp,
            Double bathHum,
            Double bathLight,
            boolean bathFan
    ) {
        this.bathTemp = bathTemp;
        this.bathHum = bathHum;
        this.bathLight = bathLight;
        this.bathFan =bathFan;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getBathTemp() {
        return bathTemp;
    }

    public void setBathTemp(Double bathTemp) {
        this.bathTemp = bathTemp;
    }

    public Double getBathHum() {
        return bathHum;
    }

    public void setBathHum(Double bathHum) {
        this.bathHum = bathHum;
    }

    public Double getBathLight() {
        return bathLight;
    }

    public void setBathLight(Double bathLight) {
        this.bathLight = bathLight;
    }

    public boolean isBathFan() {
        return bathFan;
    }

    public void setBathFan(boolean bathFan) {
        this.bathFan = bathFan;
    }
}