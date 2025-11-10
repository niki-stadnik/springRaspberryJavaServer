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
    private static int moisture1Percent = 0;
    private static int moisture2Percent = 0;
    private static boolean watered = false;


    public void setData(herbPotModel data) {
        int moisture1 = data.getMoisture1();
        int moisture2 = data.getMoisture2();
        //(2500 - moisture) / (2500 - 1000)) * 100;
        //(2500 - moisture) / (25 - 10);
        //nov min ot 1000 na 560
        //hum 1 ot4elo 680 - 120tina %
        //133 565 /25-5.6=19.4
        float h1 = (float) ((2500 - moisture1) / 19.4);
        float h2 = (float) ((2500 - moisture2) / 19.4);
        moisture1Percent = (int) h1;
        moisture2Percent = (int) h2;
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
        messaging.convertAndSend("/topic/clientHerbPot", new herbPotModel(temp1, temp2, moisture1Percent, moisture2Percent, watered));
        //log.info(data);
    }

    //moisture value of 2500 is bone dray
    //moisture value of 1000 is fully saturated

}
