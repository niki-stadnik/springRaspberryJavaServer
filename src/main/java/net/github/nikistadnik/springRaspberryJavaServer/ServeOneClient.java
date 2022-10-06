package net.github.nikistadnik.springRaspberryJavaServer;

import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.BathroomFan;

import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.LightSwitch;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.Socket;
import java.util.Map;


class ServeOneClient extends Thread {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    JSONParser parser;
    private volatile boolean loop = true;

    int ID = 0;

    private Clients clt;

    //@Autowired
    private BathroomFan bathroomFan;//ако не работи то е защото класа serveOneClient не е component/service

    public ServeOneClient(Socket s, Clients clt) throws IOException {
        socket = s;
        this.clt = clt;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        clt.addC(out);
        parser = new JSONParser();
//ako tuk vzema id moga vyv sy6yiq run da switchna nqkolko razliqni loopa taka qe da se povtarq samo nujnoto
        Map data = readAll();
        start();
    }


    public void run() {

        try {


                /*if (ID >= 500) {        // should be <= or what? !!!!!!
                    //controller input // андроида ще праща стринг с инструкция във 1 променлива във мапа
                    if (data != null) {
                        String command = (String) data.get("command");
                        System.out.println(command);
                        if (command != null) {
                            handleCommand(command);
                        }
                    }
                } else if (ID > 0) {
                    //sensors
                    for (Map.Entry pair : (Iterable<Map.Entry>) data.entrySet()) {
                        Storage.mapStorage.put(pair.getKey(), pair.getValue());
                        System.out.println(pair.getKey() + " : " + pair.getValue());
                    }

                }*/

            switch (ID) {
                case 1:     //bathroom Fan
                    while (loop) {
                        Map data = readAll();

                        System.out.println();
                        System.out.println("Sender: " + ID);
                        dataToStorage(data);
                        //devices.bathroomFan.respond();    //idea if the command is broadcast and not personal
                        //bathroomFan.respond();
                        BathroomFan.action();
                        System.out.println("total number of sockets: " + clt.nCl());
                        //System.out.println(data);
                    }
                    break;
                case 2:     //LightSwitch
                    while (loop) {
                        Map data = readAll();

                        System.out.println();
                        System.out.println("Sender: " + ID);
                        dataToStorage(data);
                        //devices.bathroomFan.respond();    //idea if the command is broadcast and not personal
                        //bathroomFan.respond();
                        LightSwitch.action();
                        System.out.println("total number of sockets: " + clt.nCl());
                        //System.out.println(data);
                    }
                    break;
                default:    //controller input // андроида ще праща стринг с инструкция във 1 променлива във мапа
                    while (loop) {
                        Map data = readAll();

                        System.out.println();
                        System.out.println("Sender: " + ID);
                        /*
                        if (data != null) {
                            String command = (String) data.get("command");
                            System.out.println(command);
                            if (command != null) {
                                handleCommand(command);
                            }
                        }
                         */
                        System.out.println("total number of sockets: " + clt.nCl());
                        //System.out.println(data);
                    }
            }

        } catch (IOException e) {
            loop = false;
        } finally {
            try {
                loop = false;
                clt.rmvC(out);
                System.out.println("disconnect a client. Total number " + clt.nCl());
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //puts the ID in the global variable with the same name
    //returns Map data
    Map readAll() throws IOException {
        Map data = null;

        String str = in.readLine();
        //if (str.equals("END")) loop = false;

        if (str != null && str.length() > 5) {
            //String decrypted = Encryption.decrypt(str);
            String decrypted = str;     //temp removed encrypt
            if (decrypted != null) {
                char[] charArr = decrypted.toCharArray();   //get the first char to see if it is json
                if (charArr[0] == '{') {
                    JSONObject json = null; //ако е някакъв произволен пакет или има грешка - json parsera се шашка и бие грешка
                    try {
                        json = (JSONObject) parser.parse(decrypted);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (json != null) {
                        /////////////do stuff here
                        long x = (long) json.get("ID");
                        ID = (int) x;
                        /////////////
                        data = ((Map) json.get("data"));
                        /////////////
                    } else {
                        System.out.println("json not parsed correctly");
                        loop = false;
                    }
                } else {
                    System.out.println("no correct json decrypted");
                    loop = false;
                }
            } else {
                System.out.println("decrypted string is null");
                loop = false;
            }
        } else {
            System.out.println("empty string received");
            loop = false;
        }
        return data;
    }

    void dataToStorage(Map data) {
        for (Map.Entry pair : (Iterable<Map.Entry>) data.entrySet()) {
            TempStorage.mapStorage.put(pair.getKey(), pair.getValue());
            System.out.println(pair.getKey() + " : " + pair.getValue());
        }
    }

    void handleCommand(String command) {
        switch (command) {
            case "giveData":
                JSONObject jo = new JSONObject();
                jo.put("ID", 999);
                jo.put("data", TempStorage.mapStorage);
                String data = jo.toString();
                String encrypted = Encryption.encrypt(data);
                out.println(encrypted);              //only to the client of this thread
                System.out.println("here is your data");
                System.out.println(TempStorage.mapStorage);
                break;
            case "bathroomFanON":
                TempStorage.bathroomFanMode = TempStorage.Mode.ON;
                TempStorage.mapStorage.put("bathroomFanMode", "on");
                bathroomFan.switchON();
                break;
            case "bathroomFanAuto":
                TempStorage.bathroomFanMode = TempStorage.Mode.AUTO;
                TempStorage.mapStorage.put("bathroomFanMode", "auto");
                break;
            case "bathroomFanOFF":
                TempStorage.bathroomFanMode = TempStorage.Mode.OFF;
                TempStorage.mapStorage.put("bathroomFanMode", "off");
                bathroomFan.switchOFF();
                break;
            case "dataDB":
                System.out.println("data from db");
                //PostgreSQL.getAllData();
                //handle data and send back to android
        }
    }
}

