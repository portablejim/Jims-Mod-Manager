package portablejim.jimsmodmanager.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;

/**
 * Config type that points to a folder relative to the json.
 */
public class ConfigFolder extends ConfigAbstract {
    File sourceFile = null;

    public ConfigFolder(JsonObject configJson) {
        super();

        JsonElement url = configJson.get("url");

    }

    @Override
    public String getSourceUrl() {
        return null;
    }
}
