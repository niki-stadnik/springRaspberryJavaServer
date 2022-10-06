package net.github.nikistadnik.springRaspberryJavaServer.storage;

import net.github.nikistadnik.springRaspberryJavaServer.TempStorage;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.BathroomFan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/storage")
public class StorageController {

    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

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
            62.6,
            26.7,
            false
    );


    @Scheduled(fixedRate = 10000)
    public void insertIns(){
        Storage ins = new Storage(
                BathroomFan.bathTemp,
                BathroomFan.bathHum,
                BathroomFan.bathLight,
                BathroomFan.bathFan
        );
        if (BathroomFan.bathTemp != null) {
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
