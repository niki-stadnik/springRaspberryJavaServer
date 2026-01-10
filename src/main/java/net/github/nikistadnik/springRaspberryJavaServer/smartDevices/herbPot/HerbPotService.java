package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.herbPot;

import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.model.ClientModel;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SmartDevice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
public class HerbPotService extends SmartDevice<HerbPotModel, HerbPotFromClientModel> {

    private static float temp1 = 0;
    private static float temp2 = 0;
    private static int moisture1Percent = 0;
    private static int moisture2Percent = 0;
    private static boolean watered = false;


    @Override
    protected String provideName() {
        return "herbPot";
    }

    @Override
    protected String pairedWithName() {
        return "growLight";
    }

    @Override
    protected Class provideDeviceModel() {
        return HerbPotModel.class;
    }

    @Override
    protected Class provideClientModel() {
        return HerbPotFromClientModel.class;
    }


    @Scheduled(fixedRate = 1000)
    private synchronized void potUpdate() {
        messaging.convertAndSend("/topic/client/" + deviceName, new HerbPotModel(temp1, temp2, moisture1Percent, moisture2Percent, watered));
        //log.info(data);
    }

    @Value("${discord.channel.id.general}")
    private String discordChannelIdGeneral;

    @Scheduled(initialDelay = 10000, fixedRate = 3600000) //3 600 000 ms = 1h
    private void lowHumAlertToDiscord(){
        if (Arrays.asList(env.getActiveProfiles()).contains("prod")) {
            if (moisture1Percent >= 55 || moisture2Percent >= 55) return;
            if (moisture1Percent < 55){
                String message = "Water herbs pot 1! Moisture: " + moisture1Percent + "%";
                discordServiceBE.sendMessage(message, discordChannelIdGeneral);
            }
            if (moisture2Percent < 55){
                String message = "Water herbs pot 2! Moisture: " + moisture2Percent + "%";
                discordServiceBE.sendMessage(message, discordChannelIdGeneral);
            }
        }
    }


    @Override
    protected void handleDeviceData(HerbPotModel data) {
        int moisture1 = data.moisture1();
        int moisture2 = data.moisture2();
        //moisture value of 2500 is bone dray
        //moisture value of 1000 is fully saturated
        //(2500 - moisture) / (2500 - 1000)) * 100;
        //(2500 - moisture) / (25 - 10);
        //nov min ot 1000 na 560
        //hum 1 ot4elo 680 - 120tina %
        //133 565 /25-5.6=19.4
        float h1 = (float) ((2500 - moisture1) / 19.4);
        float h2 = (float) ((2500 - moisture2) / 19.4);
        moisture1Percent = (int) h1;
        moisture2Percent = (int) h2;
        watered = data.watered();
        temp1 = data.temp1();
        temp2 = data.temp2();
        log.info(String.valueOf(data));
    }

    @Override
    protected void handleClientData(HerbPotFromClientModel data) {
        int command = data.command();
        int water = 0;
        switch (command){
            case 1:
                selfRebootDev(deviceName);
                rebootDev(deviceName);
                break;
            case 2: rebootDev(pairName); break;
            case 3: water = 3000; break;
        }
        if (command != 1) {
            messaging.convertAndSend("/topic/" + deviceName, new HerbPotClientModel(water));
        }
        log.info(String.valueOf(data));
    }
}
