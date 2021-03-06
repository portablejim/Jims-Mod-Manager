package portablejim.jimsmodmanager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
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
            catch (JsonSyntaxException e) {
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
            Assert.assertEquals(name, m.getName());
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
    public void testGetConfigCommonWorks() {
        JsonParser parser = new JsonParser();
        String jsonText = "{ 'name': '%s', 'config': { 'common': { 'source': 'folder', 'url': 'config/' } }, 'mods': [] }";
        JsonElement modpackJson = parser.parse(jsonText);
        Modpack m1 = new Modpack(modpackJson);
        Assert.assertTrue(m1.getConfigCommon() instanceof ConfigFolder);
    }

    @Test
    public void testGetConfigClientWorks() {
        JsonParser parser = new JsonParser();
        String jsonText = "{ 'name': '%s', 'config': { 'client': { 'source': 'folder', 'url': 'config/' } }, 'mods': [] }";
        JsonElement modpackJson = parser.parse(jsonText);
        Modpack m1 = new Modpack(modpackJson);
        Assert.assertTrue(m1.getConfigClient() instanceof ConfigFolder);
    }

    @Test
    public void testGetConfigServerWorks() {
        JsonParser parser = new JsonParser();
        String jsonText = "{ 'name': '%s', 'config': { 'server': { 'source': 'folder', 'url': 'config/' } }, 'mods': [] }";
        JsonElement modpackJson = parser.parse(jsonText);
        Modpack m1 = new Modpack(modpackJson);
        Assert.assertTrue(m1.getConfigServer() instanceof ConfigFolder);
    }
}
