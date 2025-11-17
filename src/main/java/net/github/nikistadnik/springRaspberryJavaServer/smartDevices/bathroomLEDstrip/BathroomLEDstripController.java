package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomLEDstrip;

import lombok.RequiredArgsConstructor;
import net.github.nikistadnik.springRaspberryJavaServer.model.LEDstripClientModel;
import net.github.nikistadnik.springRaspberryJavaServer.model.LEDstripModel;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenLEDstrip.kitchenLEDstripService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BathroomLEDstripController {

    private final BathroomLEDstripService service;

    @MessageMapping("/bathroomStrip")
    @SendTo("/topic/clientBathroomStrip")
    public LEDstripModel getData(LEDstripModel data) throws InterruptedException {
        Thread.sleep(50);
        service.setData(data);
        return data;
    }

    @MessageMapping("/clientBathroomStrip")
    public void getData(LEDstripClientModel data) throws InterruptedException {
        Thread.sleep(50);
        service.command(data);
    }
}
