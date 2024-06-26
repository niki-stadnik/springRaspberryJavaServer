package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch;

import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SendMessage;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorman.DoormanService;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenLEDstrip.kitchenLEDstripService;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class LightSwitchService {

    private static boolean active = false;
    private static boolean flag = false;
    private static boolean disregard = false;
    static JSONObject jo;
    //private static Double fuseBoxTemp = null;
    //private static Double fuseBoxHum = null;
    //private static boolean fuseBoxFan = false;
    private static final boolean[] light = new boolean[8];
    private static final boolean[] lightCommand = new boolean[8];
    private static final boolean[] commandFlag = new boolean[8];



    public LightSwitchService() {
    }

    //@Scheduled(fixedRate = 500)
    void lightsControl() {
        //todo auto lights
        //flagManual[] is true when the light was turned on or off from the wall or the webapp
        //when flagManual[] is false that lamp can be controlled on auto
    }

    public void command(LightSwitchClientModel data) throws InterruptedException {
        System.out.println(data);
        int lightNum = data.getLight();
        lightCommand[lightNum] = data.isState();
        if(!commandFlag[lightNum]) {
            commandFlag[lightNum] = true;
            jobToDo(lightNum);
        }
    }

    private void jobToDo(int lightNum) throws InterruptedException {
        while (light[lightNum] != lightCommand[lightNum]){
            boolean[] comm = new boolean[9];
            //comm[0] = fuseBoxFan;
            comm[lightNum/* + 1*/] = true;
            ChangeState(comm);
            if (lightCommand[lightNum]) Thread.sleep(1000); //1 sec delay
            else Thread.sleep(4000); //4 sec delay
        }
        commandFlag[lightNum] = false;
    }


    public void setData(LightSwitchModel data) {
        boolean[] newLight = new boolean[8];
        System.out.println(data);
        active = true;
        //fuseBoxTemp = data.getFuseBoxTemp();
        //fuseBoxHum = data.getFuseBoxHum();
        //fuseBoxFan = data.isFuseBoxFan();
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
                alertSubscribers(i, newLight[i]);
            }
        }
        if (!flag) {
            if (!disregard) {
                flag = true;
            } else disregard = false;
        }
    }

    private void alertSubscribers(int light, boolean state){
        if (light == 3) kitchenLEDstripService.receiveAlert(state);
    }


    public static synchronized void ChangeState(boolean[] puls) {
        if (flag) {
            flag = false;
            disregard = true;
            System.out.println("Change State");
            jo = new JSONObject();
            jo.put("pulse0", puls[0]);
            jo.put("pulse1", puls[1]);
            jo.put("pulse2", puls[2]);
            jo.put("pulse3", puls[3]);
            jo.put("pulse4", puls[4]);
            jo.put("pulse5", puls[5]);
            jo.put("pulse6", puls[6]);
            jo.put("pulse7", puls[7]);
            jo.put("pulse8", puls[8]);  //relay for doorman (operated with a pulse like the lights)
            String data = jo.toString();
            SendMessage.sendMessage("/topic/lightSwitch", data);
        }
    }


    //@Scheduled(fixedRate = 20000)    //every 20s
    private void keepAlive() {
        if (flag) {
            flag = false;
            disregard = true;
            SendMessage.sendMessage("/topic/keepAlive", "donNotDie");
        }
    }

    @Scheduled(fixedRate = 10000)    //every 10s
    private synchronized void selfReboot(){
        if (!active) DoormanService.rebootLightSwitch();
        active = false;
    }

    public static synchronized void rebootDoorman() {
        System.out.println("reboot Doorman");
        boolean[] res = {false, false, false, false, false, false, false, false, true};
        ChangeState(res);
    }

    /*
    @Scheduled(fixedRate = 5000)
    void fanControl() {
        if (fuseBoxTemp != null && fuseBoxHum != null) {
            if (fuseBoxTemp > 70 || fuseBoxTemp < 0) {
                //high alert
                //send notifications to app

                boolean[] alloff = {false, false, false, false, false, false, false, false, false}; //the first false is for the fan
                for (int i = 0; i < 8; i++) {
                    if (light[i]) {
                        alloff[i + 1] = true;
                    }
                }
                ChangeState(alloff);

            }
            if ((fuseBoxTemp > 40 || fuseBoxHum > 60) && !fuseBoxFan) {
                boolean[] fanon = {true, false, false, false, false, false, false, false, false};
                ChangeState(fanon);
            } else if (fuseBoxFan) {
                boolean[] fanoff = {false, false, false, false, false, false, false, false, false};
                ChangeState(fanoff);

            }
        }
    }
    */

}
