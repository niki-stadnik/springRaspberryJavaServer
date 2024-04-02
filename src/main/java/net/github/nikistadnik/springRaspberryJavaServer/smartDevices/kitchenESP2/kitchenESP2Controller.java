package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenESP2;

import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenLEDstrip.kitchenLEDstripModel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class kitchenESP2Controller {

    private kitchenESP2Service service;

    public kitchenESP2Controller(kitchenESP2Service service) {
        this.service = service;
    }

    @MessageMapping("/kitchenESP2")
    public void getData(kitchenESP2Model data) throws InterruptedException {
        Thread.sleep(50);
        service.setData(data);
    }
}
