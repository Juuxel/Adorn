package juuxel.adorn.gradle.util.zip;

import groovy.json.JsonOutput;
import groovy.json.JsonSlurper;

import java.nio.charset.StandardCharsets;

/**
 * A simple zip transformation step that minifies all JSON files using {@link JsonSlurper} and {@link JsonOutput}.
 */
public final class MinifyJson implements ZipTransformer.Step {
    @Override
    public boolean shouldApply(String name) {
        return name.endsWith(".json") || name.endsWith(".mcmeta");
    }

    @Override
    public byte[] transform(String name, byte[] input, ZipTransformer.Filer filer) {
        var read = new JsonSlurper().parseText(new String(input, StandardCharsets.UTF_8));
        var json = JsonOutput.toJson(read);
        return json.getBytes(StandardCharsets.UTF_8);
    }
}
