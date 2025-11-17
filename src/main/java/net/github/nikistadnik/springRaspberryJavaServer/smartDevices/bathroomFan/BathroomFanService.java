package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomFan;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.DeviceRegistry;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.RebootDevice;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class BathroomFanService {

    private final SimpMessageSendingOperations messaging;
    private final DeviceRegistry deviceRegistry;
    private final RebootDevice rebootDevice;

    private static boolean active = false;

    private static boolean flag = false;
    private static boolean disregard = false;
    private static boolean check = false;

    @Getter
    @Setter
    private static boolean bathFanCommand = false;


    private static boolean commandFlag = false;
    @Getter
    @Setter
    private static int bathroomFanDelay = 30;
    private static int counterForBathroomFan = 0;
    private static int bathroomFanCycles = 0;


    private void sendToClient() {
        messaging.convertAndSend("/topic/clientBathroomFan", new BathroomFanToClientModel(
                deviceRegistry.bathroomFanState().auto(),
                deviceRegistry.bathroomFanState().bathTemp1(),
                deviceRegistry.bathroomFanState().bathTemp2(),
                deviceRegistry.bathroomFanState().bathHum1(),
                deviceRegistry.bathroomFanState().bathHum2(),
                deviceRegistry.bathroomFanState().bathFan()));
    }

    public void command(BathroomFanClientModel data) throws InterruptedException {
        log.info(data.toString());
        deviceRegistry.bathroomFanState().auto(data.auto());
        if (!deviceRegistry.bathroomFanState().auto()) {
            bathFanCommand = data.bathFanCommand();
            if (!commandFlag) {
                commandFlag = true;
                jobToDo();
            }
        }
    }

    private synchronized void jobToDo() throws InterruptedException {
        CompletableFuture.runAsync(() -> {  //if the code is not in a new thread, the loop, for some reason, blocks the full stomp com
            while (deviceRegistry.bathroomFanState().bathFan() != bathFanCommand) {
                switchState(bathFanCommand);
                /*if (bathFanCommand) {
                    switchState(true);
                } else {
                    switchState(false);
                }*/
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            commandFlag = false;
        });
    }


    public synchronized void setData(BathroomFanModel data) {
        active = true;
        //log.info(data.toString());

        //Double bathTemp1l = data.getBathTemp1();
        //bathTemp -= 2;    //calibrating
        //int tem1 = (int) (bathTemp1l * 100);
        //deviceRegistry.bathroomFanState().bathTemp1(tem1 / 100d);
        deviceRegistry.bathroomFanState().bathTemp1(data.getBathTemp1());

        //Double bathTemp2l = data.getBathTemp1();
        //bathTemp -= 2;    //calibrating
        //int tem2 = (int) (bathTemp2l * 100);
        //deviceRegistry.bathroomFanState().bathTemp1(tem2 / 100d);
        deviceRegistry.bathroomFanState().bathTemp2(data.getBathTemp2());

        //Double bathHum1l = data.getBathHum1();
        //bathHum += 10;      //calibrating
        //int hum1 = (int) (bathHum1l * 100);
        //deviceRegistry.bathroomFanState().bathHum1(hum1 / 100d);
        deviceRegistry.bathroomFanState().bathHum1(data.getBathHum1());

        //Double bathHum2l = data.getBathHum1();
        //bathHum += 10;      //calibrating
        //int hum2 = (int) (bathHum2l * 100);
        //deviceRegistry.bathroomFanState().bathHum1(hum2 / 100d);
        deviceRegistry.bathroomFanState().bathHum2(data.getBathHum2());

        deviceRegistry.bathroomFanState().bathFan(data.isBathFan());
        double button = data.getButton();
        if (button != 0) {
            button /= 1000;
        }
        //log.info("button: {}", button);
        deviceRegistry.bathroomFanState().button(button);
        if (!flag) {                //check if message is sent
            if (!disregard) {       //disregard the next batch of data since it can be sent before the message
                flag = true;        //if disregarded allow the next message to be sent
            } else {
                disregard = false;
            }
        }
        sendToClient();
    }


    @Scheduled(fixedRate = 1000)    //500
    private void Auto() {
        if (deviceRegistry.bathroomFanState().auto()) {
            bathroomFanCycles = bathroomFanDelay;   //it's *2 because the schedule is 2 times per second
            if (deviceRegistry.bathroomFanState().bathHum1() != null && deviceRegistry.bathroomFanState().bathTemp1() != null) {
                //if ((bathHum1 > 70 || LightSwitchService.light[1]) && !bathFan) {
                if ((deviceRegistry.bathroomFanState().bathHum1() > 70 || deviceRegistry.lightSwitchState().light()[1].get()) && !deviceRegistry.bathroomFanState().bathFan()) {
                    switchState(true);
                    // } else if (bathHum1 < 54 && !LightSwitchService.light[1] && bathFan) {
                } else if (deviceRegistry.bathroomFanState().bathHum1() < 54 && !deviceRegistry.lightSwitchState().light()[1].get() && deviceRegistry.bathroomFanState().bathFan()) {
                    System.out.println(counterForBathroomFan);
                    System.out.println(bathroomFanCycles);
                    if (counterForBathroomFan >= bathroomFanCycles) {
                        switchState(false);
                    } else {
                        counterForBathroomFan++;
                    }
                } else {
                    //   if (LightSwitchService.light[1]) counterForBathroomFan = 0;
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

    @Scheduled(initialDelay = 10000, fixedRate = 30000)    //every 30s
    private synchronized void selfReboot(){
        if (!active) rebootDevice.rebootDev(RebootDevice.destination.FAN_BATHROOM);
        active = false;
    }
}
