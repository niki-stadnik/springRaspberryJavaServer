package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.adax;

import lombok.RequiredArgsConstructor;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomFan.BathroomFanModel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AdaxAPIController {

    private final AdaxAPI adaxAPI;

    @MessageMapping("/adax")
    public void getData(AdaxAPIModel data) throws InterruptedException {
        Thread.sleep(50);
        adaxAPI.command(data);
    }

    @MessageMapping("/adaxInit")
    public void init() throws InterruptedException {
        Thread.sleep(50);
        adaxAPI.updateClient();
    }
}
