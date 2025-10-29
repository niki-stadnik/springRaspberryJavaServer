package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.RebootDevice;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SendMessage;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorman.DoormanService;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenLEDstrip.kitchenLEDstripService;
import org.json.JSONObject;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LightSwitchService {

    private final RebootDevice rebootDevice;
    private final SimpMessageSendingOperations messaging;
    private final ApplicationEventPublisher eventPublisher;


    private static boolean active = false;
    private static boolean flag = false;
    private static boolean disregard = false;
    private static final boolean[] light = new boolean[8];
    private static final boolean[] lightCommand = new boolean[8];
    private static final boolean[] commandFlag = new boolean[8];



    //@Scheduled(fixedRate = 500)
    void lightsControl() {
        //todo auto lights
        //flagManual[] is true when the light was turned on or off from the wall or the webapp
        //when flagManual[] is false that lamp can be controlled on auto
    }

    public void command(LightSwitchClientModel data) throws InterruptedException {
        log.info(data.toString());
        int lightNum = data.getLight();
        lightCommand[lightNum] = data.isState();
        if(!commandFlag[lightNum]) {
            commandFlag[lightNum] = true;
            jobToDo(lightNum);
        }
    }

    private void jobToDo(int lightNum) throws InterruptedException {
        while (light[lightNum] != lightCommand[lightNum]){
            boolean[] pulse = new boolean[8];
            pulse[lightNum] = true;
            if (flag) {
                flag = false;
                disregard = true;
                messaging.convertAndSend("/topic/lightSwitch", new LightSwitchCommandModel(pulse[0], pulse[1], pulse[2], pulse[3], pulse[4], pulse[5], pulse[6], pulse[7]));
                log.info("change state");
            }
            Thread.sleep(100);
            log.info("jobtodo");
        }
        commandFlag[lightNum] = false;
    }


    public void setData(LightSwitchModel data) {
        boolean[] newLight = new boolean[8];
        //log.info(data.toString());
        active = true;
        newLight[0] = data.isLight0();
        newLight[1] = data.isLight1();
        newLight[2] = data.isLight2();
        newLight[3] = data.isLight3();
        newLight[4] = data.isLight4();
        newLight[5] = data.isLight5();
        newLight[6] = data.isLight6();
        newLight[7] = data.isLight7();
        //check for updated state
        for (int i = 0; i < 8; i++){
            if (light[i] != newLight[i]){
                light[i] = newLight[i];
                eventPublisher.publishEvent(new LightStatusChangedEvent(this, i, newLight[i]));
            }
        }
        if (!flag) {
            if (!disregard) {
                flag = true;
            } else disregard = false;
        }
    }

   /* public void Switch (boolean[] pulse) {
        if (flag) {
            flag = false;
            disregard = true;
            messaging.convertAndSend("/topic/lightSwitch", new LightSwitchCommandModel(pulse[0], pulse[1], pulse[2], pulse[3], pulse[4], pulse[5], pulse[6], pulse[7]));
            log.info("change state");
        }
    }*/



    @Scheduled(fixedRate = 10000)    //every 10s
    private synchronized void selfReboot(){
        if (!active) rebootDevice.rebootDev("rebootLightSwitch");
        active = false;
    }
}
