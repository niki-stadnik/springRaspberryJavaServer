package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorman;

import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SmartDevice;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class DoormanService extends SmartDevice<DoormanModel, DoormanClientModel> {


    private static boolean doorOpen = false;
    private static boolean doorLock = false;
    private static boolean doorButton = false;
    private static boolean bell = false;
    private static boolean rfid = false;
    private static boolean bellFlag = false;
    private static long start = 0;


    @Override
    protected String provideName() {
        return "doorman";
    }

    @Override
    protected String pairedWithName() {
        return "lightSwitch";
    }

    @Override
    protected Class provideDeviceModel() {
        return DoormanModel.class;
    }

    @Override
    protected Class provideClientModel() {
        return DoormanClientModel.class;
    }

    @Override
    protected void handleDeviceData(DoormanModel data) {
        messaging.convertAndSend("/topic/doormanClient", data);
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

    @Override
    protected void handleClientData(DoormanClientModel data) {
        int com = data.getCommand();
        if (com == 1) rebootDev(pairName);
        if (com == 2) {
            selfRebootDev(deviceName);
            rebootDev(deviceName);
        }
        log.info(data.toString());
    }
}
