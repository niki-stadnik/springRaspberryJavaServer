package net.github.nikistadnik.springRaspberryJavaServer.storage;

import lombok.RequiredArgsConstructor;
import net.github.nikistadnik.springRaspberryJavaServer.GMailer;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/storage")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;
    //private final GMailer gMailer;



    @GetMapping
    public List<Storage> hi(){
        //List<Storage> tt = storageService.getbyID(1);
        //System.out.println("getbyID: " + tt);
        return storageService.getStorage();
    }



    @PostMapping
    public void insert(Storage storage){
        //storageService.insert(storage);
    }
}
