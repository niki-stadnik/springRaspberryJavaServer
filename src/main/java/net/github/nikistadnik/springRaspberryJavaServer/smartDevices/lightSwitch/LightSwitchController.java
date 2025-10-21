package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class LightSwitchController {

    private LightSwitchService service;

    public LightSwitchController(LightSwitchService service){this.service = service; }

    @MessageMapping("/lightSwitch")
    @SendTo("/topic/lightsClient")
    public LightSwitchModel getData(LightSwitchModel data) throws InterruptedException {
        Thread.sleep(50);
        service.setData(data);
        return data;
    }

    @MessageMapping("/clientLightSwitch")
    public void getData(LightSwitchClientModel data) throws InterruptedException {
        Thread.sleep(50);
        service.command(data);
    }

}
