package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomLEDstrip;

import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.model.LEDstripClientModel;
import net.github.nikistadnik.springRaspberryJavaServer.model.LEDstripModel;
import net.github.nikistadnik.springRaspberryJavaServer.service.LEDstrip;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BathroomLEDstripService extends LEDstrip {

    public BathroomLEDstripService() {
        lightToLookFor = 1;
    }


    @Override
    protected String provideName() {
        return "bathroomStrip";
    }

    @Override
    protected String pairedWithName() {
        return "bathroomFan";
    }


}
