package net.github.nikistadnik.springRaspberryJavaServer.service;

import com.fasterxml.jackson.databind.JsonNode;
import net.github.nikistadnik.springRaspberryJavaServer.model.ClientModel;

import java.util.LinkedHashMap;

public interface DeviceService {
    String getName();
    void handle(byte[] payload);
    void handleCommand(byte[] payload);
}
