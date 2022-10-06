package net.github.nikistadnik.springRaspberryJavaServer.storage;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//https://www.baeldung.com/spring-data-jpa-query

@Service
public class StorageService {

    private final StorageRepository storageRepository;

    @Autowired
    public StorageService(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

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
