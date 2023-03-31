package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomFan;

import lombok.Data;

@Data
public class BathroomFanClientModel {
    private boolean bathFanCommand = false;
    private boolean auto = true;

    public BathroomFanClientModel() {
    }

    public BathroomFanClientModel(boolean bathFanCommand, boolean auto) {
        this.bathFanCommand = bathFanCommand;
        this.auto = auto;
    }

    public boolean isBathFanCommand() {
        return bathFanCommand;
    }

    public void setBathFanCommand(boolean bathFanCommand) {
        this.bathFanCommand = bathFanCommand;
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }
}
