package portablejim.jimsmodmanager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import portablejim.jimsmodmanager.config.ConfigFolder;

import java.io.File;
import java.io.IOException;

/**
 * Test the functions on a Modpack definition.
 */
public class ModpackTest {
    static JsonElement modpackJson1;
    static JsonElement modpackJson2;

    @BeforeClass
    public static void setUp() {
        try {
            String s1 = FileUtils.readFileToString(new File("examples/modpack-example1.json"));
            String s2 = FileUtils.readFileToString(new File("examples/modpack-example2.json"));
            JsonParser p = new JsonParser();
            modpackJson1 = p.parse(s1);
            modpackJson2 = p.parse(s2);
        } catch (IOException e) {
            e.printStackTrace();
            modpackJson1 = new JsonObject();
        }
    }

    @Test
    public void testNewModpackWorks() {
        Modpack m = new Modpack(modpackJson1);
        Assert.assertNotNull(m);
    }

    @Test
    public void testModPackGetName() {
        Modpack m1 = new Modpack(modpackJson1);
        Assert.assertEquals("Test Pack 1", m1.getName());
        Modpack m2 = new Modpack(modpackJson2);
        Assert.assertEquals("Test Pack 2", m2.getName());
    }

    @Test
    public void testGetConfigCommonWorks() {
        Modpack m1 = new Modpack(modpackJson1);
        Assert.assertTrue(m1.getConfigCommon() instanceof ConfigFolder);
        Modpack m2 = new Modpack(modpackJson2);
        Assert.assertTrue(m2.getConfigCommon() instanceof ConfigFolder);
    }
}
