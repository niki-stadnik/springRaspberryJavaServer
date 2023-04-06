package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomFan;

import net.github.nikistadnik.springRaspberryJavaServer.TempStorage;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SendMessage;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BathroomFanService {

    private static boolean flag = false;
    private static boolean disregard = false;
    private static boolean check = false;
    private static boolean auto = true;
    private static boolean bathFanCommand = false;

    private static Double bathTemp = null;
    private static Double bathHum = null;
    private static Double bathLight = null;
    private static boolean bathFan = false;

    private static boolean commandFlag = false;
    private static int bathroomFanDelay = 30;
    private static int counterForBathroomFan = 0;
    private static int bathroomFanCycles = 0;
    JSONObject jo;

    public BathroomFanService() {
    }


    public void command(BathroomFanClientModel data) throws InterruptedException {
        System.out.println(data);
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
                switchON();
            } else {
                switchOFF();
            }
            Thread.sleep(2000);
        }
        commandFlag = false;
    }


    public void setData(BathroomFanModel data) {
        //System.out.println(data);
        bathTemp = data.getBathTemp();
        bathTemp -= 2;    //calibrating
        int tem = (int)(bathTemp*100);
        bathTemp = tem/100d;
        //System.out.println(bathTemp);
        TempStorage.mapStorage.put("bathTemp", bathTemp);
        bathHum = data.getBathHum();
        bathHum += 15;      //calibrating
        int hum = (int)(bathHum*100);
        bathHum = hum/100d;
        System.out.println(bathHum);
        TempStorage.mapStorage.put("bathHum", bathHum);
        bathLight = data.getBathLight();
        TempStorage.mapStorage.put("bathLight", bathLight);
        bathFan = data.isBathFan();
        TempStorage.mapStorage.put("bathFan", bathFan);
        if (!flag) {                //check if message is sent
            if (!disregard) {       //disregard the next batch of data since it can be sent before the message
                flag = true;        //if disregarded allow the next message to be sent
            } else {
                disregard = false;
            }
        }
    }


    @Scheduled(fixedRate = 1000)    //500
    private void Auto() {
        if (auto) {
            bathroomFanCycles = bathroomFanDelay;   //it's *2 because the schedule is 2 times per second
            if (bathHum != null && bathLight != null && bathTemp != null) {
                if ((bathHum > 65 || bathLight >= 0.01) && !bathFan) {
                    switchON();
                } else if (bathHum < 56 && bathLight < 0.01 && bathFan) {
                    System.out.println(counterForBathroomFan);
                    System.out.println(bathroomFanCycles);
                    if (counterForBathroomFan >= bathroomFanCycles) {
                        switchOFF();
                    } else {
                        counterForBathroomFan++;
                    }
                } else {
                    if (bathLight >= 0.01) counterForBathroomFan = 0;
                }
            }
        }
    }


    public synchronized void switchON() {
        if (flag) {
            flag = false;
            disregard = true;
            System.out.println("switch it on");
            jo = new JSONObject();
            jo.put("data", true);
            String data = jo.toString();
            SendMessage.sendMessage("/topic/bathroomFan", data);
        }
    }

    public synchronized void switchOFF() {
        if (flag) {
            flag = false;
            disregard = true;
            System.out.println("switch it off");
            jo = new JSONObject();
            jo.put("data", false);
            String data = jo.toString();
            SendMessage.sendMessage("/topic/bathroomFan", data);
        }
    }

    @Scheduled(fixedRate = 20000)    //every 20s
    private void keepAlive() {
        if (flag) {
            flag = false;
            disregard = true;
            SendMessage.sendMessage("/topic/keepAlive", "doNotDie");
        }
    }



    public static Double getBathTemp() {
        return bathTemp;
    }

    public static void setBathTemp(Double bathTemp) {
        BathroomFanService.bathTemp = bathTemp;
    }

    public static Double getBathHum() {
        return bathHum;
    }

    public static void setBathHum(Double bathHum) {
        BathroomFanService.bathHum = bathHum;
    }

    public static Double getBathLight() {
        return bathLight;
    }

    public static void setBathLight(Double bathLight) {
        BathroomFanService.bathLight = bathLight;
    }

    public static boolean isBathFan() {
        return bathFan;
    }

    public static void setBathFan(boolean bathFan) {
        BathroomFanService.bathFan = bathFan;
    }

    public static boolean isAuto() {
        return auto;
    }

    public static void setAuto(boolean auto) {
        BathroomFanService.auto = auto;
    }

    public static boolean isBathFanCommand() {
        return bathFanCommand;
    }

    public static void setBathFanCommand(boolean bathFanCommand) {
        BathroomFanService.bathFanCommand = bathFanCommand;
    }

    public static int getBathroomFanDelay() {
        return bathroomFanDelay;
    }

    public static void setBathroomFanDelay(int bathroomFanDelay) {
        BathroomFanService.bathroomFanDelay = bathroomFanDelay;
    }
}
