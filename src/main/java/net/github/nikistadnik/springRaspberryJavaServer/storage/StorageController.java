package net.github.nikistadnik.springRaspberryJavaServer.storage;

import lombok.RequiredArgsConstructor;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.DeviceRegistry;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/storage")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;
    private final DeviceRegistry deviceRegistry;


    @GetMapping
    public List<Storage> hi(){
        System.out.println("StorageController");
        //insert(test);
        List<Storage> tt = storageService.getbyID(379);
        System.out.println("getbyID: " + tt);
        return storageService.getStorage();
    }

    Storage test = new Storage(
            125.5,
            110.2,
            62.6,
            26.7,
            false
    );


    //@Scheduled(fixedRate = 10000)
    public void insertIns(){
        Storage ins = new Storage(
                deviceRegistry.bathroomFanState().bathTemp1(),
                deviceRegistry.bathroomFanState().bathTemp2(),
                deviceRegistry.bathroomFanState().bathHum1(),
                deviceRegistry.bathroomFanState().bathHum2(),
                deviceRegistry.bathroomFanState().bathFan()
        );
        if (deviceRegistry.bathroomFanState().bathTemp1() != null) {
            storageService.insert(ins);
        }
        hii();
    }

    public void hii(){
        System.out.println("StorageController");
        //insert(test);
        List<Storage> tt = storageService.getbyID(379);
        System.out.println("getbyID: " + tt);
        //System.out.println("get?: " + tt.get(1));
    }

    @PostMapping
    public void insert(Storage storage){
        storageService.insert(storage);
    }
}
