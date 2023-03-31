package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch;

import net.github.nikistadnik.springRaspberryJavaServer.TempStorage;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SendMessage;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class LightSwitchService {

    private static boolean flag = false;
    private static boolean disregard = false;
    JSONObject jo;
    private static Double fuseBoxTemp = null;
    private static Double fuseBoxHum = null;
    private static boolean fuseBoxFan = false;
    private static final boolean[] light = new boolean[8];
    private static final boolean[] lightSnapshot = new boolean[8];
    private static final boolean[] lightSnapshotAuto = new boolean[8];
    private static final boolean[] flagManual = new boolean[8];
    private static final boolean[] lightCommand = new boolean[8];
    private static boolean check = false;
    private static int delay = 0;


    public LightSwitchService() {
    }

    //@Scheduled(fixedRate = 500)
    void lightsControl() {
        //todo auto lights
        //flagManual[] is true when the light was turned on or off from the wall or the webapp
        //when flagManual[] is false that lamp can be controlled on auto
    }

    public void command(LightSwitchClientModel data) {
        System.out.println(data);
        lightCommand[0] = data.isLight0();
        lightCommand[1] = data.isLight1();
        lightCommand[2] = data.isLight2();
        lightCommand[3] = data.isLight3();
        lightCommand[4] = data.isLight4();
        lightCommand[5] = data.isLight5();
        lightCommand[6] = data.isLight6();
        lightCommand[7] = data.isLight7();
        for (int i = 0; i < 8; i++) {
            lightSnapshot[i] = light[i];
        }
        check = true;
        boolean[] comm = {fuseBoxFan, lightCommand[0], lightCommand[1], lightCommand[2], lightCommand[3], lightCommand[4], lightCommand[5], lightCommand[6], lightCommand[7]};
        ChangeState(comm);
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
        TempStorage.mapStorage.put("light1", light[1]);
        light[2] = data.isLight2();
        TempStorage.mapStorage.put("light2", light[2]);
        light[3] = data.isLight3();
        TempStorage.mapStorage.put("light3", light[3]);
        light[4] = data.isLight4();
        TempStorage.mapStorage.put("light4", light[4]);
        light[5] = data.isLight5();
        TempStorage.mapStorage.put("light5", light[5]);
        light[6] = data.isLight6();
        TempStorage.mapStorage.put("light6", light[6]);
        light[7] = data.isLight7();
        TempStorage.mapStorage.put("light7", light[7]);
        if (!flag) {
            if (!disregard) {
                flag = true;
                delay++;
                if (check) {
                    tagManualInputs();
                    checkJobDone();
                }
            } else disregard = false;
        } else {
            tagManualInputs();
        }
    }

    private void checkJobDone() {
        check = false;
        for (int i = 0; i < 8; i++) {
            if (lightCommand[i]) {
                if (light[i] != lightSnapshot[i]) {
                    lightCommand[i] = false;
                } else check = true;
            }
        }
        if (check) {
            if (delay > 10) {
                boolean[] comm = {fuseBoxFan, lightCommand[0], lightCommand[1], lightCommand[2], lightCommand[3], lightCommand[4], lightCommand[5], lightCommand[6], lightCommand[7]};
                ChangeState(comm);
                delay = 0;
            }
        }
    }

    private void tagManualInputs() {
        for (int i = 0; i < 8; i++) {
            if (lightSnapshotAuto[i] != light[i]) {
                lightSnapshotAuto[i] = light[i];
                if (!flagManual[i]) {
                    flagManual[i] = true;
                } else flagManual[i] = false;
            }
        }
    }

    public synchronized void ChangeState(boolean[] puls) {
        if (flag) {
            flag = false;
            disregard = true;
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
    }

    @Scheduled(fixedRate = 300000)    //every 5 min = 300000
    private void keepAlive() {
        if (flag) {
            flag = false;
            disregard = true;
            SendMessage.sendMessage("/topic/keepAlive", "donNotDie");
        }
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

}
