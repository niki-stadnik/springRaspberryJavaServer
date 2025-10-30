package net.github.nikistadnik.springRaspberryJavaServer.smartDevices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RebootDevice {

    public enum destination {

        LED_KITCHEN("restartKitchen1"),
        KITCHEN("restartKitchen2"),
        LIGHT_SWITCH("rebootLightSwitch"),
        DOORMAN("rebootDoorman"),
        LED_BATHROOM("rebootBathroomLed"),
        FAN_BATHROOM("rebootBathroomFan");

        private final String value;
        destination(String value) {
            this.value = value;
        }
        public String value() {
            return value;
        }
    }

    private final SimpMessageSendingOperations messaging;

    public void rebootDev(destination dest) {
        messaging.convertAndSend("/topic/" + dest.value(), "{\"relayRestart\":true}");
        log.info("restart device:" + dest);
    }

    /*
    To call this:
    @RequiredArgsConstructor
    private final RebootDevice rebootDevice;
    rebootDevice.rebootDev(RebootDevice.destination.DOORMAN);
     */



}
