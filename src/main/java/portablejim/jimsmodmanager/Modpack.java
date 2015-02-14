package portablejim.jimsmodmanager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Definiton of a modpack.
 */
public class Modpack {
    private String name;
    public Modpack(JsonElement modpackJson) {
        JsonObject modpackJsonObject = modpackJson.getAsJsonObject();
        name = modpackJsonObject.get("name").getAsString();
    }

    public String getName() {
        return name;
    }
}
