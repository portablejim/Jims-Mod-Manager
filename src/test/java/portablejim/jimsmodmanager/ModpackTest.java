package portablejim.jimsmodmanager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import groovy.json.JsonException;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import portablejim.jimsmodmanager.config.ConfigFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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
    public void testNewModpackWorksRandom() {
        JsonParser parser = new JsonParser();
        for(String name : testRandomText()) {
            String jsonText = String.format("{ 'name': '%s', 'config': {}, 'mods': [] }", name);
            JsonElement json = parser.parse(jsonText);
            Modpack m = new Modpack(json);
            Assert.assertNotNull(m);
        }
    }

    @Test
    public void testInvalidJsonCausesException() {
        JsonParser parser = new JsonParser();
        ArrayList<String> jsonText = new ArrayList<>();
        jsonText.add("{ 'name': '%s', 'config': {} }");
        jsonText.add("{ 'name': '%s', 'mods': [] }");
        jsonText.add("{ 'name': '%s' }");
        jsonText.add("{ 'config': {} }");
        jsonText.add("{ 'mods': [] }");
        jsonText.add("{ }");
        for(String jsonString : jsonText) {
            try {
                JsonElement json = parser.parse(jsonString);
                new Modpack(json);
                Assert.fail();
            }
            catch (NullPointerException e) {
                e.printStackTrace();
                Assert.fail();
            }
            catch (JsonException e) {
                Assert.assertTrue(true);
            }
        }
    }

    @Test
    public void testModpackNameWorksRandom() {
        JsonParser parser = new JsonParser();
        for(String name : testRandomText()) {
            String jsonText = String.format("{ 'name': '%s', 'config': {}, 'mods': [] }", name);
            JsonElement json = parser.parse(jsonText);
            Modpack m = new Modpack(json);
            Assert.assertNotNull(m);
        }
    }

    private ArrayList<String> testRandomText() {
        ArrayList<String> output = new ArrayList<>();
        output.add("");
        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            int l = r.nextInt(50);
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < l; j++) {
                sb.append(r.nextInt(26) + 'a');
            }
            output.add(sb.toString());

        }
        return output;
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
