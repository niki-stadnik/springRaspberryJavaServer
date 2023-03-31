package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.client;

import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomFan.BathroomFanModel;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomFan.BathroomFanService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ClientController {

    private ClientService service;

    public ClientController(ClientService clientService){
        this.service = clientService;
    }

    @MessageMapping("/client")
    public void getData(ClientModel data) throws InterruptedException {
        Thread.sleep(50);
        service.setData(data);
    }

}
