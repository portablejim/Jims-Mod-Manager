package portablejim.jimsmodmanager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import portablejim.jimsmodmanager.config.ConfigAbstract;
import portablejim.jimsmodmanager.configaction.ConfigActionAbstract;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Tests for the backend.
 */
public class ManagerBackendTest {
    @Rule
    public TemporaryFolder testMinecraftDir = new TemporaryFolder();

    @Test
    public void getLocalModpackRunsProcessModpackIfValid() {
        Model model = new Model();
        File testModPackDir = new File(testMinecraftDir.getRoot(), "jmm-modpack");
        //noinspection ResultOfMethodCallIgnored
        testModPackDir.mkdir();
        File testModPackFile = new File(testModPackDir, "modpack.json");
        try {
            FileUtils.writeStringToFile(testModPackFile, "{ 'name': 'Test Pack', 'config': {}, 'mods': [] }");
        } catch (IOException e) {
            e.printStackTrace();
        }

        AtomicBoolean called = new AtomicBoolean();
        called.set(false);
        ManagerBackend backend = new ManagerBackend(model, testMinecraftDir.getRoot()) {
            public void processModpack(Modpack modpack) {
                called.set(true);
            }
        };

        backend.getLocalModpack("jmm-modpack", "modpack.json");

        Assert.assertTrue("processModpack() not called.", called.get());
    }

    @Test
    public void getLocalModpackSetsModelIfValid() {
        Model model = new Model();
        File testModPackDir = new File(testMinecraftDir.getRoot(), "jmm-modpack");
        //noinspection ResultOfMethodCallIgnored
        testModPackDir.mkdir();
        File testModPackFile = new File(testModPackDir, "modpack.json");
        try {
            FileUtils.writeStringToFile(testModPackFile, "{ 'name': 'Test Pack', 'config': {}, 'mods': [] }");
        } catch (IOException e) {
            e.printStackTrace();
        }

        ManagerBackend backend = new ManagerBackend(model, testMinecraftDir.getRoot()) {
            public void processModpack(Modpack modpack) {
            }
        };

        backend.getLocalModpack("jmm-modpack", "modpack.json");
        int modPackPercent = model.getModpack_json_progress();

        Assert.assertEquals(100, modPackPercent);
    }

    @Test
    public void getLocalModpackDoesNotRunsProcessModpackIfHasValidModpackIsFalse() {
        Model model = new Model();

        AtomicBoolean called = new AtomicBoolean();
        called.set(false);
        ManagerBackend backend = new ManagerBackend(model, testMinecraftDir.getRoot()) {
            public boolean hasValidModpack(File modpackFile) {
                return false;
            }
            public void processModpack(Modpack modpack) {
                called.set(true);
            }
        };

        backend.getLocalModpack("jmm-modpack", "modpack.json");

        Assert.assertFalse("processModpack() is called.", called.get());
    }

    @Test
    public void getLocalModpackSetsModelIfHasValidModpackIsFalse() {
        Model model = new Model();

        ManagerBackend backend = new ManagerBackend(model, testMinecraftDir.getRoot()) {
            public boolean hasValidModpack(File modpackFile) {
                return false;
            }
        };

        model.setModpack_json_progress(-1);

        backend.getLocalModpack("jmm-modpack", "modpack.json");
        int modPackPercent = model.getModpack_json_progress();

        Assert.assertEquals(0, modPackPercent);
    }

    ConfigAbstract passedAbstractCommon;
    ConfigAbstract passedAbstractSide;
    @Test
    public void processModpackRunsProcessConfigWithNullIfNoPackSpecificed() throws IOException {
        Model model = new Model();

        passedAbstractCommon = new ConfigTest();
        passedAbstractSide = new ConfigTest();

        ManagerBackend backend = new ManagerBackend(model, testMinecraftDir.getRoot()) {
            public void processConfigs(ConfigAbstract configCommon, ConfigAbstract configSide) {
                passedAbstractCommon = configCommon;
                passedAbstractSide = configSide;
            }
        };

        JsonParser p = new JsonParser();
        JsonObject json = p.parse("{ 'name': 'Testpack', 'config': {}, 'mods': [] }").getAsJsonObject();

        Modpack modpack = new Modpack(json);
        backend.processModpack(modpack);

        Assert.assertNull("Abstract common was not null", passedAbstractCommon);
        Assert.assertNull("Abstract side was not null", passedAbstractSide);

        model.setSide(Model.SIDE.CLIENT);

        modpack = new Modpack(json);
        backend.processModpack(modpack);

        Assert.assertNull("Abstract common was not null", passedAbstractCommon);
        Assert.assertNull("Abstract side was not null", passedAbstractSide);

        model.setSide(Model.SIDE.SERVER);

        modpack = new Modpack(json);
        backend.processModpack(modpack);

        Assert.assertNull("Abstract common was not null", passedAbstractCommon);
        Assert.assertNull("Abstract side was not null", passedAbstractSide);
    }

