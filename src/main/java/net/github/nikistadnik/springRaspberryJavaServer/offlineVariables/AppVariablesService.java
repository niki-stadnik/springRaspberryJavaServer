package net.github.nikistadnik.springRaspberryJavaServer.offlineVariables;

import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class AppVariablesService {

    private final AppVariablesRepository repo;

    public AppVariablesService(AppVariablesRepository repo) {
        this.repo = repo;
    }

    public String get(String key, String defaultValue) {
        return repo.findById(key)
                .map(AppVariables::getValue)
                .orElse(defaultValue);
    }

    public void set(String key, String value) {
        AppVariables setting = repo.findById(key)
                .orElse(new AppVariables(key, null));
        setting.setValue(value);
        repo.save(setting);
    }

    // typed helpers
    public int getInt(String key, int def)          { return Integer.parseInt(get(key, String.valueOf(def))); }
    public boolean getBoolean(String key, boolean def) { return Boolean.parseBoolean(get(key, String.valueOf(def))); }
    public String getString(String key, String def) { return get(key, def); }
    public LocalTime getLocalTime(String key, LocalTime defaultValue) {
        return get(key, null) != null
                ? LocalTime.parse(get(key, null))
                : defaultValue;
    }
    public void setInt(String key, int v)           { set(key, String.valueOf(v)); }
    public void setBoolean(String key, boolean v)   { set(key, String.valueOf(v)); }
    public void setString(String key, String v)     { set(key, v); }
    public void setLocalTime(String key, LocalTime value) {
        set(key, value.toString());
    }
}