package net.github.nikistadnik.springRaspberryJavaServer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.model.ClientModel;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SmartDevice;
import net.github.nikistadnik.springRaspberryJavaServer.model.LEDstripClientModel;
import net.github.nikistadnik.springRaspberryJavaServer.model.LEDstripCommamdModel;
import net.github.nikistadnik.springRaspberryJavaServer.model.LEDstripModel;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.herbPot.HerbPotModel;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch.LightStatusChangedEvent;
import org.springframework.context.event.EventListener;

import java.util.LinkedHashMap;


@Slf4j
public abstract class LEDstrip extends SmartDevice<LEDstripModel, LEDstripClientModel> {

    private int duty = 0;
    private int newDuty = 0;
    private int time = 0;

    protected int lightToLookFor;


    private void stripControl(){
        if (newDuty != duty){
            messaging.convertAndSend("/topic/" + deviceName, new LEDstripCommamdModel(duty, newDuty, time));
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
        messaging.convertAndSend("/topic/client/" + deviceName, "{\"duty\":" + duty + "}");
    }

    @Override
    protected Class provideDeviceModel() {
        return LEDstripModel.class;
    }

    @Override
    protected Class provideClientModel() {
        return LEDstripClientModel.class;
    }


    @Override
    protected void handleDeviceData(LEDstripModel data) {
        messaging.convertAndSend("/topic/client/" + deviceName, data);
        duty = data.duty();
        if (duty != newDuty && (duty - newDuty > 2 || newDuty - duty > 2)){
            stripControl();
        }
        log.info(String.valueOf(data));
    }

    @Override
    protected void handleClientData(LEDstripClientModel data) {
        int com = data.command();
        if (com == 1) rebootDev(deviceName); //self
        if (com == 2) rebootDev(pairName); //second device
        if (com == 3) reloadPage();
        if (com == 4) {
            newDuty = data.duty();
            time = data.time();
            stripControl();
        }
        log.info(data.toString());
    }
}
