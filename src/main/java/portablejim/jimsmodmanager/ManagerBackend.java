package portablejim.jimsmodmanager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.minecraft.launchwrapper.LogWrapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import portablejim.jimsmodmanager.config.ConfigAbstract;
import portablejim.jimsmodmanager.config.ConfigFolder;
import portablejim.jimsmodmanager.configaction.ConfigActionAbstract;
import portablejim.jimsmodmanager.configaction.ConfigActionFolder;
import portablejim.jimsmodmanager.view.ILauncherFrontend;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static portablejim.jimsmodmanager.Model.STATE.DOWNLOADING_CONFIG_COMMON;
import static portablejim.jimsmodmanager.Model.STATE.DOWNLOADING_CONFIG_SIDE;
import static portablejim.jimsmodmanager.Model.STATE.DOWNLOADING_MODS;
import static portablejim.jimsmodmanager.Model.STATE.START;

/**
 * Main backend for the manager.
 */
public class ManagerBackend {
    private final Model model;
    private final ILauncherFrontend frontend;
    private final File minecraftDir;

    public ManagerBackend() {
        this(null, null, null);
    }
    public ManagerBackend(Model model, ILauncherFrontend frontend, File minecraftDir) {

        this.model = model;
        this.frontend = frontend;
        this.minecraftDir = minecraftDir;
    }

    private JsonObject packJson;
    private Modpack modpack;
    private ConfigActionAbstract configActionCommon;
    private ConfigActionAbstract configActionSide;
    public void process() {
        while(true) {
            switch (model.getStage()) {
                case START:
                    packJson = getLocalModpack(model, minecraftDir);
                    break;
                case DOWNLOADING_MODPACK:
                    return;
                case PROCESS_MODPACK:
                    modpack = new Modpack(packJson);

                    File modpackConfigDir = new File(minecraftDir, "jmm-modpack/");
                    ConfigAbstract configCommon = modpack.getConfigCommon();
                    ConfigAbstract configSide = null;
                    if(model.getSide() == Model.SIDE.CLIENT) {
                        configSide = modpack.getConfigClient();
                    }
                    else if(model.getSide() == Model.SIDE.SERVER) {
                        configSide = modpack.getConfigServer();
                    }
                    if(configCommon instanceof ConfigFolder) {
                        configActionCommon = new ConfigActionFolder((ConfigFolder) configCommon, modpackConfigDir);
                    }
                    if(configSide instanceof ConfigFolder) {
                        configActionSide = new ConfigActionFolder((ConfigFolder) configSide, modpackConfigDir);
                    }
                    model.setStage(DOWNLOADING_CONFIG_COMMON);
                    break;
                case DOWNLOADING_CONFIG_COMMON:
                    File configDir = new File(minecraftDir, "config/");
                    mergeConfigs(configDir, configActionCommon, configActionSide);
                case DOWNLOADING_MODS:
                    return;
           }
        }
    }

    public JsonObject getLocalModpack(Model model, File minecraftDir) {
        if(hasValidModpack(minecraftDir)) {
            File modpackDir = new File(minecraftDir, "jmm-modpack");
            File modpackFile = new File(modpackDir, "modpack.json");
            model.setStage(Model.STATE.PROCESS_MODPACK);
            return getModpackJson(modpackFile).getAsJsonObject();
        }
        model.setStage(Model.STATE.DOWNLOADING_MODPACK);
        return null;
    }

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

    public void mergeConfigs(File outputDir, ConfigActionAbstract configActionCommon, ConfigActionAbstract configActionSide) {
        if(configActionCommon != null) {
            configActionCommon.copyTo(outputDir);
        }
        model.setStage(DOWNLOADING_CONFIG_SIDE);
        if(configActionSide != null) {
            configActionSide.copyTo(outputDir);
        }
        model.setStage(DOWNLOADING_MODS);
    }
}
