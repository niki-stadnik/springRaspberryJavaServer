package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomFan;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SmartDevice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class BathroomFanService extends SmartDevice<BathroomFanModel, BathroomFanClientModel> {

    private static boolean flag = false;
    private static boolean disregard = false;

    private static boolean commandFlag = false;
    @Getter
    @Setter
    private static int bathroomFanDelay = 60;
    private static int counterForBathroomFan = 0;
    private static int bathroomFanCycles = 0;


    @Override
    protected String provideName() {
        return "bathroomFan";
    }

    @Override
    protected String pairedWithName() {
        return "bathroomStrip";
    }

    @Override
    protected Class<BathroomFanModel> provideDeviceModel() {
        return BathroomFanModel.class;
    }

    @Override
    protected Class provideClientModel() {
        return BathroomFanClientModel.class;
    }

    @Override
    protected void handleDeviceData(BathroomFanModel data) {
        deviceRegistry.bathroomFanState().bathTemp1(data.getBathTemp1());
        deviceRegistry.bathroomFanState().bathTemp2(data.getBathTemp2());
        deviceRegistry.bathroomFanState().bathHum1(data.getBathHum1());
        deviceRegistry.bathroomFanState().bathHum2(data.getBathHum2());
        deviceRegistry.bathroomFanState().bathFan1(data.isBathFan());
        deviceRegistry.bathroomFanState().bathFan2(data.isBathFan2());
        double button = data.getButton();
        if (button != 0) {
            button /= 1000;
            deviceRegistry.bathroomFanState().button(button);
            try {
                buttonCheck();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else deviceRegistry.bathroomFanState().button(0.0);
        //log.info("button: {}", button);
        if (!flag) {                //check if message is sent
            if (!disregard) {       //disregard the next batch of data since it can be sent before the message
                flag = true;        //if disregarded allow the next message to be sent
            } else {
                disregard = false;
            }
        }
        sendToClient();
    }

    @Override
    protected void handleClientData(BathroomFanClientModel data) {
        log.info(data.toString());
        if (data.maxHum1() != 0) deviceRegistry.bathroomFanState().maxHum1(data.maxHum1());
        if (data.maxHum2() != 0) deviceRegistry.bathroomFanState().maxHum2(data.maxHum2());
        if (data.minHum1() != 0) deviceRegistry.bathroomFanState().minHum1(data.minHum1());
        if (data.minHum2() != 0) deviceRegistry.bathroomFanState().minHum2(data.minHum2());
        for (int i = 0; i < 2; i++) {
            if (data.modeFan()[i] != 0) { //0 = no action
                switch (data.modeFan()[i]) {
                    case 1:               //1 = off
                        deviceRegistry.bathroomFanState().requestedFanState()[i] = false;
                        if (i == 0) deviceRegistry.bathroomFanState().auto1(false);
                        else deviceRegistry.bathroomFanState().auto2(false);
                        if (!commandFlag) {
                            commandFlag = true;
                            equalizer();
                        }
                        break;
                    case 2:             //2 = on
                        deviceRegistry.bathroomFanState().requestedFanState()[i] = true;
                        if (i == 0) deviceRegistry.bathroomFanState().auto1(false);
                        else deviceRegistry.bathroomFanState().auto2(false);
                        if (!commandFlag) {
                            commandFlag = true;
                            equalizer();
                        }
                        break;
                    case 3:             //3 = auto
                        if (i == 0) deviceRegistry.bathroomFanState().auto1(true);
                        else deviceRegistry.bathroomFanState().auto2(true);
                        break;
                }
            }
        }
    }


    private void sendToClient() {
        messaging.convertAndSend("/topic/client/bathroomFan", new BathroomFanToClientModel(
                deviceRegistry.bathroomFanState().auto1(),
                deviceRegistry.bathroomFanState().auto2(),
                deviceRegistry.bathroomFanState().bathTemp1(),
                deviceRegistry.bathroomFanState().bathTemp2(),
                deviceRegistry.bathroomFanState().bathHum1(),
                deviceRegistry.bathroomFanState().bathHum2(),
                deviceRegistry.bathroomFanState().bathFan1(),
                deviceRegistry.bathroomFanState().bathFan2(),
                deviceRegistry.bathroomFanState().minHum1(),
                deviceRegistry.bathroomFanState().maxHum1(),
                deviceRegistry.bathroomFanState().minHum2(),
                deviceRegistry.bathroomFanState().maxHum2()
        ));
    }

    void buttonCheck() throws InterruptedException {
        if (deviceRegistry.bathroomFanState().button() != null && deviceRegistry.bathroomFanState().button() != 0) {
            if (deviceRegistry.bathroomFanState().button() < 2) {
                if (deviceRegistry.bathroomFanState().auto1()) {
                    log.info("button auto off, fan off");
                    deviceRegistry.bathroomFanState().auto1(false);
                    deviceRegistry.bathroomFanState().requestedFanState()[0] = false;
                    if (!commandFlag) {
                        commandFlag = true;
                        equalizer();
                    }
                } else {
                    log.info("button auto on, fan on");
                    deviceRegistry.bathroomFanState().auto1(true);
                    deviceRegistry.bathroomFanState().requestedFanState()[0] = true;
                    if (!commandFlag) {
                        commandFlag = true;
                        equalizer();
                    }
                }
            } else {
                log.info("button auto off, fan on (hold)");
                deviceRegistry.bathroomFanState().auto1(false);
                deviceRegistry.bathroomFanState().requestedFanState()[0] = true;
                if (!commandFlag) {
                    commandFlag = true;
                    equalizer();
                }
            }
        }
    }


    @Scheduled(fixedRate = 1000)    //500
    private void Auto() {
        if (deviceRegistry.bathroomFanState().auto1()) {
            bathroomFanCycles = bathroomFanDelay;   //it's *2 because the schedule is 2 times per second
            if (deviceRegistry.bathroomFanState().bathHum2() != null && deviceRegistry.bathroomFanState().bathTemp2() != null) {
                if ((deviceRegistry.bathroomFanState().bathHum2() > deviceRegistry.bathroomFanState().maxHum1() || deviceRegistry.lightSwitchState().light()[1].get()) && !deviceRegistry.bathroomFanState().bathFan1()) {
                    deviceRegistry.bathroomFanState().requestedFanState()[0] = true;
                    equalizer();
                } else if (deviceRegistry.bathroomFanState().bathHum2() < deviceRegistry.bathroomFanState().minHum1() && !deviceRegistry.lightSwitchState().light()[1].get() && deviceRegistry.bathroomFanState().bathFan1()) {
                    log.info("counter/cycles: {} / {}", counterForBathroomFan, bathroomFanCycles);
                    if (counterForBathroomFan >= bathroomFanCycles) {
                        deviceRegistry.bathroomFanState().requestedFanState()[0] = false;
                        equalizer();
                    } else {
                        counterForBathroomFan++;
                    }
                } else {
                    if (deviceRegistry.lightSwitchState().light()[1].get()) counterForBathroomFan = 0;
                }
            }
        }
        if (deviceRegistry.bathroomFanState().auto2()) {
            if (deviceRegistry.bathroomFanState().bathHum2() != null) {
                if (deviceRegistry.bathroomFanState().bathHum2() > deviceRegistry.bathroomFanState().maxHum2()) {
                    deviceRegistry.bathroomFanState().requestedFanState()[1] = true;
                    equalizer();
                }else if (deviceRegistry.bathroomFanState().bathHum2() < deviceRegistry.bathroomFanState().minHum2()) {
                    deviceRegistry.bathroomFanState().requestedFanState()[1] = false;
                    equalizer();
                }
            }
        }
    }


    public synchronized void equalizer() {
        CompletableFuture.runAsync(() -> {  //if the code is not in a new thread, the loop, for some reason, blocks the full stomp com
            while (deviceRegistry.bathroomFanState().bathFan1() != deviceRegistry.bathroomFanState().requestedFanState()[0]
                    || deviceRegistry.bathroomFanState().bathFan2() != deviceRegistry.bathroomFanState().requestedFanState()[1]) {
                if (flag) {
                    flag = false;
                    disregard = true;
                    boolean state1 = deviceRegistry.bathroomFanState().requestedFanState()[0];
                    boolean state2 = deviceRegistry.bathroomFanState().requestedFanState()[1];
                    messaging.convertAndSend("/topic/bathroomFan", "{\"fan1\":" + state1 + "," + "\"fan2\":" + state2 + "}");
                    log.info("change state: fan1:{}, fan2:{}", state1, state2);
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            commandFlag = false;
        });
    }
}
