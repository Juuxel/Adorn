package juuxel.adorn.data;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.data.fabric.AdornFabricBlockTagGenerator;
import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.lib.registry.AdornRegistryKeys;
import juuxel.adorn.util.Dyes;
import juuxel.adorn.util.Logging;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.JsonKeySortOrderCallback;
import net.minecraft.core.RegistrySetBuilder;
import org.slf4j.Logger;

public final class AdornDataGenerator implements DataGeneratorEntrypoint {
    private static final Logger LOGGER = Logging.logger();
    private static final String COMMON_MODE_PROPERTY = "adorn.data.commonMode";

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(AdornRegistryKeys.CONE_VARIANT, ConeVariant::bootstrap);
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        Dyes.checkInDev(); // run this check in CI when generating data
        var pack = fabricDataGenerator.createPack();

        if (Boolean.getBoolean(COMMON_MODE_PROPERTY)) {
            LOGGER.info("Running Adorn data generators in common mode");
            initCommon(pack);
        } else {
            LOGGER.info("Running Adorn data generators in Fabric mode");
            initFabric(pack);
        }
    }

    private void initCommon(FabricDataGenerator.Pack pack) {
        pack.addProvider(AdornDynamicRegistryGenerator::new);
        pack.addProvider(AdornGeneralDataGenerator::new);
        pack.addProvider(AdornTagGenerator::new);
        pack.addProvider(AdornBlockLootTableGenerator::new);
        pack.addProvider(AdornModelGenerator::new);
        pack.addProvider(AdornRecipeGenerator.Provider::new);
        pack.addProvider(BookGenerator::new);
        var blockTags = pack.addProvider(AdornBlockTagGenerator::new);
        pack.addProvider((output, registriesFuture) -> new AdornItemTagGenerator(output, registriesFuture, blockTags));
        pack.addProvider((output, _) -> PackMcmetaGeneration.create(output));
    }

    private void initFabric(FabricDataGenerator.Pack pack) {
        pack.addProvider(AdornFabricBlockTagGenerator::new);
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
