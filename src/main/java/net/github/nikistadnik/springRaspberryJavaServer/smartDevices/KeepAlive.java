package net.github.nikistadnik.springRaspberryJavaServer.smartDevices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class KeepAlive {

    protected final SimpMessageSendingOperations messaging;

    //if esp32 freezes again just revert to a main KeepAlive
    @Scheduled(fixedRate = 20000)    //every 20s
    private void keepAlive() {
        //SendMessage.sendMessage("/topic/keepAlive", "donNotDie");
        messaging.convertAndSend("/topic/keepAlive", "donNotDie");
        //log.info("keep alive");
    }

}
