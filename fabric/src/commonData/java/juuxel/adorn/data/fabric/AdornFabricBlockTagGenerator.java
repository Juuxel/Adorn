package juuxel.adorn.data.fabric;

import juuxel.adorn.block.AdornBlocks;
import net.fabricmc.fabric.api.block.v1.BlockFunctionalityTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;

import java.util.concurrent.CompletableFuture;

public final class AdornFabricBlockTagGenerator extends FabricTagsProvider.BlockTagsProvider {
    public AdornFabricBlockTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        valueLookupBuilder(BlockFunctionalityTags.CAN_CLIMB_TRAPDOOR_ABOVE)
            .add(AdornBlocks.STONE_LADDER.get());

        valueLookupBuilder(TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("towelette", "displaceable")))
            .add(AdornBlocks.STONE_TORCH_GROUND.get());
    }
}
