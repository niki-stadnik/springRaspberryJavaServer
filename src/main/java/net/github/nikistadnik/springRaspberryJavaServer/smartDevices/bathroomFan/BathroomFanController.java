package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomFan;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BathroomFanController {

    private final BathroomFanService service;

    @MessageMapping("/bathroomFan")
    public void getData(BathroomFanModel data) throws InterruptedException {
        Thread.sleep(50);
        service.setData(data);
    }

    @MessageMapping("/clientBathroomFan")
    public void getData(BathroomFanClientModel data) throws InterruptedException {
        Thread.sleep(50);
        service.command(data);
    }
}
