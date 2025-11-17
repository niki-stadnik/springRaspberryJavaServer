package net.github.nikistadnik.springRaspberryJavaServer.service;

import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SmartDevice;
import net.github.nikistadnik.springRaspberryJavaServer.model.LEDstripClientModel;
import net.github.nikistadnik.springRaspberryJavaServer.model.LEDstripCommamdModel;
import net.github.nikistadnik.springRaspberryJavaServer.model.LEDstripModel;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch.LightStatusChangedEvent;
import org.springframework.context.event.EventListener;


@Slf4j
public abstract class LEDstrip extends SmartDevice {

    private static int duty = 0;
    private static int newDuty = 0;
    private static int time = 0;

    protected String rebootTopicSecond;
    protected int lightToLookFor;

    public void setData (LEDstripModel data){
        active = true;
        duty = data.getDuty();
        if (duty != newDuty && (duty - newDuty > 2 || newDuty - duty > 2)){
            stripControl();
        }
        log.info(String.valueOf(data));
    }

    public void command (LEDstripClientModel data){
        int com = data.getCommand();
        if (com == 1) rebootDev(rebootTopic); //self
        if (com == 2) rebootDev(rebootTopicSecond); //second device
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
            messaging.convertAndSend("/topic/" + receiveTopic, new LEDstripCommamdModel(duty, newDuty, time));
            log.info("led strip change:" + duty + "->" + newDuty);
        }
    }


    //LightSwitchService calls when a change is detected in the kitchen light
    @EventListener
    public void handleLightStatusChangeEvent(LightStatusChangedEvent event) {
        if (event.getLight() == lightToLookFor) {
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
        messaging.convertAndSend("/topic/" + sendTopic, "{\"duty\":" + duty + "}");
    }
}
