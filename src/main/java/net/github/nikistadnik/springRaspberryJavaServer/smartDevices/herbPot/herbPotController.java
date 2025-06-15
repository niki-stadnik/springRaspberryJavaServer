package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.herbPot;

import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenLEDstrip.kitchenLEDstripClientModel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class herbPotController {

    private herbPotService service;

    public herbPotController(herbPotService service) {
        this.service = service;
    }

    @MessageMapping("/herbPot")
    //@SendTo("/topic/clientHerbPot")
    public herbPotModel getData (herbPotModel data) throws InterruptedException {
        Thread.sleep(50);
        service.setData(data);
        return data;
    }

    @MessageMapping("/clientHerbPot")
    public void getData(herbPotClientModel data) throws InterruptedException {
        Thread.sleep(50);
        service.command(data);
    }
}
