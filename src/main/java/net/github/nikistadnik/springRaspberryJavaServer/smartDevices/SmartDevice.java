package net.github.nikistadnik.springRaspberryJavaServer.smartDevices;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.discord.DiscordServiceBE;
import net.github.nikistadnik.springRaspberryJavaServer.model.SmartDeviceModel;
import net.github.nikistadnik.springRaspberryJavaServer.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
public abstract class SmartDevice<T, U> implements DeviceService {

    @Autowired
    protected SimpMessageSendingOperations messaging;
    @Autowired
    protected DeviceRegistry deviceRegistry;
    @Autowired
    protected ApplicationEventPublisher eventPublisher;
    @Autowired
    protected DiscordServiceBE discordServiceBE;
    @Autowired
    protected Environment env;

    protected final String deviceName;
    protected abstract String provideName();

    protected final String pairName;
    protected abstract String pairedWithName();

    protected Class<T> deviceModelType;
    protected abstract Class<T> provideDeviceModel();

    protected Class<U> clientModelType;
    protected abstract Class<U> provideClientModel();


    protected SmartDevice() {
        this.deviceName = provideName();
        this.pairName = pairedWithName();
        this.deviceModelType = provideDeviceModel();
        this.clientModelType = provideClientModel();
    }




    @Value("${discord.channel.id.reboot-log}")
    protected String discordChannelIdRebootLog;


    //todo опция за изключване на рестарт или нотификации поне

    //Reboot logic - make "active" true, every time data is received
    protected boolean active = false;
    protected boolean state = false;
    @Scheduled(initialDelay = 10000, fixedRate = 30000)    //every 30s
    protected synchronized void selfReboot(){
        if (!active) {
            state = false;
            selfRebootDev(deviceName);
            rebootDev(deviceName);
        }
        active = false;
    }
    protected void rebootDev(String dest) {
        long now = System.nanoTime();
        long last = deviceRegistry.lastCall().get();
        // 5 seconds in nanoseconds = 5_000_000_000L
        if (now - last < 5_000_000_000L) {
            log.info("last reboot call was less than 5 seconds ago");
            return;
        }
        deviceRegistry.lastCall().set(now);
        messaging.convertAndSend("/topic/" + dest + "/reboot", "{\"relayRestart\":true}");
        log.info("restart device: " + dest);
        if (Arrays.asList(env.getActiveProfiles()).contains("prod")) {
            discordServiceBE.sendMessage(dest, discordChannelIdRebootLog);
        }
    }

    long lastSelfReboot = 0;
    protected void selfRebootDev(String dest) {
        long now = System.nanoTime();
        // 5 seconds in nanoseconds = 5_000_000_000L
        if (now - lastSelfReboot < 5_000_000_000L) {
            log.info("last reboot call was less than 5 seconds ago");
            return;
        }
        lastSelfReboot = now;
        messaging.convertAndSend("/topic/" + dest + "/selfReboot", "{\"relayRestart\":true}");
        log.info("self reboot device: " + dest);
        if (Arrays.asList(env.getActiveProfiles()).contains("prod")) {
            discordServiceBE.sendMessage(dest, discordChannelIdRebootLog);
        }
    }

    @Scheduled(fixedRate = 1000)
    private synchronized void statusUpdate() {
        messaging.convertAndSend("/topic/state/" + deviceName, new SmartDeviceModel(state));
        //log.info(data);
    }

    @Override
    public String getName() {
        return deviceName;
    }


    @Override
    public void handle(byte[] payload) {
        active = true;
        state = true;
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            T data = objectMapper.readValue(payload, deviceModelType);
            handleDeviceData(data);
        } catch (IOException e) {
            throw new RuntimeException("Invalid JSON payload", e);
        }
        //log.info("map: {}", String.valueOf(map));
    }
    protected abstract void handleDeviceData(T data);

    @Override
    public void handleCommand(byte[] payload) {
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            U data = objectMapper.readValue(payload, clientModelType);
            handleClientData(data);
        } catch (IOException e) {
            throw new RuntimeException("Invalid JSON payload", e);
        }
        //log.info("map: {}", String.valueOf(map));
    }
    protected abstract void handleClientData(U data);
}



//todo мога да го направя, дата и статус да се пращат на 1 адрес, но с различна команда?
//също както направих девайс/ и кллиент/ да са с 1 и същ топик, така и да махна другите

//ако направя моделите да наследяват интерфейс, и в цонтролера подам интерфейса, ще се натаманяват ли сами?

//да си мятам джейсана до сървиса и  там да го конвертирам, вместо да се чудя как да подавам модели