    @Test
    public void processModpackPassesCommonSideBothSpecified() throws IOException {
        Model model = new Model();

        passedAbstractCommon = new ConfigTest();
        passedAbstractSide = new ConfigTest();

        ManagerBackend backend = new ManagerBackend(model, testMinecraftDir.getRoot()) {
            public void processConfigs(ConfigAbstract configCommon, ConfigAbstract configSide) {
                passedAbstractCommon = configCommon;
                passedAbstractSide = configSide;
            }
        };

        JsonParser p = new JsonParser();
        JsonElement json = p.parse("{ 'name': 'Testpack', " +
                "'config': { " +
                "'common': { 'source': 'folder', 'url': 'config/' }, " +
                "'client': { 'source': 'folder', 'url': 'config-client/' }, " +
                "'server': { 'source': 'folder', 'url': 'config-server/' } " +
                "}, 'mods': [] }");
        Modpack modpack = new Modpack(json);

        backend.processModpack(modpack);

        Assert.assertNotNull("configCommon common was null", passedAbstractCommon);
        Assert.assertEquals("configCommon was not using the common config", modpack.getConfigCommon(), passedAbstractCommon);
    }

    @Test
    public void processModpackPassesCommonNullSideSidesSpecified() throws IOException {
        Model model = new Model();

        passedAbstractCommon = new ConfigTest();
        passedAbstractSide = new ConfigTest();

        ManagerBackend backend = new ManagerBackend(model, testMinecraftDir.getRoot()) {
            public void processConfigs(ConfigAbstract configCommon, ConfigAbstract configSide) {
                passedAbstractCommon = configCommon;
                passedAbstractSide = configSide;
            }
        };

        JsonParser p = new JsonParser();
        JsonElement json = p.parse("{ 'name': 'Testpack', " +
                "'config': { " +
                "'client': { 'source': 'folder', 'url': 'config-client/' }, " +
                "'server': { 'source': 'folder', 'url': 'config-server/' } " +
                "}, 'mods': [] }");
        Modpack modpack = new Modpack(json);

        backend.processModpack(modpack);

        Assert.assertNull("Abstract common was not null", passedAbstractCommon);
    }

    @Test
    public void processModpackPassesClientSideClient() throws IOException {
        Model model = new Model();

        passedAbstractCommon = new ConfigTest();
        passedAbstractSide = new ConfigTest();

        ManagerBackend backend = new ManagerBackend(model, testMinecraftDir.getRoot()) {
            public void processConfigs(ConfigAbstract configCommon, ConfigAbstract configSide) {
                passedAbstractCommon = configCommon;
                passedAbstractSide = configSide;
            }
        };

        model.setSide(Model.SIDE.CLIENT);

        JsonParser p = new JsonParser();
        JsonElement json = p.parse("{ 'name': 'Testpack', " +
                "'config': { 'client': { 'source': 'folder', 'url': 'config-client/' } }, " +
                "'mods': [] }");
        Modpack modpack = new Modpack(json);

        backend.processModpack(modpack);

        Assert.assertNotNull("configClient common was null", passedAbstractSide);
        Assert.assertEquals("configClient was not using the common config", modpack.getConfigClient(), passedAbstractSide);
    }

    @Test
    public void processModpackPassesClientSideServer() throws IOException {
        Model model = new Model();

        passedAbstractCommon = new ConfigTest();
        passedAbstractSide = new ConfigTest();

        ManagerBackend backend = new ManagerBackend(model, testMinecraftDir.getRoot()) {
            public void processConfigs(ConfigAbstract configCommon, ConfigAbstract configSide) {
                passedAbstractCommon = configCommon;
                passedAbstractSide = configSide;
            }
        };

        model.setSide(Model.SIDE.SERVER);

        JsonParser p = new JsonParser();
        JsonElement json = p.parse("{ 'name': 'Testpack', " +
                "'config': { 'server': { 'source': 'folder', 'url': 'config-server/' } }, " +
                "'mods': [] }");
        Modpack modpack = new Modpack(json);

        backend.processModpack(modpack);

        Assert.assertNotNull("configServer common was null", passedAbstractSide);
        Assert.assertEquals("configServer was not using the common config", modpack.getConfigServer(), passedAbstractSide);
    }

