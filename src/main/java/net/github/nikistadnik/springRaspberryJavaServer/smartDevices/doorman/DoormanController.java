package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorman;

import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch.LightSwitchClientModel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class DoormanController {

    private DoormanService service;

    public DoormanController(DoormanService service) {this.service = service; }

    @MessageMapping("/doorman")
    @SendTo("/topic/doorman")
    public DoormanModel getData (DoormanModel data) throws InterruptedException {
        Thread.sleep(50);
        service.setData(data);
        return data;
    }

}
