package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenLEDstrip;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class kitchenLEDstripController {

    private final kitchenLEDstripService service;

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
