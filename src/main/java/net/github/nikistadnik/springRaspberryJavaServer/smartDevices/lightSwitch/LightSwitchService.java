package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch;

import net.github.nikistadnik.springRaspberryJavaServer.TempStorage;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SendMessage;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomFan.BathroomFanModel;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class LightSwitchService {

    JSONObject jo;
    private static Double fuseBoxTemp = null;
    private static Double fuseBoxHum = null;
    private static boolean fuseBoxFan = false;
    private static boolean[] light = new boolean[8];


    public LightSwitchService() {
    }

    void lightsControl(){
        //todo auto lights
    }

    public void setData(LightSwitchModel data) {
        System.out.println(data);
        fuseBoxTemp = data.getFuseBoxTemp();
        TempStorage.mapStorage.put("fuseBoxTemp", fuseBoxTemp);
        fuseBoxHum = data.getFuseBoxHum();
        TempStorage.mapStorage.put("fuseBoxHum", fuseBoxHum);
        fuseBoxFan = data.isFuseBoxFan();
        TempStorage.mapStorage.put("fuseBoxFan", fuseBoxFan);
        light[0] = data.isLight0();
        TempStorage.mapStorage.put("light0", light[0]);
        light[1] = data.isLight1();
        TempStorage.mapStorage.put("light0", light[1]);
        light[2] = data.isLight2();
        TempStorage.mapStorage.put("light0", light[2]);
        light[3] = data.isLight3();
        TempStorage.mapStorage.put("light0", light[3]);
        light[4] = data.isLight4();
        TempStorage.mapStorage.put("light0", light[4]);
        light[5] = data.isLight5();
        TempStorage.mapStorage.put("light0", light[5]);
        light[6] = data.isLight6();
        TempStorage.mapStorage.put("light0", light[6]);
        light[7] = data.isLight7();
        TempStorage.mapStorage.put("light0", light[7]);
    }

    public synchronized void ChangeState(boolean[] puls) {
        System.out.println("Change State");

        jo = new JSONObject();
        jo.put("pulse0", puls[0]);  //on off state for fan and not lights
        jo.put("pulse1", puls[1]);  // all the other are light pulses
        jo.put("pulse2", puls[2]);
        jo.put("pulse3", puls[3]);
        jo.put("pulse4", puls[4]);
        jo.put("pulse5", puls[5]);
        jo.put("pulse6", puls[6]);
        jo.put("pulse7", puls[7]);
        jo.put("pulse8", puls[8]);
        String data = jo.toString();
        SendMessage.sendMessage("/topic/lightSwitch", data);
    }

    @Scheduled(fixedRate = 5000)
    void fanControl() {
        if (fuseBoxTemp != null && fuseBoxHum != null) {
            if (fuseBoxTemp > 70 || fuseBoxTemp < 0) {
                //high alert
                //send notifications to app

                boolean[] alloff = {false, false, false, false, false, false, false, false, false}; //the first false is for the fan
                for (int i = 0; i < 8; i++) {
                    if (light[i]) {
                        alloff[i+1] = true;
                    }
                }
                ChangeState(alloff);

            }
            if ((fuseBoxTemp > 30 || fuseBoxHum > 60) && !fuseBoxFan) {
                boolean[] fanon = {true, false, false, false, false, false, false, false, false};
                ChangeState(fanon);
            } else if (fuseBoxFan) {
                boolean[] fanoff = {false, false, false, false, false, false, false, false, false};
                ChangeState(fanoff);

            }
        }
    }

}
