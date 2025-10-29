package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.herbPot;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class herbPotController {

    private final herbPotService service;

    @MessageMapping("/herbPot")
    public void getData (herbPotModel data) throws InterruptedException {
        Thread.sleep(50);
        service.setData(data);
    }

    @MessageMapping("/clientHerbPot")
    public void getData(herbPotClientModel data) throws InterruptedException {
        Thread.sleep(50);
        service.command(data);
    }
}
