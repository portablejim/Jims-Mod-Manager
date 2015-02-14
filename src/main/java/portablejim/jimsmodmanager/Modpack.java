package portablejim.jimsmodmanager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import groovy.json.JsonException;
import portablejim.jimsmodmanager.config.ConfigAbstract;
import portablejim.jimsmodmanager.config.ConfigFolder;

import java.util.HashMap;

/**
 * Definiton of a modpack.
 */
public class Modpack {
    private String name;
    private HashMap<String,ConfigAbstract> config;

    public Modpack(JsonElement modpackJson) {
        JsonObject configJson;
        JsonObject modpackJsonObject = modpackJson.getAsJsonObject();

        if(modpackJsonObject.get("name") == null) throw new JsonException("No (or invalid) name field.");
        if(modpackJsonObject.get("config") == null) throw new JsonException("No (or invalid) config fields.");
        if(modpackJsonObject.get("mods") == null) throw new JsonException("No (or invalid) mod list.");


        name = modpackJsonObject.get("name").getAsString();

        config = new HashMap<>();

        String[] configTypes = {"common", "client", "server"};
        for (String type : configTypes) {
            configJson = modpackJsonObject.getAsJsonObject("config").getAsJsonObject(type);
            if(configJson == null || configJson.isJsonNull()) {
                continue;
            }
            String configType = configJson.get("source").getAsString();
            switch (configType) {
                case "folder":
                    config.put(type, new ConfigFolder(configJson));
            }
        }

        if(modpackJsonObject.get("mods") == null) {
            throw new JsonException("No (or invalid) mod list.");
        }
    }

    public String getName() {
        return name;
    }

    public ConfigAbstract getConfigCommon() {
        if(config.containsKey("common")) return config.get("common");
        else return null;
    }
}
