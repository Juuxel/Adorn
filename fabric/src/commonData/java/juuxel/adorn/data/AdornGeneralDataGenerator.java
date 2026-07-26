package juuxel.adorn.data;

import juuxel.adorn.datagen.DataGenerator;
import juuxel.adorn.datagen.DataOutput;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;

public final class AdornGeneralDataGenerator extends AdornCustomDataGenerator {
    private static final String MAIN_CONFIGS_PROPERTY = "adorn.data.mainConfigs";

    public AdornGeneralDataGenerator(FabricPackOutput output) {
        super(output);
    }

    @Override
    protected void run(DataOutput output) {
        DataGenerator.generate(AdornTagGenerator.getDataConfigs(MAIN_CONFIGS_PROPERTY), output);
    }

    @Override
    public String getName() {
        return "General Data";
    }
}
