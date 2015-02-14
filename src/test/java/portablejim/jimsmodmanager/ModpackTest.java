package portablejim.jimsmodmanager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jdk.nashorn.internal.parser.JSONParser;
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
    JsonElement modpackJson;

    @BeforeClass
    public void setUp() {
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
}
