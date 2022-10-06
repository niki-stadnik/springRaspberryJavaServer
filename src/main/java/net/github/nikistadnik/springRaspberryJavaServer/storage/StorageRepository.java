package net.github.nikistadnik.springRaspberryJavaServer.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {

    @Query(
            value = "SELECT * FROM storage WHERE id = :id",
            nativeQuery = true)
    List<Storage> findid(int id);
}
