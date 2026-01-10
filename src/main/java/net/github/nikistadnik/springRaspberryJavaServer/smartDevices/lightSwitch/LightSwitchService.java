package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch;

import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SmartDevice;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class LightSwitchService extends SmartDevice<LightSwitchModel, LightSwitchClientModel> {

    private static boolean flag = false;
    private static boolean disregard = false;


    @Override
    protected String provideName() {
        return "lightSwitch";
    }

    @Override
    protected String pairedWithName() {
        return "doorman";
    }

    @Override
    protected Class provideDeviceModel() {
        return LightSwitchModel.class;
    }

    @Override
    protected Class provideClientModel() {
        return LightSwitchClientModel.class;
    }

    @Override
    protected void handleDeviceData(LightSwitchModel data) {
        messaging.convertAndSend("/topic/lightsClient", data);
        boolean[] newLight = new boolean[8];
        //log.info(data.toString());
        newLight[0] = data.isLight0();  //Спалня
        newLight[1] = data.isLight1();  //Баня
        newLight[2] = data.isLight2();  //Кухня
        newLight[3] = data.isLight3();  //Трапезария
        newLight[4] = data.isLight4();  //Хол
        newLight[5] = data.isLight5();  //Балкон
        newLight[6] = data.isLight6();  //Коридор
        newLight[7] = data.isLight7();
        //check for updated state
        for (int i = 0; i < 8; i++) {
            //if (light[i] != newLight[i]){
            if (deviceRegistry.lightSwitchState().light()[i].get() != newLight[i]) {
                //light[i] = newLight[i];
                deviceRegistry.lightSwitchState().light()[i].set(newLight[i]);
                eventPublisher.publishEvent(new LightStatusChangedEvent(this, i, newLight[i]));
                log.info("light change: {} -> {}", i, newLight[i]);
            }
        }
        if (!flag) {
            if (!disregard) {
                flag = true;
            } else disregard = false;
        }
    }

    @Override
    protected void handleClientData(LightSwitchClientModel data) {
        log.info(data.toString());
        long start = System.currentTimeMillis();
        boolean[] lightCommand = data.switchStateOf();
        boolean[] newLight = data.stateLight();
        CompletableFuture.runAsync(() -> {
            boolean flagLoop = true;
            while (flagLoop) {
                flagLoop = false;
                for (int i = 0; i < 7; i++) {
                    if (lightCommand[i]) {
                        //if (newLight[i] == light[i]) {
                        if (newLight[i] == deviceRegistry.lightSwitchState().light()[i].get()) {
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
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                //log.info("flagLoop: 1");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (System.currentTimeMillis() - start > 5000) {
                    flagLoop = false;
                    log.info("light command timeout");
                }
            }
            //log.info("flagLoop: 2");
        });
    }

    //@Scheduled(fixedRate = 500)
    void lightsControl() {
        //todo auto lights
        //flagManual[] is true when the light was turned on or off from the wall or the webapp
        //when flagManual[] is false that lamp can be controlled on auto

    }
}
