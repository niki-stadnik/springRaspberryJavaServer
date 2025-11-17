package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomLEDstrip;

import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.service.LEDstrip;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BathroomLEDstripService extends LEDstrip {

    public BathroomLEDstripService() {
        rebootTopic = "rebootBathroomStrip";
        rebootTopicSecond = "rebootBathroomFan";
        receiveTopic = "bathroomStrip";
        sendTopic = "clientBathroomStrip";
        lightToLookFor = 1;
    }
/*
    @Scheduled(fixedRate = 1000)
    public void test(){
        log.info("test");
        if (deviceRegistry.bathroomFanState().bathTemp1() == null) return;
        double temp = deviceRegistry.bathroomFanState().bathTemp1();
        log.info(String.valueOf(temp));

    }

 */


}
