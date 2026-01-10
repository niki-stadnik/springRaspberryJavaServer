package net.github.nikistadnik.springRaspberryJavaServer.smartDevices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.discord.DiscordServiceBE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class RebootDevice {

    public enum destination {

        LED_KITCHEN("kitchenStrip/reboot"), //restartKitchen1
        KITCHEN("kitchen2/reboot"),     //restartKitchen2
        LIGHT_SWITCH("lightSwitch/reboot"),
        DOORMAN("doorman/reboot"),
        LED_BATHROOM("bathroomStrip/reboot"),
        FAN_BATHROOM("bathroomFan/reboot");

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

    private final DiscordServiceBE discordServiceBE;

    @Autowired
    private Environment env;

    @Value("${discord.channel.id.reboot-log}")
    private String discordChannelIdRebootLog;


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
        if (Arrays.asList(env.getActiveProfiles()).contains("prod")) {
            discordServiceBE.sendMessage(String.valueOf(dest), discordChannelIdRebootLog);
        }
    }

    /*
    To call this:
    @RequiredArgsConstructor
    private final RebootDevice rebootDevice;
    rebootDevice.rebootDev(RebootDevice.destination.DOORMAN);
     */



}
