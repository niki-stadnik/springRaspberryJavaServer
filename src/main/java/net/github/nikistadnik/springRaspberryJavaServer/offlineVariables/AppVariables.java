package net.github.nikistadnik.springRaspberryJavaServer.offlineVariables;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "app_variables")
public class AppVariables {

    @Getter
    @Id
    private String key;

    @Setter
    @Getter
    private String value;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void touch() { this.updatedAt = LocalDateTime.now(); }

    // constructors, getters, setters
    public AppVariables() {}
    public AppVariables(String key, String value) { this.key = key; this.value = value; }
}