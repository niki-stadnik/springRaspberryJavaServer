package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenLEDstrip;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class kitchenLEDstripController {

    private kitchenLEDstripService service;

    public kitchenLEDstripController(kitchenLEDstripService service) {
        this.service = service;
    }

    @MessageMapping("/kitchenStrip")
    @SendTo("/topic/clientKitchenStrip")
    public kitchenLEDstripModel getData(kitchenLEDstripModel data) throws InterruptedException {
        Thread.sleep(50);
        service.setData(data);
        return data;
    }

    @MessageMapping("/clientKitchenStrip")
    public void getData(kitchenLEDstripClientModel data) throws InterruptedException {
        Thread.sleep(50);
        service.command(data);
    }
}
