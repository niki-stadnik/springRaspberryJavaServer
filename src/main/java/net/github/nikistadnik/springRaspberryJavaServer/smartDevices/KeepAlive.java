package net.github.nikistadnik.springRaspberryJavaServer.smartDevices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KeepAlive {
    //if esp32 freezes again just revert to a main KeepAlive
    @Scheduled(fixedRate = 20000)    //every 20s
    private void keepAlive() {
        SendMessage.sendMessage("/topic/keepAlive", "donNotDie");
        //log.info("keep alive");
    }

}
