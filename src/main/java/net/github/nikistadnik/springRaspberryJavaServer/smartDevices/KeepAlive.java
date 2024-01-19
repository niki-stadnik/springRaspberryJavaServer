package net.github.nikistadnik.springRaspberryJavaServer.smartDevices;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class KeepAlive {
    //if esp32 freezes again just revert to a main KeepAlive
    @Scheduled(fixedRate = 20000)    //every 20s
    private void keepAlive() {
        SendMessage.sendMessage("/topic/keepAlive", "donNotDie");
        System.out.println("keepAlive SENT");
    }

}
