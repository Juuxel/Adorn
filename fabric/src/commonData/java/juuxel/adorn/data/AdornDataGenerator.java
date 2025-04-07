package juuxel.adorn.data;

import juuxel.adorn.AdornCommon;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.JsonKeySortOrderCallback;

public final class AdornDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();
        pack.addProvider(AdornGeneralDataGenerator::new);
        pack.addProvider(AdornTagGenerator::new);
        pack.addProvider(AdornBlockLootTableGenerator::new);
        pack.addProvider(AdornModelGenerator::new);
        pack.addProvider(AdornRecipeGenerator.Provider::new);
        pack.addProvider(BookGenerator::new);
        var blockTags = pack.addProvider(AdornBlockTagGenerator::new);
        pack.addProvider((output, registriesFuture) -> new AdornItemTagGenerator(output, registriesFuture, blockTags));
        pack.addProvider((output, registriesFuture) -> PackMcmetaGeneration.create(output));
    }

    @Override
    public String getEffectiveModId() {
        return AdornCommon.NAMESPACE;
    }

    @Override
    public void addJsonKeySortOrders(JsonKeySortOrderCallback callback) {
        callback.add("title", 1);
        callback.add("translate", 1);
    }
}
