package portablejim.jimsmodmanager.config;

import com.google.gson.JsonObject;

/**
 * Config type that points to a folder relative to the json.
 */
public class ConfigFolder extends ConfigAbstract {
    String sourceFile = null;

    public ConfigFolder(JsonObject configJson) {
        super();

        sourceFile = configJson.get("url").getAsString();

    }

    @Override
    public String getSourceUrl() {
        return sourceFile;
    }
}
