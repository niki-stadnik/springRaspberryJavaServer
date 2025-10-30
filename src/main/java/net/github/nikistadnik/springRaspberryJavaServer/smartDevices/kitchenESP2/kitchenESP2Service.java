package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenESP2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.RebootDevice;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenLEDstrip.kitchenLEDstripService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class kitchenESP2Service {

    private final RebootDevice rebootDevice;

    private static boolean active = false;

    private static boolean here = false;
    public void setData(kitchenESP2Model data){
        active = true;
        here = data.isHere();
        log.info(data.toString());
    }

    @Scheduled(fixedRate = 300000)    //every 5m
    private synchronized void selfReboot(){
        if (!active) rebootDevice.rebootDev(RebootDevice.destination.KITCHEN);
        active = false;
    }
}
