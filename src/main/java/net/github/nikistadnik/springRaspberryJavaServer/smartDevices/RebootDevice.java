package net.github.nikistadnik.springRaspberryJavaServer.smartDevices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.model.SmartDeviceModel;
import net.github.nikistadnik.springRaspberryJavaServer.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RebootDevice {

    @Autowired
    protected SimpMessageSendingOperations messaging;

    private final Map<String, DeviceService> devices;

    public RebootDevice(List<DeviceService> deviceServices) {
        devices = deviceServices.stream().collect(Collectors.toMap(DeviceService::getName, s -> s));
    }

    public void toggleMonitoring(String name, boolean monitor){
        devices.get(name).setRebootMonitor(monitor);
        log.info("reboot monitor for: {} , is now: {}", name, monitor);
    }

    public void reboot(String name){
        log.info("rebooting from UI: {}", name);
        devices.get(name).rebootDev(name);
        devices.get(name).selfRebootDev(name);
    }

    ObjectMapper mapper = new ObjectMapper();
    ObjectNode jsonNode = mapper.createObjectNode();

    @Scheduled(fixedRate = 1000)
    private synchronized void statusUpdate() {
        for (DeviceService device : devices.values()) {
            jsonNode.put(device.getName(), device.getState());
        }
        messaging.convertAndSend("/topic/state", jsonNode);
        //log.info(data);
    }

    public JsonNode monitoringStatus(){
        ObjectNode jsonNode = mapper.createObjectNode();
        for (DeviceService device : devices.values()) {
            jsonNode.put(device.getName(), device.getRebootMonitor());
        }
        return jsonNode;
    }

    @Scheduled(initialDelay = 10000, fixedRate = 30000)    //every 30s
    protected synchronized void autoReboot(){
        for (DeviceService device : devices.values()) {
            if (device.getRebootMonitor()) {
                if (!device.getActive()) {
                    device.setState(false);
                    device.selfRebootDev(device.getName());
                    device.rebootDev(device.getName());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                device.setActive(false);
            }
        }
    }
}


//todo send reboot monitoring status to db repository, so that it is remembered after power out