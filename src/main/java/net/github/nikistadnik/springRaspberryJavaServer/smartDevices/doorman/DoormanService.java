package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorman;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.RebootDevice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DoormanService {

    private final RebootDevice rebootDevice;

    private static boolean active = false;

    private static boolean doorOpen = false;
    private static boolean doorLock = false;
    private static boolean doorButton = false;
    private static boolean bell = false;
    private static boolean rfid = false;


    public void setData (DoormanModel data){
        active = true;
        doorOpen = data.isDoorOpen();
        doorLock = data.isDoorLock();
        doorButton = data.isDoorButton();
        bell = data.isBell();
        rfid = data.isRfid();
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
