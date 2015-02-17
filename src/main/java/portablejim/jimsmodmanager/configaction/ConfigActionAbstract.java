package portablejim.jimsmodmanager.configaction;

import portablejim.jimsmodmanager.config.ConfigAbstract;

import java.io.File;

/**
 * Base class for actions performed on a config.
 */
public abstract class ConfigActionAbstract {
    protected ConfigAbstract config;
    File configDir;

    public ConfigActionAbstract(ConfigAbstract config, File configDir) {
        this.config = config;
        this.configDir = configDir;
    }

    public abstract void copyTo(File location);
}
