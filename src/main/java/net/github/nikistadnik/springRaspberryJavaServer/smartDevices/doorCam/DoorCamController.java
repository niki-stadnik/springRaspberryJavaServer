package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorCam;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class DoorCamController {

    private DoorCamService service;

    public DoorCamController(DoorCamService service) {
        this.service = service;
    }

    @MessageMapping("/doorCam")
    @SendTo("/topic/imageDoorCam")
    public DoorCamClientModel getData(DoorCamModel data) throws IOException {
        //System.out.println(data);
        return service.setData(data);
    }
}
