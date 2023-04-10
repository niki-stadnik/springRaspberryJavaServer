package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.adax;

import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomFan.BathroomFanModel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class AdaxAPIController {

    private AdaxAPI adaxAPI;

    public AdaxAPIController(AdaxAPI adaxAPI) {
        this.adaxAPI = adaxAPI;
    }

    @MessageMapping("/adax")
    public void getData(AdaxAPIModel data) throws InterruptedException {
        Thread.sleep(50);
        adaxAPI.command(data);
    }

    @MessageMapping("/adaxInit")
    public void init() throws InterruptedException {
        Thread.sleep(50);
        adaxAPI.getData();
    }
}
