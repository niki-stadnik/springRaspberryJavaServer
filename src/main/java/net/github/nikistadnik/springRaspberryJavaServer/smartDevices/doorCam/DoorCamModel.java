package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorCam;

import lombok.Data;

@Data
public class DoorCamModel {

    private byte[] imageData;

    public DoorCamModel() {
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public DoorCamModel(byte[] imageData) {
        this.imageData = imageData;
    }


}