    @Test
    public void hadValidModpackReturnsTrueWhenValidJSON() throws IOException {
        TemporaryFolder testMinecraftDir = new TemporaryFolder();
        testMinecraftDir.create();
        File testModpackDir = testMinecraftDir.newFolder("jmm-modpack");
        File testJsonFile = new File(testModpackDir, "modpack.json");
        FileWriter fw = new FileWriter(testJsonFile);
        fw.write("{ 'Name': 'testpack' }");
        fw.close();

        ManagerBackend backend = new ManagerBackend();
        Assert.assertTrue(backend.hasValidModpack(testJsonFile));
    }

    @Test
    public void hadValidModpackReturnsFalseWhenInvalidJSON() throws IOException {
        TemporaryFolder testMinecraftDir = new TemporaryFolder();
        testMinecraftDir.create();
        File testModpackDir = testMinecraftDir.newFolder("jmm-modpack");
        File testJsonFile = new File(testModpackDir, "modpack.json");
        FileWriter fw = new FileWriter(testJsonFile);
        fw.write("[ 'Name': 'testpack' }");
        fw.close();

        ManagerBackend backend = new ManagerBackend();
        Assert.assertFalse(backend.hasValidModpack(testJsonFile));
    }

    @Test
    public void getModpackJsonWorks() throws IOException {
        String json1 = "{ 'Name': 'testpack1' }";
        String json2 = "{ 'Name': 'testpack2' }";
        JsonParser parser = new JsonParser();

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
    }

    public static int i = 0;
    class ConfigTest extends ConfigAbstract {
        public int called = 0;

        @Override
        public String getSourceUrl() {
            called++;
            return "config/";
        }
    }

    class ConfigActionTest extends ConfigActionAbstract {
        public int copyToCalled = 0;

        public ConfigActionTest(File configDir) {
            super(new ConfigTest(), configDir);
        }

        @Override
        public void copyTo(File location) {
            copyToCalled = i;
            i++;
        }
    }

    @Test
    public void mergeConfigWorksWithBoth() {
        TemporaryFolder f = new TemporaryFolder();
        File modpackDir = null;
        File outputDir = null;
        try {
            f.create();
            modpackDir = f.getRoot();
            outputDir = f.newFolder();
        } catch (IOException e) {
            e.printStackTrace();
        }
        i = 0;
        ConfigActionTest configAction1 = new ConfigActionTest(modpackDir);
        ConfigActionTest configAction2 = new ConfigActionTest(modpackDir);
        ManagerBackend backend = new ManagerBackend();
        backend.mergeConfigs(outputDir, configAction1, configAction2);
        Assert.assertEquals(0, configAction1.copyToCalled);
        Assert.assertEquals(1, configAction2.copyToCalled);

    }

    @Test
    public void mergeConfigWorksWithSideOnly() {
        TemporaryFolder f = new TemporaryFolder();
        File modpackDir = null;
        File outputDir = null;
        try {
            f.create();
            modpackDir = f.getRoot();
            outputDir = f.newFolder();
        } catch (IOException e) {
            e.printStackTrace();
        }
        i = 0;
        ConfigActionTest configAction2 = new ConfigActionTest(modpackDir);
        ManagerBackend backend = new ManagerBackend();
        backend.mergeConfigs(outputDir, null, configAction2);
        Assert.assertEquals(0, configAction2.copyToCalled);

    }

    @Test
    public void mergeConfigWorksWithCommonOnly() {
        TemporaryFolder f = new TemporaryFolder();
        File modpackDir = null;
        File outputDir = null;
        try {
            f.create();
            modpackDir = f.getRoot();
            outputDir = f.newFolder();
        } catch (IOException e) {
            e.printStackTrace();
        }
        i = 0;
        ConfigActionTest configAction1 = new ConfigActionTest(modpackDir);
        ManagerBackend backend = new ManagerBackend();
        backend.mergeConfigs(outputDir, configAction1, null);
        Assert.assertEquals(0, configAction1.copyToCalled);

    }
}
