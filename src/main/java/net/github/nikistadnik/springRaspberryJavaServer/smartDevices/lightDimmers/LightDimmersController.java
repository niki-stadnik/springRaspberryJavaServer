package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightDimmers;

import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch.LightSwitchClientModel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class LightDimmersController {

    private LightDimmersService service;

    public LightDimmersController(LightDimmersService service) {
        this.service = service;
    }

    @MessageMapping("/lightDimmer")
    @SendTo("/topic/dimmersClient")
    public LightDimmerModel getData(LightDimmerModel data) throws InterruptedException {
        Thread.sleep(50);
        service.setData(data);
        return data;
    }

    @MessageMapping("/clientLightDimmer")
    public void getClientData(LightDimmerModel data) throws InterruptedException {
        Thread.sleep(50);
        service.command(data);
    }

}
