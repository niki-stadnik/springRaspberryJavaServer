package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomFan;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.TempStorage;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SendMessage;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch.LightSwitchCommandModel;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch.LightSwitchService;
import org.json.JSONObject;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BathroomFanService {

    private final SimpMessageSendingOperations messaging;

    private static boolean flag = false;
    private static boolean disregard = false;
    private static boolean check = false;
    private static boolean auto = true;
    @Getter @Setter
    private static boolean bathFanCommand = false;

    @Getter @Setter
    private static Double bathTemp1 = null;
    @Getter @Setter
    private static Double bathTemp2 = null;
    @Getter @Setter
    private static Double bathHum1 = null;
    @Getter @Setter
    private static Double bathHum2 = null;
    @Getter @Setter
    private static boolean bathFan = false;

    private static boolean commandFlag = false;
    @Getter @Setter
    private static int bathroomFanDelay = 30;
    private static int counterForBathroomFan = 0;
    private static int bathroomFanCycles = 0;
    JSONObject jo;



    private void sendToClient(){
        messaging.convertAndSend("/topic/clientBathroomFan", new BathroomFanModel(auto, bathTemp1, bathTemp2, bathHum1, bathHum2, bathFan));
    }

    public void command(BathroomFanClientModel data) throws InterruptedException {
        log.info(data.toString());
        auto = data.isAuto();
        if (!auto) {
            bathFanCommand = data.isBathFanCommand();
            if(!commandFlag){
                commandFlag = true;
                jobToDo();
            }
        }
    }

    private void jobToDo() throws InterruptedException {
        while (bathFan != bathFanCommand){
            if (bathFanCommand) {
                switchState(true);
            } else {
                switchState(false);
            }
            Thread.sleep(2000);
        }
        commandFlag = false;
    }


    public void setData(BathroomFanModel data) {
        //System.out.println(data);
        bathTemp1 = data.getBathTemp1();
        //bathTemp -= 2;    //calibrating
        int tem = (int)(bathTemp1*100);
        bathTemp1 = tem/100d;
        //System.out.println(bathTemp);
        bathHum1 = data.getBathHum1();
        //bathHum += 10;      //calibrating
        int hum = (int)(bathHum1*100);
        bathHum1 = hum/100d;
        //System.out.println(bathHum);
        bathFan = data.isBathFan();
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
        if (auto) {
            bathroomFanCycles = bathroomFanDelay;   //it's *2 because the schedule is 2 times per second
            if (bathHum1 != null && bathTemp1 != null) {
                if ((bathHum1 > 70 || LightSwitchService.light[1]) && !bathFan) {
                    switchState(true);
                } else if (bathHum1 < 54 && !LightSwitchService.light[1] && bathFan) {
                    System.out.println(counterForBathroomFan);
                    System.out.println(bathroomFanCycles);
                    if (counterForBathroomFan >= bathroomFanCycles) {
                        switchState(false);
                    } else {
                        counterForBathroomFan++;
                    }
                } else {
                    if (LightSwitchService.light[1]) counterForBathroomFan = 0;
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
