package net.github.nikistadnik.springRaspberryJavaServer.controller;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.RebootDevice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reboot")
@RequiredArgsConstructor
@Slf4j
public class RebootController {

    private final RebootDevice rebootDevice;

    @PostMapping("/monitoring/{device}")
    public ResponseEntity<Void> toggleMonitoring(@RequestBody boolean monitor, @PathVariable String device){
        //log.info("Toggle rebooting for device: {} to: {}", device, monitor);
        rebootDevice.toggleMonitoring(device, monitor);
        return ResponseEntity.ok().build(); // HTTP 200
    }

    @PostMapping("/rebooting/{device}")
    public ResponseEntity<Void> reboot(@PathVariable String device){
        //log.info("Rebooting device: {}", device);
        rebootDevice.reboot(device);
        return ResponseEntity.ok().build(); // HTTP 200
    }

    @GetMapping("/monitoring")
    public JsonNode getMonitoringStatus(){
        return rebootDevice.monitoringStatus();
    }
}
