package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorman;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.RebootDevice;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch.LightStatusChangedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DoormanService {

    private final RebootDevice rebootDevice;


    private final ApplicationEventPublisher eventPublisher;

    private static boolean active = false;

    private static boolean doorOpen = false;
    private static boolean doorLock = false;
    private static boolean doorButton = false;
    private static boolean bell = false;
    private static boolean rfid = false;
    private static boolean bellFlag = false;
    private static long start = 0;


    public void setData (DoormanModel data){
        active = true;
        doorOpen = data.isDoorOpen();
        doorLock = data.isDoorLock();
        doorButton = data.isDoorButton();
        bell = data.isBell();
        rfid = data.isRfid();
        if (bell) {
            if (!bellFlag) {
                bellFlag = true;
                start = System.currentTimeMillis();
                double held = 0;
                log.info("door bell start");
                eventPublisher.publishEvent(new DoormanDoorBellEvent(this, held));
            }
        } else if (bellFlag) {
            bellFlag = false;
            double held = ((double)((System.currentTimeMillis() - start) / 100)) / 10;
            //accounting for the delayed discharge of the AC current
            held -= 3.5;
            log.info("door bell end: " + held);
            eventPublisher.publishEvent(new DoormanDoorBellEvent(this, held));
        }
        //log.info(data.toString());
    }

    public void command(DoormanClientModel data){
        int com = data.getCommand();
        if (com == 1) rebootDevice.rebootDev(RebootDevice.destination.LIGHT_SWITCH);
        if (com == 2) rebootDevice.rebootDev(RebootDevice.destination.DOORMAN);
        log.info(data.toString());
    }



    @Scheduled(initialDelay = 10000, fixedRate = 10000)    //every 10s
    private synchronized void selfReboot(){
        if (!active) rebootDevice.rebootDev(RebootDevice.destination.DOORMAN);
        active = false;
    }
}
