package portablejim.jimsmodmanager.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by james on 16/02/15.
 */
public class ConfigFolderTest {
    @Test
    public void configCreateWorksWithSlash() {
        JsonParser p = new JsonParser();
        JsonObject jsonObject = p.parse("{ 'source': 'folder', 'url': 'config/' }").getAsJsonObject();
        TemporaryFolder f = new TemporaryFolder();
        File mcDir = null;
        try {
            f.create();
            mcDir = f.getRoot();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ConfigFolder config = new ConfigFolder(jsonObject);
        Assert.assertNotNull(config);
    }

    @Test
    public void configCreateWorksWithoutSlash() {
        JsonParser p = new JsonParser();
        JsonObject jsonObject = p.parse("{ 'source': 'folder', 'url': 'config' }").getAsJsonObject();
        ConfigFolder config = new ConfigFolder(jsonObject);
        Assert.assertNotNull(config);
    }

    @Test
    public void configCreateWorksWithDot() {
        JsonParser p = new JsonParser();
        JsonObject jsonObject = p.parse("{ 'source': 'folder', 'url': '.' }").getAsJsonObject();
        ConfigFolder config = new ConfigFolder(jsonObject);
        Assert.assertNotNull(config);
    }

    @Test
    public void configCreateWorksEmpty() {
        JsonParser p = new JsonParser();
        JsonObject jsonObject = p.parse("{ 'source': 'folder', 'url': '' }").getAsJsonObject();
        ConfigFolder config = new ConfigFolder(jsonObject);
        Assert.assertNotNull(config);
    }

    @Test
    public void getSourceUrlWorksAZ() {
        JsonParser p = new JsonParser();
        for(String name : testRandomText()) {
            JsonObject jsonObject = p.parse(String.format("{ 'source': 'folder', 'url': '%s' }", name)).getAsJsonObject();
            ConfigAbstract config = new ConfigFolder(jsonObject);
            String url = config.getSourceUrl();
            Assert.assertEquals(name, url);
        }
    }

    private ArrayList<String> testRandomText() {
        ArrayList<String> output = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            int l = r.nextInt(50);
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < l; j++) {
                sb.append(r.nextInt(26) + 'a');
                sb.append(Character.toChars(r.nextInt(26) + 'a'));
            }
            output.add(sb.toString());

        }
        return output;
    }

    private ArrayList<String> testRandomTextAll() {
        ArrayList<String> output = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            int l = r.nextInt(50);
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < l; j++) {
                sb.append(Character.toChars(r.nextInt(105) + '!'));
            }
            output.add(sb.toString());

        }
        return output;
    }
}
