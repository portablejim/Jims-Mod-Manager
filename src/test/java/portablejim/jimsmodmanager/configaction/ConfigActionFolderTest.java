package portablejim.jimsmodmanager.configaction;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import portablejim.jimsmodmanager.config.ConfigFolder;

import java.io.File;
import java.io.IOException;

/**
 * Unit tests for ConfigActionFolder.
 */
public class ConfigActionFolderTest {
    @Rule
    public TemporaryFolder f = new TemporaryFolder();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void copyToWorksSimple() {
        File configDir = null, sourceDir = null, targetDir = null, target1 = null, target2 = null;
        try {
            configDir = f.newFolder();

            sourceDir = new File(configDir, "source/");
            sourceDir.mkdir();
            targetDir = new File(configDir, "target/");
            sourceDir.mkdir();
            new File(sourceDir, "file1").createNewFile();
            new File(sourceDir, "file2").createNewFile();
            target1 = new File(targetDir, "file1");
            target2 = new File(targetDir, "file2");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonParser p = new JsonParser();
        assert sourceDir != null;
        JsonObject json = p.parse("{ 'type': 'folder', 'url': 'source/' }").getAsJsonObject();
        ConfigFolder configFolder = new ConfigFolder(json);
        ConfigActionFolder configActionFolder = new ConfigActionFolder(configFolder, configDir);
        configActionFolder.copyTo(targetDir);
        assert target1 != null;
        Assert.assertTrue("File 1 does not exist", target1.exists());
        Assert.assertTrue("File 2 does not exist", target2.exists());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void copyToWorksWithDirectories() {
        File configDir = null, sourceDir = null, targetDir = null, target1 = null, target2 = null, target3 = null, target4 = null;
        try {
            configDir = f.newFolder();

            sourceDir = new File(configDir, "source/");
            sourceDir.mkdir();
            targetDir = new File(configDir, "target/");
            sourceDir.mkdir();
            new File(sourceDir, "file1").createNewFile();
            File f2 = new File(sourceDir, "dir2");
            f2.mkdir();
            new File(f2, "file3").createNewFile();
            new File(f2, "file4").createNewFile();
            target1 = new File(targetDir, "file1");
            target2 = new File(targetDir, "dir2");
            target3 = new File(target2, "file3");
            target4 = new File(target2, "file4");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonParser p = new JsonParser();
        assert sourceDir != null;
        JsonObject json = p.parse("{ 'type': 'folder', 'url': 'source/' }").getAsJsonObject();
        ConfigFolder configFolder = new ConfigFolder(json);
        ConfigActionFolder configActionFolder = new ConfigActionFolder(configFolder, configDir);
        configActionFolder.copyTo(targetDir);
        assert target1 != null;
        Assert.assertTrue("File 1 does not exist", target1.exists());
        Assert.assertTrue("Directory 2 does not exist", target2.exists());
        Assert.assertTrue("File 3 does not exist", target3.exists());
        Assert.assertTrue("File 3 does not exist", target4.exists());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void copyToWorksAbsolutePath() {
        File configDir = null, sourceDir = null, targetDir = null, target1 = null, target2 = null;
        try {
            configDir = f.newFolder();

            sourceDir = new File(configDir, "source/");
            sourceDir.mkdir();
            targetDir = new File(configDir, "target/");
            sourceDir.mkdir();
            new File(sourceDir, "file1").createNewFile();
            new File(sourceDir, "file2").createNewFile();
            target1 = new File(targetDir, "file1");
            target2 = new File(targetDir, "file2");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonParser p = new JsonParser();
        assert sourceDir != null;
        JsonObject json = p.parse(String.format("{ 'type': 'folder', 'url': '%s' }", sourceDir.getAbsolutePath())).getAsJsonObject();
        ConfigFolder configFolder = new ConfigFolder(json);
        ConfigActionFolder configActionFolder = new ConfigActionFolder(configFolder, configDir);
        configActionFolder.copyTo(targetDir);
        assert target1 != null;
        Assert.assertTrue("File 1 does not exist", target1.exists());
        Assert.assertTrue("File 2 does not exist", target2.exists());
    }
}
