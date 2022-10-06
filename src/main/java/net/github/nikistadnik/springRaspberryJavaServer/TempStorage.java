package net.github.nikistadnik.springRaspberryJavaServer;

import java.util.LinkedHashMap;
import java.util.Map;

public class TempStorage {
    enum Mode {ON, OFF, AUTO}

    public static Map mapStorage = new LinkedHashMap();
    public static Mode bathroomFanMode = Mode.AUTO;
    //"bathroomFanMode", "auto"
    //"BathroomFanDelay", 30

}
