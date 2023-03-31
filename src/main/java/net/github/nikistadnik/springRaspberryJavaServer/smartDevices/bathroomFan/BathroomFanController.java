package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomFan;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class BathroomFanController {
    private BathroomFanService service;

    public BathroomFanController(BathroomFanService bathroomFanService){
        this.service = bathroomFanService;
    }

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
