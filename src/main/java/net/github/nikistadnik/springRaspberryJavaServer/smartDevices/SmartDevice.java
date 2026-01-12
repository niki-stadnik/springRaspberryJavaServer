package net.github.nikistadnik.springRaspberryJavaServer.smartDevices;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.discord.DiscordServiceBE;
import net.github.nikistadnik.springRaspberryJavaServer.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

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

    protected boolean active = false;
    protected boolean state = false;
    protected boolean rebootMonitor = true;


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


    @Override
    public void rebootDev(String dest) {
        messaging.convertAndSend("/topic/" + dest + "/reboot", "{\"relayRestart\":true}");
        log.info("restart device: " + dest);
        if (Arrays.asList(env.getActiveProfiles()).contains("prod")) {
            discordServiceBE.sendMessage(dest, discordChannelIdRebootLog);
        }
    }

    public void selfRebootDev(String dest) {
        messaging.convertAndSend("/topic/" + dest + "/selfReboot", "{\"relayRestart\":true}");
        log.info("self reboot device: " + dest);
        if (Arrays.asList(env.getActiveProfiles()).contains("prod")) {
            discordServiceBE.sendMessage(dest, discordChannelIdRebootLog);
        }
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

    @Override
    public boolean getState() {
        return state;
    }

    @Override
    public void setState(boolean b) {
        state = b;
    }

    @Override
    public boolean getActive() {
        return active;
    }

    @Override
    public void setActive(boolean b) {
        active = b;
    }

    @Override
    public boolean getRebootMonitor() {
        return rebootMonitor;
    }

    @Override
    public void setRebootMonitor(boolean b) {
        rebootMonitor = b;
    }
}
