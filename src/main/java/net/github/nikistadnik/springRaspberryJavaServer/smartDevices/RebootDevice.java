package net.github.nikistadnik.springRaspberryJavaServer.smartDevices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RebootDevice {

    private final SimpMessageSendingOperations messaging;

    public void rebootDev(String dest) {
        messaging.convertAndSend("/topic/" + dest, "{\"relayRestart\":true}");
        log.info("restart device:" + dest);
    }
}
