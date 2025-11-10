package net.github.nikistadnik.springRaspberryJavaServer.storage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data
@Entity(name = "Storage")
@Table(name = "storage")
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //from bathroom fan
    @Column(name = "bath_temp_1")
    private Double bathTemp1;
    @Column(name = "bath_temp_2")
    private Double bathTemp2;
    @Column(name = "bath_hum_1")
    private Double bathHum1;
    @Column(name = "bath_hum_2")
    private Double bathHum2;
    @Column(name = "bath_fan")
    private boolean bathFan;
    //from light switch
    @Column(name = "light_0")
    private boolean light0;
    @Column(name = "light_1")
    private boolean light1;
    @Column(name = "light_2")
    private boolean light2;
    @Column(name = "light_3")
    private boolean light3;
    @Column(name = "light_4")
    private boolean light4;
    @Column(name = "light_5")
    private boolean light5;
    @Column(name = "light_6")
    private boolean light6;

}