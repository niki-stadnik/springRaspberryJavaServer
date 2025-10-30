package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.RebootDevice;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class LightSwitchService {

    private final RebootDevice rebootDevice;
    private final SimpMessageSendingOperations messaging;
    private final ApplicationEventPublisher eventPublisher;

    public static final boolean[] light = new boolean[8];
    private static boolean[] lightCommand = new boolean[8];
    private static boolean[] newLight = new boolean[8];
    private static final boolean[] commandFlag = new boolean[8];
    private static boolean active = false;
    private static boolean flag = false;
    private static boolean disregard = false;
    private static boolean flagBlock = false;
    private static boolean flagLoop = true;


    //@Scheduled(fixedRate = 500)
    void lightsControl() {
        //todo auto lights
        //flagManual[] is true when the light was turned on or off from the wall or the webapp
        //when flagManual[] is false that lamp can be controlled on auto
    }

    public void command(LightSwitchClientModel data) throws InterruptedException {
        if (flagLoop) {
            flagLoop = false;
            log.info("forced loop off");
        }
        log.info(data.toString());
        lightCommand = data.switchStateOf();
        newLight = data.stateLight();
        if (!flagBlock) {
            flagBlock = true;
            jobToDo();
            log.info("flagLoop: 3");
        }
    }


    private void jobToDo() throws InterruptedException {
        flagLoop = true;
        while (flagLoop) {
            flagLoop = false;
            for (int i = 0; i < 7; i++) {
                if (lightCommand[i]) {
                    if (newLight[i] == light[i]) {
                        lightCommand[i] = false;
                    } else {
                        flagLoop = true;
                    }
                }
            }
            if (flagLoop) {
                if (flag) {
                    flag = false;
                    disregard = true;
                    log.info("change state of: {}", Arrays.toString(lightCommand));
                    messaging.convertAndSend("/topic/lightSwitch", new LightSwitchCommandModel(lightCommand[0], lightCommand[1], lightCommand[2], lightCommand[3], lightCommand[4], lightCommand[5], lightCommand[6], false));
                    Thread.sleep(100);
                }
            }
            log.info("flagLoop: 1");
            Thread.sleep(10);
        }
        log.info("flagLoop: 2");
        flagBlock = false;
    }


    public void setData(LightSwitchModel data) {
        boolean[] newLight = new boolean[8];
        //log.info(data.toString());
        active = true;
        newLight[0] = data.isLight0();  //Спалня
        newLight[1] = data.isLight1();  //Баня
        newLight[2] = data.isLight2();  //Кухня
        newLight[3] = data.isLight3();  //Трапезария
        newLight[4] = data.isLight4();  //Хол
        newLight[5] = data.isLight5();  //Балкон
        newLight[6] = data.isLight6();  //Коридор
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


    @Scheduled(fixedRate = 10000)    //every 10s
    private synchronized void selfReboot(){
        if (!active) rebootDevice.rebootDev(RebootDevice.destination.LIGHT_SWITCH);
        active = false;
    }
}
