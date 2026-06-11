package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.herbPot;

import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.model.ClientModel;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SmartDevice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Arrays;

@Service
@Slf4j
public class HerbPotService extends SmartDevice<HerbPotModel, HerbPotFromClientModel> {

    private static float temp1 = 0;
    private static float temp2 = 0;
    private static int moisture1Percent = 0;
    private static int moisture2Percent = 0;
    private static boolean herbLight = false;
    private static boolean herbLightNew = false;
    private static boolean herbLightAuto = true;

    private static LocalTime herbLightStartTime = LocalTime.of(9, 0);
    private static LocalTime herbLightEndTime = LocalTime.of(0, 0);

    private static boolean herbWaterAuto = true;
    private static LocalTime herbWaterTime = LocalTime.of(8, 0);
    private static int humAlert = 50;
    private static int waterDuration = 30;




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

    @Override
    protected void loadVariables() {
        herbLightStartTime = appVariablesService.getLocalTime("herbpot.light.start", LocalTime.of(9, 0));
        herbLightEndTime = appVariablesService.getLocalTime("herbpot.light.end", LocalTime.of(0, 0));
        herbLightAuto = appVariablesService.getBoolean("herbpot.light.auto", true);
        herbWaterTime = appVariablesService.getLocalTime("herbpot.water.start", LocalTime.of(0, 0));
        herbWaterAuto = appVariablesService.getBoolean("herbpot.water.auto", true);
        humAlert = appVariablesService.getInt("herbpot.water.alert", 50);
        waterDuration = appVariablesService.getInt("herbpot.water.duration", 30);
    }


    @Scheduled(fixedRate = 1000)
    private synchronized void potUpdate() {
        messaging.convertAndSend("/topic/client/" + deviceName, new HerbPotToClientModel(temp1, temp2, moisture1Percent, moisture2Percent, herbLight, herbLightAuto, herbLightStartTime, herbLightEndTime, humAlert, waterDuration, herbWaterTime, herbWaterAuto));
    }

    @Value("${discord.channel.id.general}")
    private String discordChannelIdGeneral;

    @Scheduled(initialDelay = 10000, fixedRate = 3600000) //3 600 000 ms = 1h
    private void lowHumAlertToDiscord() {
        if (Arrays.asList(env.getActiveProfiles()).contains("prod")) {
            if (moisture1Percent >= humAlert || moisture2Percent >= humAlert) return;
            if (moisture1Percent < humAlert) {
                String message = "Water herbs pot 1! Moisture: " + moisture1Percent + "%";
                discordServiceBE.sendMessage(message, discordChannelIdGeneral);
            }
            if (moisture2Percent < humAlert) {
                String message = "Water herbs pot 2! Moisture: " + moisture2Percent + "%";
                discordServiceBE.sendMessage(message, discordChannelIdGeneral);
            }
        }
    }

    private void herbLightControl() {
        herbLightNew = isActive(herbLightStartTime, herbLightEndTime);
        if (herbLight != herbLightNew) {
            messaging.convertAndSend("/topic/" + deviceName, new HerbPotClientModel(0, herbLightNew));
            log.info("herb light change: " + herbLight + "->" + herbLightNew);
        }
    }

    public boolean isActive(LocalTime start, LocalTime end) {
        LocalTime now = LocalTime.now();
        if (start.isBefore(end)) {
            return now.isAfter(start) && now.isBefore(end);
        }
        return now.isAfter(start) || now.isBefore(end);
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
        herbLight = data.herbLight();
        temp1 = data.temp1();
        temp2 = data.temp2();
        //log.info(String.valueOf(data));
        if (herbLightAuto) herbLightControl();
        else messaging.convertAndSend("/topic/" + deviceName, new HerbPotClientModel(0, false));
    }

    @Override
    protected void handleClientData(HerbPotFromClientModel data) {
        log.info(String.valueOf(data));
        int command = data.command();
        switch (command) {
            case 1:
                selfRebootDev(deviceName);
                rebootDev(deviceName);
                break;
            case 2:
                rebootDev(pairName);
                break;
            case 3:
                herbWaterAuto = !herbWaterAuto;
                appVariablesService.setBoolean("herbpot.water.auto", herbWaterAuto);
                break;
            case 4:
                herbLightAuto = !herbLightAuto;
                if (herbLight && !herbLightAuto) {
                    herbLightNew = false;
                }
                appVariablesService.setBoolean("herbpot.light.auto", herbLightAuto);
                messaging.convertAndSend("/topic/" + deviceName, new HerbPotClientModel(0, herbLightNew));
                log.info("2 herb light change: " + herbLight + "->" + herbLightNew);
                break;
            case 5:
                herbLightStartTime = data.herbLightStartTime();
                appVariablesService.setLocalTime("herbpot.light.start", data.herbLightStartTime());
                herbLightEndTime = data.herbLightEndTime();
                appVariablesService.setLocalTime("herbpot.light.end", data.herbLightEndTime());
                break;
            case 6:
                herbWaterTime = data.herbWaterTime();
                appVariablesService.setLocalTime("herbpot.water.start", data.herbWaterTime());
                humAlert = data.humAlert();
                appVariablesService.setInt("herbpot.water.alert", data.humAlert());
                waterDuration = data.waterDuration();
                appVariablesService.setInt("herbpot.water.duration", data.waterDuration());
                break;
        }
    }
}


//todo watering auto logic