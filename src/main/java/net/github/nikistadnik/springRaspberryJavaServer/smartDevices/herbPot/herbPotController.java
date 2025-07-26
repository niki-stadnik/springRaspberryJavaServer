package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.herbPot;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class herbPotController {

    private herbPotService service;

    public herbPotController(herbPotService service) {
        this.service = service;
    }

    @MessageMapping("/herbPot")
    //@SendTo("/topic/clientHerbPot")
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
