package portablejim.jimsmodmanager.mod;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Assert;
import org.junit.Test;
import portablejim.jimsmodmanager.Model;

/**
 * Tests to test creating a mod definition from JSON.
 */
public class ModAbstractTest {
    @Test
    public void creationWithValidDataFilledCorrectly() {
        JsonParser p = new JsonParser();
        String input = "{ 'name': 'Testing', 'id': 'test', 'side': 'common', 'version': '1', 'sha1': '0' }";
        JsonObject json = p.parse(input).getAsJsonObject();

        class TestMod extends ModAbstract {
            public TestMod(JsonObject json) {
                super(json);
            }
        }

        ModAbstract mod = new TestMod(json);

        Assert.assertEquals("Testing", mod.name);
        Assert.assertEquals("test", mod.id);
        Assert.assertEquals(Model.SIDE.COMMON, mod.side);
        Assert.assertEquals("1", mod.version);
        Assert.assertEquals("0", mod.getSha1String());
    }

    @Test
    public void creationWithIdFilledNullsValues() {
        JsonParser p = new JsonParser();
        String input = "{ 'id': 'test' }";
        JsonObject json = p.parse(input).getAsJsonObject();

        class TestMod extends ModAbstract {
            public TestMod(JsonObject json) {
                super(json);
            }
        }

        ModAbstract mod = new TestMod(json);

        Assert.assertNull(mod.name);
        Assert.assertEquals("test", mod.id);
        Assert.assertEquals(Model.SIDE.COMMON, mod.side);
        Assert.assertNull(mod.version);
        Assert.assertNull(mod.getSha1String());
    }

    @Test(expected = NullPointerException.class)
    public void creationWithNoIdThrowsException() {
        JsonParser p = new JsonParser();
        String input = "{ 'name': 'Testing', 'side': 'common', 'version': '1', 'sha1': '0' }";
        JsonObject json = p.parse(input).getAsJsonObject();

        class TestMod extends ModAbstract {
            public TestMod(JsonObject json) {
                super(json);
            }
        }

        //noinspection UnusedDeclaration
        ModAbstract mod = new TestMod(json);
    }

    @Test
    public void creationWithInValidDataNullsCorrectly() {
        JsonParser p = new JsonParser();
        String input = "{ 'name': 'Testing', 'id': 'test', 'side': 'common', 'version': {}, 'sha1': '0' }";
        JsonObject json = p.parse(input).getAsJsonObject();

        class TestMod extends ModAbstract {
            public TestMod(JsonObject json) {
                super(json);
            }
        }

        ModAbstract mod = new TestMod(json);

        Assert.assertEquals("Testing", mod.name);
        Assert.assertEquals("test", mod.id);
        Assert.assertEquals(Model.SIDE.COMMON, mod.side);
        Assert.assertNull(mod.version);
        Assert.assertEquals("0", mod.getSha1String());
    }
}
