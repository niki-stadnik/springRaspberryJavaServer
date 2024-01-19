package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorman;

import lombok.Data;

@Data
public class DoormanModel {

    private boolean doorOpen;
    private boolean doorLock;
    private boolean doorButton;
    private boolean bell;
    private boolean rfid;

    public DoormanModel() {
    }

    public DoormanModel(boolean doorOpen, boolean doorLock, boolean doorButton, boolean bell, boolean rfid) {
        this.doorOpen = doorOpen;
        this.doorLock = doorLock;
        this.doorButton = doorButton;
        this.bell = bell;
        this.rfid = rfid;
    }

    public boolean isDoorOpen() {
        return doorOpen;
    }

    public void setDoorOpen(boolean doorOpen) {
        this.doorOpen = doorOpen;
    }

    public boolean isDoorLock() {
        return doorLock;
    }

    public void setDoorLock(boolean doorLock) {
        this.doorLock = doorLock;
    }

    public boolean isDoorButton() {
        return doorButton;
    }

    public void setDoorButton(boolean doorButton) {
        this.doorButton = doorButton;
    }

    public boolean isBell() {
        return bell;
    }

    public void setBell(boolean bell) {
        this.bell = bell;
    }

    public boolean isRfid() {
        return rfid;
    }

    public void setRfid(boolean rfid) {
        this.rfid = rfid;
    }
}
