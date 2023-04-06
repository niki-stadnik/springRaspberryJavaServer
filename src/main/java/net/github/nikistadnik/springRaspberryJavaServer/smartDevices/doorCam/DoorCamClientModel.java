package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorCam;

public class DoorCamClientModel {

    private String image;

    public DoorCamClientModel() {
    }

    public DoorCamClientModel(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
