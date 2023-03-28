package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch;

import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomFan.BathroomFanModel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class LightSwitchController {

    private LightSwitchService service;

    public LightSwitchController(LightSwitchService service){this.service = service; }

    @MessageMapping("/lightSwitch")
    public void getData(LightSwitchModel data) throws InterruptedException {
        Thread.sleep(50);
        service.setData(data);
    }

}
