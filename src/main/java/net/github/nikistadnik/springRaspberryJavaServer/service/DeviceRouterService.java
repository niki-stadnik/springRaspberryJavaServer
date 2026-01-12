package net.github.nikistadnik.springRaspberryJavaServer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DeviceRouterService {

    private final Map<String, DeviceService> devices;

    public DeviceRouterService(List<DeviceService> deviceServices) {
        devices = deviceServices.stream().collect(Collectors.toMap(DeviceService::getName, s -> s));
    }

    public void routeDevice(String deviceName, byte[] payload) {
        devices.get(deviceName).handle(payload);
    }

    public void routeClient(String deviceName, byte[] payload) {
        devices.get(deviceName).handleCommand(payload);
    }
}
