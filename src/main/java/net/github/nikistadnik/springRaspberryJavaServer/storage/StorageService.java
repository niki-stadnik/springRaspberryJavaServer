package net.github.nikistadnik.springRaspberryJavaServer.storage;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.DeviceRegistry;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//https://www.baeldung.com/spring-data-jpa-query

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    private final StorageRepository storageRepository;
    private final DeviceRegistry deviceRegistry;

    //get all rows
    public List<Storage> getStorage() {
        System.out.println("StorageService");
        List<Storage> testl = storageRepository.findAll();
        System.out.println("direct print: " + testl);

        //list object to JSON
        JSONArray jo = new JSONArray(testl);
        System.out.println("jo: " + jo);
        for (int i = 0; i < jo.length(); ++i) {
            JSONObject rec = jo.getJSONObject(i);
            int id = rec.getInt("id");
            System.out.println("id: " + id);
            // ...
        }
        ////////////////////

        return storageRepository.findAll();
    }

    //get only row with id 1 (implemented in Repository)
    public List<Storage> getbyID(int x) {
        return storageRepository.findid(x);
    }

    public void insert(Storage storage) {
        System.out.println(storage);
        storageRepository.save(storage);
    }

    @Scheduled(initialDelay = 10000, fixedRate = 60000)
    public void insertIns() {
        Storage ins = new Storage();
             ins.bathTemp1(deviceRegistry.bathroomFanState().bathTemp1())
                .bathTemp2(deviceRegistry.bathroomFanState().bathTemp2())
                .bathHum1(deviceRegistry.bathroomFanState().bathHum1())
                .bathHum2(deviceRegistry.bathroomFanState().bathHum2())
                .bathFan(deviceRegistry.bathroomFanState().bathFan())
                .light0(deviceRegistry.lightSwitchState().light()[0].get())
                .light1(deviceRegistry.lightSwitchState().light()[1].get())
                .light2(deviceRegistry.lightSwitchState().light()[2].get())
                .light3(deviceRegistry.lightSwitchState().light()[3].get())
                .light4(deviceRegistry.lightSwitchState().light()[4].get())
                .light5(deviceRegistry.lightSwitchState().light()[5].get())
                .light6(deviceRegistry.lightSwitchState().light()[6].get());
        storageRepository.save(ins);
    }


    public void hii() {
        System.out.println("StorageController");
        List<Storage> tt = getbyID(1);
        System.out.println("getbyID: " + tt);
        System.out.println("get?: " + tt.get(1));
    }

    @Autowired
    private Environment env;

    @PostConstruct
    public void checkProfile() {
        log.info("Active profiles: {}", Arrays.toString(env.getActiveProfiles()));
    }


/*
    //To perform an insert operation, we have to both apply @Modifying and use a native query since INSERT is not a part of the JPA interface:
    @Modifying
    @Query(
            value =
                    "insert into Users (name, age, email, status) values (:name, :age, :email, :status)",
            nativeQuery = true)
    void insertUser(@Param("name") String name, @Param("age") Integer age,
                    @Param("status") Integer status, @Param("email") String email);

 */

}
