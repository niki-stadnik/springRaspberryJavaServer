package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.herbPot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class herbPotService {

    private final SimpMessageSendingOperations messaging;

    private static float temp1 = 0;
    private static float temp2 = 0;
    private static int moisture1 = 0;
    private static int moisture2 = 0;
    private static float t1 = 0;
    private static float t2 = 0;
    private static int moisture1Percent = 0;
    private static int moisture2Percent = 0;
    private static boolean watered = false;


    public void setData(herbPotModel data) {
        moisture1 = data.getMoisture1();
        moisture2 = data.getMoisture2();
        watered = data.isWatered();
        temp1 = data.getTemp1();
        temp2 = data.getTemp2();
        log.info(String.valueOf(data));
    }

    public void command(herbPotClientModel data){
        System.out.println(data);
        int water = data.getWater();
        boolean restart = data.isRestart();
        messaging.convertAndSend("/topic/herbPot", new herbPotClientModel(water, restart));
        log.info(String.valueOf(data));
    }

    @Scheduled(fixedRate = 1000)
    private synchronized void potUpdate() {
        //(2500 - moisture) / (2500 - 1000)) * 100;
        t1 = (float) (2500 - moisture1) / 15;
        t2 = (float) (2500 - moisture2) / 15;
        moisture1Percent = (int) t1;
        moisture2Percent = (int) t2;
        messaging.convertAndSend("/topic/clientHerbPot", new herbPotModel(temp1, temp2, moisture1Percent, moisture2Percent, watered));
        //log.info(data);
    }

    //moisture value of 2500 is bone dray
    //moisture value of 1000 is fully saturated

}
