package net.github.nikistadnik.springRaspberryJavaServer.smartDevices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
public abstract class SmartDevice {

    @Autowired
    protected SimpMessageSendingOperations messaging;
    @Autowired
    protected DeviceRegistry deviceRegistry;
    @Autowired
    protected ApplicationEventPublisher eventPublisher;

    protected String rebootTopic;
    protected String receiveTopic;
    protected String sendTopic;


    //Reboot logic - make "active" true, every time data is received
    protected static boolean active = false;
    @Scheduled(initialDelay = 10000, fixedRate = 30000)    //every 30s
    private synchronized void selfReboot(){
        if (!active) rebootDev(rebootTopic);
        active = false;
    }
    public void rebootDev(String dest) {
        long now = System.nanoTime();
        long last = deviceRegistry.lastCall().get();
        // 5 seconds in nanoseconds = 5_000_000_000L
        if (now - last < 5_000_000_000L) {
            log.info("last reboot call was less than 5 seconds ago");
            return;
        }
        deviceRegistry.lastCall().set(now);
        messaging.convertAndSend("/topic/" + dest, "{\"relayRestart\":true}");
        log.info("restart device: " + dest);
    }


}
