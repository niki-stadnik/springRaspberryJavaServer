package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightDimmers;
/*
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LightDimmersController {

    private final LightDimmersService service;

    @MessageMapping("/lightDimmer")
    @SendTo("/topic/dimmersClient")
    public void getData(LightDimmerModel data) throws InterruptedException {
        Thread.sleep(50);
        service.setData(data);
    }

    @MessageMapping("/client/lightDimmer")
    public void getClientData(LightDimmerModel data) throws InterruptedException {
        Thread.sleep(50);
        service.command(data);
    }

}

 */
