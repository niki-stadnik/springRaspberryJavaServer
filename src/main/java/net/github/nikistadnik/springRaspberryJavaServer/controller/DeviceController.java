package net.github.nikistadnik.springRaspberryJavaServer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.service.DeviceRouterService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DeviceController {

    private final DeviceRouterService router;

    @MessageMapping("/device/{device}")
    public void handleDevice(@DestinationVariable String device, byte[] payload) throws Exception {
        router.routeDevice(device, payload);
        Thread.sleep(50);
    }

    @MessageMapping("/client/{device}")
    public void handleClient(@DestinationVariable String device, byte[] payload) throws Exception {
        router.routeClient(device, payload);
        Thread.sleep(50);
    }
}
