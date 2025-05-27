package juuxel.adorn.data.fabric;

import juuxel.adorn.AdornCommon;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public final class AdornFabricDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();
        pack.addProvider(AdornFabricBlockTagGenerator::new);
    }

    @Override
    public String getEffectiveModId() {
        return AdornCommon.NAMESPACE;
    }
}
