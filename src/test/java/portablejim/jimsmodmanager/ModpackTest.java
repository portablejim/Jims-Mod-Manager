package portablejim.jimsmodmanager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Test the functions on a Modpack definition.
 */
public class ModpackTest {
    static JsonElement modpackJson;

    @BeforeClass
    public static void setUp() {
        try {
            String s = FileUtils.readFileToString(new File("examples/modpack-example1.json"));
            JsonParser p = new JsonParser();
            modpackJson = p.parse(s);
        } catch (IOException e) {
            e.printStackTrace();
            modpackJson = new JsonObject();
        }
    }

    @Test
    public void testNewModpackWorks() {
        Modpack m = new Modpack(modpackJson);
        Assert.assertNotNull(m);
    }

    @Test
    public void testModPackGetName() {
        Modpack m = new Modpack(modpackJson);
        String name = m.getName();
        Assert.assertEquals("Test Pack 1", name);
    }
}
