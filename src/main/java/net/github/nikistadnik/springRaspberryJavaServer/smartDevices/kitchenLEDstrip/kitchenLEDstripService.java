package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenLEDstrip;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.RebootDevice;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch.LightStatusChangedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class kitchenLEDstripService {

    private final RebootDevice rebootDevice;
    private final SimpMessageSendingOperations messaging;

    private static boolean active = false;

    private static int duty = 0;
    private static int newDuty = 0;
    private static int time = 0;

    public void setData (kitchenLEDstripModel data){
        active = true;
        duty = data.getDuty();
        if (duty != newDuty && (duty - newDuty > 2 || newDuty - duty > 2)){
            stripControl();
        }
        //log.info(String.valueOf(data));
    }

    public void command (kitchenLEDstripClientModel data){
        int com = data.getCommand();
        if (com == 1) rebootDevice.rebootDev(RebootDevice.destination.LED_KITCHEN); //self
        if (com == 2) rebootDevice.rebootDev(RebootDevice.destination.KITCHEN); //kitchenESP32Service
        if (com == 3) reloadPage();
        if (com == 4) {
            newDuty = data.getDuty();
            time = data.getTime();
            stripControl();
        }
        log.info(data.toString());
    }

    private void stripControl(){
        if (newDuty != duty){
            messaging.convertAndSend("/topic/kitchenStrip", new kitchenLEDstripCommamdModel(duty, newDuty, time));
            log.info("led strip change:" + duty + "->" + newDuty);
        }
    }

    //LightSwitchService calls when a change is detected in the kitchen light
    @EventListener
    public void handleLightStatusChangeEvent(LightStatusChangedEvent event) {
        if (event.getLight() == 2) {
            log.info("event:" + event);
            boolean state = event.isState();
            if (state){
                newDuty = 255;
                time = 10;
                stripControl();
            }else{
                newDuty = 0;
                time = 10;
                stripControl();
            }
        }
    }


    private void reloadPage(){
        messaging.convertAndSend("/topic/clientKitchenStrip", "{\"duty\":" + duty + "}");
    }

    @Scheduled(initialDelay = 10000, fixedRate = 30000)    //every 30s
    private synchronized void selfReboot(){
        if (!active) rebootDevice.rebootDev(RebootDevice.destination.LED_KITCHEN);
        active = false;
    }
}
