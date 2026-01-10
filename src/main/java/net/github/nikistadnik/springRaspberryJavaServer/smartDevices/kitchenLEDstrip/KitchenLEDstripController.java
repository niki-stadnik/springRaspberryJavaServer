package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenLEDstrip;
/*
import lombok.RequiredArgsConstructor;
import net.github.nikistadnik.springRaspberryJavaServer.model.LEDstripClientModel;
import net.github.nikistadnik.springRaspberryJavaServer.model.LEDstripModel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class KitchenLEDstripController {

    private final KitchenLEDstripService service;

    @MessageMapping("/kitchenStrip")
    @SendTo("/topic/clientKitchenStrip")
    public LEDstripModel getData(LEDstripModel data) throws InterruptedException {
        Thread.sleep(50);
        service.setData(data);
        return data;
    }

    @MessageMapping("/clientKitchenStrip")
    public void getData(LEDstripClientModel data) throws InterruptedException {
        Thread.sleep(50);
        service.command(data);
    }
}

 */
