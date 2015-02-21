package portablejim.jimsmodmanager.mod;

import com.google.gson.JsonObject;
import portablejim.jimsmodmanager.Model;

import java.math.BigInteger;

/**
 * Mod config from json file.
 */
public abstract class ModAbstract {
    protected String id;
    public String name;
    public Model.SIDE side;
    public String version;
    public String outputfilename;
    protected BigInteger sha1;

    public ModAbstract(JsonObject json) {
        this.id = json.get("id").getAsString();
        this.name = tryJsonString(json, "name");
        this.version = tryJsonString(json, "version");
        this.outputfilename = tryJsonString(json, "file");

        this.side = Model.SIDE.COMMON;
        String sideString = tryJsonString(json, "side");
        if(sideString != null) {
            if("client".equals(sideString)) {
                this.side = Model.SIDE.CLIENT;
            }
            else if("server".equals(sideString)) {
                this.side = Model.SIDE.SERVER;
            }
        }
        String sha1String = tryJsonString(json, "sha1");
        if(sha1String != null && !sha1String.isEmpty()) {
            setSha1(sha1String);
        }
    }

    private String tryJsonString(JsonObject json, String key) {
        try {
            return json.get(key).getAsString();
        }
        catch (Exception e) {
            // Not correct, ignoring
        }
        return null;
    }

    public boolean setSha1(String sha1) {
        try {
            this.sha1 = new BigInteger(sha1, 16);
            return true;
        }
        catch (NumberFormatException ignored) {
            // Illegal value, ignoring.
            this.sha1 = null;
        }
        return false;
    }

    public String getSha1String() {
        return sha1 == null ? null : sha1.toString(16);
    }
}
