package net.github.nikistadnik.springRaspberryJavaServer.smartDevices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
@Slf4j
public class RebootDevice {

    public enum destination {

        LED_KITCHEN("restartKitchen1"),
        KITCHEN("restartKitchen2"),
        LIGHT_SWITCH("rebootLightSwitch"),
        DOORMAN("rebootDoorman"),
        LED_BATHROOM("rebootBathroomStrip"),
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

    private final DeviceRegistry deviceRegistry;

    public void rebootDev(destination dest) {
        long now = System.nanoTime();
        long last = deviceRegistry.lastCall().get();
        // 5 seconds in nanoseconds = 5_000_000_000L
        if (now - last < 5_000_000_000L) {
            log.info("last reboot call was less than 5 seconds ago");
            return;
        }
        deviceRegistry.lastCall().set(now);
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
