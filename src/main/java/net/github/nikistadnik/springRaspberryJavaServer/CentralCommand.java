package net.github.nikistadnik.springRaspberryJavaServer;

import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.BathroomFan;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.LightSwitch;
import net.github.nikistadnik.springRaspberryJavaServer.storage.StorageController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CentralCommand extends Thread {
    private static volatile boolean loop = true;

    static int repeatInterval = 10; //delay between loops in ms //at 100 arduino cant decrypt in time xD
    // might have to implement restrictions on broadcasts per sec (if 2 arduinos have to get commands at once)
    private final BathroomFan bathroomFan;
    private final LightSwitch lightSwitch;

    //calls the Control method of all devices and executes it at a certain intervals
    @Autowired
    CentralCommand(BathroomFan bathroomFan,
                   LightSwitch lightSwitch) {
        this.bathroomFan = bathroomFan;
        this.lightSwitch = lightSwitch;
        start();
    }

    //automation thread
    public void run() {

        long past = 0;

        while (loop) {

            if (TempStorage.bathroomFanMode == TempStorage.Mode.AUTO) {
                bathroomFan.Auto();
            }

            lightSwitch.Auto();


            //every second - DB update
            if (System.currentTimeMillis() >= past + 1000) {
                //db update
                //StorageController.insertIns();
                //System.out.println("Fixed rate task - " + System.currentTimeMillis() / 1000);
                past = System.currentTimeMillis();
            }
            try {
                Thread.sleep(repeatInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getRepeatInterval() {
        return repeatInterval;
    }

    public static void terminate() {
        loop = false;
    }

}
