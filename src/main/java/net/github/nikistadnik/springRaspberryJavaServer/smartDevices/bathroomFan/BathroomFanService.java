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

    @Getter
    @Setter
    private static boolean bathFanCommand = false;


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
        deviceRegistry.bathroomFanState().bathFan(data.isBathFan());
        double button = data.getButton();
        if (button != 0) {
            button /= 1000;
            deviceRegistry.bathroomFanState().button(button);
            try {
                buttonCheck();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }else deviceRegistry.bathroomFanState().button(0.0);
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
        deviceRegistry.bathroomFanState().auto(data.auto());
        if (!deviceRegistry.bathroomFanState().auto()) {
            bathFanCommand = data.bathFanCommand();
            if (!commandFlag) {
                commandFlag = true;
                try {
                    jobToDo();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    private synchronized void jobToDo() throws InterruptedException {
        CompletableFuture.runAsync(() -> {  //if the code is not in a new thread, the loop, for some reason, blocks the full stomp com
            while (deviceRegistry.bathroomFanState().bathFan() != bathFanCommand) {
                switchState(bathFanCommand);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            commandFlag = false;
        });
    }


    private void sendToClient() {
        messaging.convertAndSend("/topic/client/bathroomFan", new BathroomFanToClientModel(
                deviceRegistry.bathroomFanState().auto(),
                deviceRegistry.bathroomFanState().bathTemp1(),
                deviceRegistry.bathroomFanState().bathTemp2(),
                deviceRegistry.bathroomFanState().bathHum1(),
                deviceRegistry.bathroomFanState().bathHum2(),
                deviceRegistry.bathroomFanState().bathFan()));
    }

    void buttonCheck() throws InterruptedException {
        if (deviceRegistry.bathroomFanState().button() != null && deviceRegistry.bathroomFanState().button() != 0) {
            if (deviceRegistry.bathroomFanState().button() < 2) {
                if (deviceRegistry.bathroomFanState().auto()) {
                    deviceRegistry.bathroomFanState().auto(false);
                    log.info("button auto off, fan off");
                    bathFanCommand = false;
                    if (!commandFlag) {
                        commandFlag = true;
                        jobToDo();
                    }
                }else {
                    deviceRegistry.bathroomFanState().auto(true);
                    log.info("button auto on, fan on");
                    bathFanCommand = true;
                    if (!commandFlag) {
                        commandFlag = true;
                        jobToDo();
                    }
                }
            } else {
                deviceRegistry.bathroomFanState().auto(false);
                log.info("button auto off, fan on (hold)");
                bathFanCommand = true;
                if (!commandFlag) {
                    commandFlag = true;
                    jobToDo();
                }
            }
        }
    }


    @Scheduled(fixedRate = 1000)    //500
    private void Auto() {
        if (deviceRegistry.bathroomFanState().auto()) {
            bathroomFanCycles = bathroomFanDelay;   //it's *2 because the schedule is 2 times per second
            if (deviceRegistry.bathroomFanState().bathHum2() != null && deviceRegistry.bathroomFanState().bathTemp2() != null) {
                if ((deviceRegistry.bathroomFanState().bathHum2() > 70 || deviceRegistry.lightSwitchState().light()[1].get()) && !deviceRegistry.bathroomFanState().bathFan()) {
                    switchState(true);
                } else if (deviceRegistry.bathroomFanState().bathHum2() < 55 && !deviceRegistry.lightSwitchState().light()[1].get() && deviceRegistry.bathroomFanState().bathFan()) {
                    log.info("counter/cycles: {} / {}", counterForBathroomFan , bathroomFanCycles);
                    if (counterForBathroomFan >= bathroomFanCycles) {
                        switchState(false);
                    } else {
                        counterForBathroomFan++;
                    }
                } else {
                    if (deviceRegistry.lightSwitchState().light()[1].get()) counterForBathroomFan = 0;
                }
            }
        }
    }

    public synchronized void switchState(boolean state) {
        if (flag) {
            flag = false;
            disregard = true;
            messaging.convertAndSend("/topic/bathroomFan", "{\"data\":" + state + "}");
            log.info("change state: {}", state);
        }
    }
}
