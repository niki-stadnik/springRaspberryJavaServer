package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorlock;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DoorlockController {

    private final DoorlockService service;

    @MessageMapping("/doorlock")
    public void getData (DoorlockModel data) throws InterruptedException {
        Thread.sleep(50);
        service.setData(data);
        service.lockUpdate();
        log.info("Doorlock data 1: {}", data);
    }

    @MessageMapping("/clientDoorlock")
    @SendTo("/topic/doorlock")
    public DoorlockClientModel getData(DoorlockClientModel data) throws InterruptedException {
        Thread.sleep(50);
        //service.command(data);
        log.info("Doorlock data 2: {}", data);
        return data;
    }

}
