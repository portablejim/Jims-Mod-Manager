package portablejim.jimsmodmanager;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.launchwrapper.LogWrapper;

import java.io.File;
import java.util.List;

/**
 * Tweaker used as entry point to mod manager.
 */
public class ModManager implements ITweaker {
    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        LogWrapper.info("ModManager: acceptOptions()");
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        LogWrapper.info("ModManager: injectIntoClassLoader()");
        //classLoader.
    }

    @Override
    public String getLaunchTarget() {
        return "portablejim.jimsmodmanager.DummyLaunchTarget";
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }
}
