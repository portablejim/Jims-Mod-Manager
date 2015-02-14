package portablejim.jimsmodmanager;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
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

    @Test
    public void getModpackJsonWorks() {
        String json1 = "{ 'Name': 'testpack1' }";
        String json2 = "{ 'Name': 'testpack2' }";
        JsonParser parser = new JsonParser();
        try {
            TemporaryFolder testModpackDir = new TemporaryFolder();
            testModpackDir.create();
            File testModpackFile = testModpackDir.newFile("modpack.json");
            FileUtils.writeStringToFile(testModpackFile, json1);
            ManagerBackend backend = new ManagerBackend();
            JsonElement output1 = backend.getModpackJson(testModpackFile);
            Assert.assertEquals(output1, parser.parse(json1));
            FileUtils.writeStringToFile(testModpackFile, json2);
            JsonElement output2 = backend.getModpackJson(testModpackFile);
            Assert.assertEquals(output2, parser.parse(json2));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
