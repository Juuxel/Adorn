package juuxel.adorn.data;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.item.AdornItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public final class AdornModelGenerator extends FabricModelProvider {
    private static final Model CONE_MODEL = new Model(
        Optional.of(AdornCommon.id("block/templates/cone")),
        Optional.empty(),
        TextureKey.TOP,
        TextureKey.SIDE,
        TextureKey.BOTTOM
    );

    private final CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture;
    public RegistryWrapper.WrapperLookup registries;

    public AdornModelGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output);
        this.registriesFuture = registriesFuture;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return registriesFuture.thenCompose(registries -> {
            this.registries = registries;
            return super.run(writer);
        });
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        AdornBlocks.PAINTED_PLANKS.forEach((color, planks) -> {
            generator.registerCubeAllModelTexturePool(planks)
                .slab(AdornBlocks.PAINTED_WOOD_SLABS.getEager(color))
                .stairs(AdornBlocks.PAINTED_WOOD_STAIRS.getEager(color))
                .fence(AdornBlocks.PAINTED_WOOD_FENCES.getEager(color))
                .fenceGate(AdornBlocks.PAINTED_WOOD_FENCE_GATES.getEager(color))
                .pressurePlate(AdornBlocks.PAINTED_WOOD_PRESSURE_PLATES.getEager(color))
                .button(AdornBlocks.PAINTED_WOOD_BUTTONS.getEager(color));
        });

        for (ConeVariant variant : ConeVariant.values()) {
            registerCone(generator, variant);
        }
    }

    private static Identifier getConeModelId(ConeVariant variant) {
        return AdornCommon.id("block/" + variant.id() + "_cone");
    }

    private static void registerCone(BlockStateModelGenerator generator, ConeVariant variant) {
        var modelId = getConeModelId(variant);
        var textures = new TextureMap()
            .put(TextureKey.TOP, modelId.withSuffixedPath("_top"))
            .put(TextureKey.SIDE, modelId.withSuffixedPath("_side"))
            .put(TextureKey.BOTTOM, modelId.withSuffixedPath("_bottom"));
        CONE_MODEL.upload(modelId, textures, generator.modelCollector);
        generator.registerParentedItemModel(AdornItems.CONES.getEager(variant), modelId);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
    }
}
