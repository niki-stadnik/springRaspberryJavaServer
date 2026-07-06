package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorman;

import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SmartDevice;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorlock.DoorlockService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class DoormanService extends SmartDevice<DoormanModel, DoormanClientModel> {


    private static boolean doorOpen = false;
    private static boolean doorLock = false;
    private static boolean doorButton = false;
    private static boolean bell = false;
    private static String rfid;
    private static boolean bellFlag = false;
    private static boolean doorFlag = false;
    private static long start = 0;
    private static long startB = 0;

    @Value("${rfid.card}")
    String rfidCard;


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
    protected void loadVariables() {

    }

    @Override
    protected void handleDeviceData(DoormanModel data) {
        messaging.convertAndSend("/topic/doormanClient", data);
        log.info(data.toString());
        doorButton = data.doorButton();
        bell = data.bell();
        if (data.rfid() != null) {
            rfid = data.rfid();
            if (rfid.equals(rfidCard)) {
                log.info("door rfid scanned: {}", rfid);
                eventPublisher.publishEvent(new DoormanDoorRfidEvent(this, rfid));
            }
        }
        if (doorButton && !doorFlag) {
            doorFlag = true;
            startB = System.currentTimeMillis();
            log.info("door button start");
        } else if (doorFlag) {
            doorFlag = false;
            long held = System.currentTimeMillis() - startB;
            log.info("door button end: {}", held);
            eventPublisher.publishEvent(new DoormanDoorButtonEvent(this, held));
        }
        if (bell && !bellFlag) {
            bellFlag = true;
            start = System.currentTimeMillis();
            double held = 0;
            log.info("door bell start");
            eventPublisher.publishEvent(new DoormanDoorBellEvent(this, held));
        } else if (bellFlag) {
            bellFlag = false;
            double held = ((double) ((System.currentTimeMillis() - start) / 100)) / 10;
            //accounting for the delayed discharge of the AC current
            held -= 3.5;
            log.info("door bell end: " + held);
            eventPublisher.publishEvent(new DoormanDoorBellEvent(this, held));
        }
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
