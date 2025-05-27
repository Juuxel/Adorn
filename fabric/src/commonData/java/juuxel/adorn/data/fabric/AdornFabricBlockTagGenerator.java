package juuxel.adorn.data.fabric;

import juuxel.adorn.block.AdornBlocks;
import net.fabricmc.fabric.api.block.v1.BlockFunctionalityTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public final class AdornFabricBlockTagGenerator extends FabricTagProvider.BlockTagProvider {
    public AdornFabricBlockTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries) {
        getOrCreateTagBuilder(BlockFunctionalityTags.CAN_CLIMB_TRAPDOOR_ABOVE)
            .add(AdornBlocks.STONE_LADDER.get());

        getOrCreateTagBuilder(TagKey.of(RegistryKeys.BLOCK, Identifier.of("towelette", "displaceable")))
            .add(AdornBlocks.STONE_TORCH_GROUND.get());
    }
}
