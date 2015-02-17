package portablejim.jimsmodmanager.configaction;

import org.apache.commons.io.FileUtils;
import portablejim.jimsmodmanager.config.ConfigFolder;

import java.io.File;
import java.io.IOException;

/**
 * Perform actions if the config files are in a folder.
 */
public class ConfigActionFolder extends ConfigActionAbstract {
    public ConfigActionFolder(ConfigFolder config, File configDir) {
        super(config, configDir);
    }

    @Override
    public void copyTo(File location) {
        File sourceDir = new File(config.getSourceUrl());
        if(!sourceDir.isAbsolute()) {
            sourceDir = new File(configDir, config.getSourceUrl());
        }
        if(sourceDir.exists()) {
            File[] files = sourceDir.listFiles();
            if(files == null) {
                return;
            }
            for(File child : files) {
                try {
                    File targetLocation = new File(location, child.getName());
                    if (child.isDirectory()) {
                        FileUtils.copyDirectory(child, targetLocation);
                    }
                    else {
                        FileUtils.copyFile(child, targetLocation);
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
