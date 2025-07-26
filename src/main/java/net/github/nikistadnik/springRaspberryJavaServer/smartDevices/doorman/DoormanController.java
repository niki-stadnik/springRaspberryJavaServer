package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorman;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class DoormanController {

    private DoormanService service;

    public DoormanController(DoormanService service) {this.service = service; }

    @MessageMapping("/doorman")
    @SendTo("/topic/doormanClient")
    public void getData (DoormanModel data) throws InterruptedException {
        Thread.sleep(50);
        service.setData(data);
    }

    @MessageMapping("/clientDoorman")
    public void getData(DoormanClientModel data) throws InterruptedException {
        Thread.sleep(50);
        service.command(data);
    }

}
