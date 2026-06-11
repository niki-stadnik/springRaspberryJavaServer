package net.github.nikistadnik.springRaspberryJavaServer.offlineVariables;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppVariablesRepository extends JpaRepository<AppVariables, String> {
}
