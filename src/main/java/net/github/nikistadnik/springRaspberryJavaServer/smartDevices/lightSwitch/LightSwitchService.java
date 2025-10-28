package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch;

import lombok.RequiredArgsConstructor;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SendMessage;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorman.DoormanService;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenLEDstrip.kitchenLEDstripService;
import org.json.JSONObject;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LightSwitchService {

    private final SimpMessageSendingOperations messaging;

    private static boolean active = false;
    private static boolean flag = false;
    private static boolean disregard = false;
    static JSONObject jo;
    private static final boolean[] light = new boolean[8];
    private static final boolean[] lightCommand = new boolean[8];
    private static final boolean[] commandFlag = new boolean[8];



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
            Switch(comm);
            if (lightCommand[lightNum]) Thread.sleep(1000); //1 sec delay
            else Thread.sleep(4000); //4 sec delay
        }
        commandFlag[lightNum] = false;
    }


    public void setData(LightSwitchModel data) {
        boolean[] newLight = new boolean[8];
        JSONObject jsonObject = new JSONObject(data);
        System.out.println(jsonObject.toString());
        active = true;
        newLight[0] = data.isLight0();
        newLight[1] = data.isLight1();
        newLight[2] = data.isLight2();
        newLight[3] = data.isLight3();
        newLight[4] = data.isLight4();
        newLight[5] = data.isLight5();
        newLight[6] = data.isLight6();
        newLight[7] = data.isLight7();
        //check for updated state
        //String dataOut = data.toString();
        //SendMessage.sendMessage("/topic/lightsClient", dataOut);
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
        if (light == 2) kitchenLEDstripService.receiveAlert(state);
    }

    public void Switch (boolean[] pulse) {
        if (flag) {
            flag = false;
            disregard = true;
            System.out.println("Change State");
            messaging.convertAndSend("/topic/lightSwitch", new LightSwitchCommandModel(pulse[0], pulse[1], pulse[2], pulse[3], pulse[4], pulse[5], pulse[6], pulse[7], pulse[8]));
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
        jo = new JSONObject();
        jo.put("pulse0", false);
        jo.put("pulse1", false);
        jo.put("pulse2", false);
        jo.put("pulse3", false);
        jo.put("pulse4", false);
        jo.put("pulse5", false);
        jo.put("pulse6", false);
        jo.put("pulse7", false);
        jo.put("pulse8", true);  //relay for doorman (operated with a pulse like the lights)
        String data = jo.toString();
        SendMessage.sendMessage("/topic/lightSwitch", data);
    }

}
