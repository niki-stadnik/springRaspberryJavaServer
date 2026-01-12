package net.github.nikistadnik.springRaspberryJavaServer.service;

public interface DeviceService {
    String getName();
    void handle(byte[] payload);
    void handleCommand(byte[] payload);
    boolean getState();
    void setState(boolean b);
    boolean getActive();
    void setActive(boolean b);
    boolean getRebootMonitor();
    void setRebootMonitor(boolean b);
    void rebootDev(String dest);
    void selfRebootDev(String dest);
}
