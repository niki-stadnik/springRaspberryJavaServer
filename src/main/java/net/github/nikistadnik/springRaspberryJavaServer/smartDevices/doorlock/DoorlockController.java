package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorlock;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class DoorlockController {

    private DoorlockService service;

    public DoorlockController(DoorlockService service) {
        this.service = service;
    }

    @MessageMapping("/doorlock")
    public void getData (DoorlockModel data) throws InterruptedException {
        Thread.sleep(50);
        service.setData(data);
        service.lockUpdate();
    }

    @MessageMapping("/clientDoorlock")
    public void getData(DoorlockClientModel data) throws InterruptedException {
        Thread.sleep(50);
        service.command(data);
    }

}
