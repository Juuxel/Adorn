package juuxel.adorn.gradle.compatchecker;

import juuxel.adorn.datagen.Id;

import java.util.ArrayList;
import java.util.List;

public final class ModCompatChecker {
    private final List<Id> textures;
    private final List<Id> items;
    private final JarIndex modJar;
    private final List<String> missingFiles = new ArrayList<>();

    public ModCompatChecker(List<Id> textures, List<Id> items, JarIndex modJar) {
        this.textures = textures;
        this.items = items;
        this.modJar = modJar;
    }

    public List<String> check() {
        for (Id item : items) {
            checkItem(item);
        }

        for (Id texture : textures) {
            checkTexture(texture);
        }

        return missingFiles;
    }

    private void checkItem(Id itemId) {
        checkPath("assets/" + itemId.namespace() + "/models/item/" + itemId.path() + ".json");
    }

    private void checkTexture(Id textureLocation) {
        checkPath("assets/" + textureLocation.namespace() + "/textures/" + textureLocation.path() + ".png");
    }

    private void checkPath(String path) {
        if (!modJar.files().contains(path)) {
            missingFiles.add(path);
        }
    }
}
