package portablejim.jimsmodmanager;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Main backend for the manager.
 */
public class ManagerBackend {
    public boolean hasValidModpack(File minecraftDir) {
        File modpackDir = new File(minecraftDir, "jmm-modpack");
        File modpackFile = new File(modpackDir, "modpack.json");

        JSONObject modpack = null;
        try {
            String modpackString = IOUtils.toString(modpackFile.toURI());
            modpack = new JSONObject(modpackString);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }


        return true;
    }
}
