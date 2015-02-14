package portablejim.jimsmodmanager;

import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Tests for the backend.
 */
public class ManagerBackendTest {

    @Test
    public void hadValidModpackReturnsTrueWhenValidJSON() {
        try {
            TemporaryFolder testMinecraftDir = new TemporaryFolder();
            testMinecraftDir.create();
            File testModpackDir = testMinecraftDir.newFolder("jmm-modpack");
            File testJsonFile = new File(testModpackDir, "modpack.json");
            FileWriter fw = new FileWriter(testJsonFile);
            fw.write("{ 'Name': 'testpack' }");
            fw.close();

            ManagerBackend backend = new ManagerBackend();
            Assert.assertTrue(backend.hasValidModpack(testMinecraftDir.getRoot()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void hadValidModpackReturnsFalseWhenInvalidJSON() {
        try {
            TemporaryFolder testMinecraftDir = new TemporaryFolder();
            testMinecraftDir.create();
            File testModpackDir = testMinecraftDir.newFolder("jmm-modpack");
            File testJsonFile = new File(testModpackDir, "modpack.json");
            FileWriter fw = new FileWriter(testJsonFile);
            fw.write("[ 'Name': 'testpack' }");
            fw.close();

            ManagerBackend backend = new ManagerBackend();
            Assert.assertFalse(backend.hasValidModpack(testMinecraftDir.getRoot()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
