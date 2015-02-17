package portablejim.jimsmodmanager;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Data model. Stores state.
 */
public class Model {
    public static enum STATE {
        START,
        DOWNLOADING_MODPACK,
        PROCESS_MODPACK,
        DOWNLOADING_CONFIG_COMMON,
        DOWNLOADING_CONFIG_SIDE,
        PROCESSING_CONFIGS,
        DOWNLOADING_MODS,
        PROCESSING_MODS,
        LAUNCHING
    }
    public static enum SIDE {
        COMMON,
        CLIENT,
        SERVER
    }
    public class ModState {
        public String id;
        public String name;
        public String version;
        public long size;
        public String url;
        public int progress;
        public boolean enabled;
    }

    private STATE stage;
    private int error;
    private int modpack_json_progress;
    private int config_common_progress;
    private int config_sided_progress;
    private SIDE side;
    private ConcurrentHashMap<String, ModState> mods;

    public Model() {
        stage = STATE.START;
        error = 0;
        modpack_json_progress = 0;
        config_common_progress = 0;
        config_sided_progress = 0;
        side = SIDE.COMMON;
        mods = new ConcurrentHashMap<>();
    }

    public STATE getStage() {
        return stage;
    }

    public void setStage(STATE stage) {
        this.stage = stage;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getModpack_json_progress() {
        return modpack_json_progress;
    }

    public void setModpack_json_progress(int modpack_json_progress) {
        this.modpack_json_progress = modpack_json_progress;
    }

    public int getConfig_common_progress() {
        return config_common_progress;
    }

    public void setConfig_common_progress(int config_common_progress) {
        this.config_common_progress = config_common_progress;
    }

    public int getConfig_sided_progress() {
        return config_sided_progress;
    }

    public void setConfig_sided_progress(int config_sided_progress) {
        this.config_sided_progress = config_sided_progress;
    }

    public SIDE getSide() {
        return side;
    }

    public void setSide(SIDE side) {
        this.side = side;
    }

    public void addMod(String modId, String modName, String modVersion, String modUrl, boolean modEnabled) {
        ModState state = new ModState();
        state.id = modId;
        state.name = modName;
        state.version = modVersion;
        state.url = modUrl;
        state.enabled = modEnabled;
        state.size = 0;
        state.progress = 0;

        mods.put(modId, state);
    }

    public void setSize(String modId, int size) {
        if(mods.containsKey(modId)) {
            mods.get(modId).size = size;
        }
    }

    public void setProgress(String modId, int progress) {
        if(mods.containsKey(modId)) {
            mods.get(modId).progress = progress;
        }
    }

    public ModState getModState(String modId) {
        return mods.get(modId);
    }

}
