package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenLEDstrip;

import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.service.LEDstrip;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KitchenLEDstripService extends LEDstrip {
    @Override
    protected String provideName() {
        return "kitchenStrip";
    }

    @Override
    protected String pairedWithName() {
        return "kitchen2";
    }

    public KitchenLEDstripService() {
        lightToLookFor = 2;
    }

}
