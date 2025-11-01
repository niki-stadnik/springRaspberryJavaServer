package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenESP2;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class kitchenESP2Controller {

    private final kitchenESP2Service service;

    @MessageMapping("/kitchenESP2")
    public void getData(kitchenESP2Model data) throws InterruptedException {
        Thread.sleep(50);
        service.setData(data);
    }
}
