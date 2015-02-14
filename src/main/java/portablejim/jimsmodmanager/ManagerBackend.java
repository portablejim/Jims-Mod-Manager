package portablejim.jimsmodmanager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.minecraft.launchwrapper.LogWrapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;

/**
 * Main backend for the manager.
 */
public class ManagerBackend {
    public boolean hasValidModpack(File minecraftDir) {
        File modpackDir = new File(minecraftDir, "jmm-modpack");
        File modpackFile = new File(modpackDir, "modpack.json");
        JsonParser parser = new JsonParser();

        JsonElement modpack = null;
        try {
            String modpackString = IOUtils.toString(modpackFile.toURI());
            modpack = parser.parse(modpackString);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (JsonSyntaxException e) {
            LogWrapper.warning("Error parsing modpack json. File: %s", modpackFile.getPath());
            LogWrapper.warning("Json error: %s", e.getLocalizedMessage());
            return false;
        }

        return true;
    }

    public JsonElement getModpackJson(File modpackFile) {
        JsonParser parser = new JsonParser();
        try {
            String packString = FileUtils.readFileToString(modpackFile);
            return parser.parse(packString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JsonObject();
    }
}
