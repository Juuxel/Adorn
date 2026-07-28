package juuxel.adorn.gradle.util.zip;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.gradle.api.provider.Property;

import java.nio.charset.StandardCharsets;

public abstract class InjectJijMetadata implements ZipTransformer.Step {
    public abstract Property<String> getGroup();
    public abstract Property<String> getModId();
    public abstract Property<String> getVersion();
    public abstract Property<String> getFilePath();

    private final String targetFileName;

    protected InjectJijMetadata(String targetFileName) {
        this.targetFileName = targetFileName;
    }

    @Override
    public boolean shouldApply(String name) {
        return targetFileName.equals(name);
    }

    @Override
    public byte[] transform(String name, byte[] input, ZipTransformer.Filer filer) {
        var gson = new Gson();
        var json = gson.fromJson(new String(input, StandardCharsets.UTF_8), JsonObject.class);
        transform(json);
        return gson.toJson(json).getBytes(StandardCharsets.UTF_8);
    }

    protected abstract void transform(JsonObject json);

    public abstract static class FabricModJson extends InjectJijMetadata {
        public FabricModJson() {
            super("fabric.mod.json");
        }

        @Override
        protected void transform(JsonObject json) {
            var obj = new JsonObject();
            obj.addProperty("file", getFilePath().get());
            json.getAsJsonArray("jars").add(obj);
        }
    }

    public abstract static class NeoForgeJarJar extends InjectJijMetadata {
        public NeoForgeJarJar() {
            super("META-INF/jarjar/metadata.json");
        }

        @Override
        protected void transform(JsonObject json) {
            var identifier = new JsonObject();
            identifier.addProperty("group", getGroup().get());
            identifier.addProperty("artifact", getModId().get());
            var version = getVersion().get();
            var versionData = new JsonObject();
            versionData.addProperty("range", "[" + version + ",)");
            versionData.addProperty("artifactVersion", version);

            var obj = new JsonObject();
            obj.add("identifier", identifier);
            obj.add("version", versionData);
            obj.addProperty("path", getFilePath().get());

            json.getAsJsonArray("jars").add(obj);
        }
    }
}
