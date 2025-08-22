package juuxel.adorn.data;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.block.StandingCautionSignBlock;
import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.item.AdornItems;
import juuxel.adorn.lib.registry.Registered;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.DataWriter;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateSupplier;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.TexturedModel;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.VariantsBlockStateSupplier;
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

    private static final TextureKey SIGN_TEXTURE_KEY = TextureKey.of("sign");
    private static final Model WALL_CAUTION_SIGN_MODEL = new Model(
        Optional.of(AdornCommon.id("block/templates/wall_caution_sign")),
        Optional.empty(),
        SIGN_TEXTURE_KEY
    );
    private static final TexturedModel.Factory WALL_CAUTION_SIGN_MODEL_FACTORY =
        TexturedModel.makeFactory(block -> new TextureMap().put(SIGN_TEXTURE_KEY, TextureMap.getId(block).withPath(path -> path.replace("wall_", ""))), WALL_CAUTION_SIGN_MODEL);
    private static final Model STANDING_CAUTION_SIGN_ROT0_MODEL = new Model(
        Optional.of(AdornCommon.id("block/templates/standing_caution_sign")),
        Optional.empty(),
        SIGN_TEXTURE_KEY
    );
    private static final TexturedModel.Factory STANDING_CAUTION_SIGN_ROT0_MODEL_FACTORY =
        TexturedModel.makeFactory(block -> new TextureMap().put(SIGN_TEXTURE_KEY, TextureMap.getId(block)), STANDING_CAUTION_SIGN_ROT0_MODEL);
    private static final Model STANDING_CAUTION_SIGN_ROT1_MODEL = new Model(
        Optional.of(AdornCommon.id("block/templates/standing_caution_sign_rot1")),
        Optional.of("_rot1"),
        SIGN_TEXTURE_KEY
    );
    private static final TexturedModel.Factory STANDING_CAUTION_SIGN_ROT1_MODEL_FACTORY =
        TexturedModel.makeFactory(block -> new TextureMap().put(SIGN_TEXTURE_KEY, TextureMap.getId(block)), STANDING_CAUTION_SIGN_ROT1_MODEL);
    private static final Model STANDING_CAUTION_SIGN_ROT2_MODEL = new Model(
        Optional.of(AdornCommon.id("block/templates/standing_caution_sign_rot2")),
        Optional.of("_rot2"),
        SIGN_TEXTURE_KEY
    );
    private static final TexturedModel.Factory STANDING_CAUTION_SIGN_ROT2_MODEL_FACTORY =
        TexturedModel.makeFactory(block -> new TextureMap().put(SIGN_TEXTURE_KEY, TextureMap.getId(block)), STANDING_CAUTION_SIGN_ROT2_MODEL);
    private static final Model STANDING_CAUTION_SIGN_ROT3_MODEL = new Model(
        Optional.of(AdornCommon.id("block/templates/standing_caution_sign_rot3")),
        Optional.of("_rot3"),
        SIGN_TEXTURE_KEY
    );
    private static final TexturedModel.Factory STANDING_CAUTION_SIGN_ROT3_MODEL_FACTORY =
        TexturedModel.makeFactory(block -> new TextureMap().put(SIGN_TEXTURE_KEY, TextureMap.getId(block)), STANDING_CAUTION_SIGN_ROT3_MODEL);

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

        generator.registerNorthDefaultHorizontalRotation(AdornBlocks.BARRICADE.get());
        registerCautionSign(generator, AdornBlocks.CAUTION_SIGN, AdornBlocks.WALL_CAUTION_SIGN);
        registerCautionSign(generator, AdornBlocks.BEE_CAUTION_SIGN, AdornBlocks.BEE_WALL_CAUTION_SIGN);
        registerCautionSign(generator, AdornBlocks.BOOK_CAUTION_SIGN, AdornBlocks.BOOK_WALL_CAUTION_SIGN);
        registerCautionSign(generator, AdornBlocks.CLIFF_CAUTION_SIGN, AdornBlocks.CLIFF_WALL_CAUTION_SIGN);
        registerCautionSign(generator, AdornBlocks.FORBIDDEN_CAUTION_SIGN, AdornBlocks.FORBIDDEN_WALL_CAUTION_SIGN);
        registerCautionSign(generator, AdornBlocks.HELMET_CAUTION_SIGN, AdornBlocks.HELMET_WALL_CAUTION_SIGN);
        registerCautionSign(generator, AdornBlocks.RAILS_CAUTION_SIGN, AdornBlocks.RAILS_WALL_CAUTION_SIGN);
        registerCautionSign(generator, AdornBlocks.SURPRISE_CAUTION_SIGN, AdornBlocks.SURPRISE_WALL_CAUTION_SIGN);
    }

    private static void registerCautionSign(BlockStateModelGenerator generator, Registered<? extends Block> standing, Registered<? extends Block> wall) {
        Identifier modelId = STANDING_CAUTION_SIGN_ROT0_MODEL_FACTORY.upload(standing.get(), generator.modelCollector);
        Identifier modelIdRot1 = STANDING_CAUTION_SIGN_ROT1_MODEL_FACTORY.upload(standing.get(), generator.modelCollector);
        Identifier modelIdRot2 = STANDING_CAUTION_SIGN_ROT2_MODEL_FACTORY.upload(standing.get(), generator.modelCollector);
        Identifier modelIdRot3 = STANDING_CAUTION_SIGN_ROT3_MODEL_FACTORY.upload(standing.get(), generator.modelCollector);

        BlockStateVariantMap variantMap = BlockStateVariantMap.create(StandingCautionSignBlock.ROTATION)
            .register(0, createStandingCautionSignVariant(modelId, VariantSettings.Rotation.R0))
            .register(1, createStandingCautionSignVariant(modelIdRot1, VariantSettings.Rotation.R0))
            .register(2, createStandingCautionSignVariant(modelIdRot2, VariantSettings.Rotation.R0))
            .register(3, createStandingCautionSignVariant(modelIdRot3, VariantSettings.Rotation.R90))
            .register(4, createStandingCautionSignVariant(modelId, VariantSettings.Rotation.R90))
            .register(5, createStandingCautionSignVariant(modelIdRot1, VariantSettings.Rotation.R90))
            .register(6, createStandingCautionSignVariant(modelIdRot2, VariantSettings.Rotation.R90))
            .register(7, createStandingCautionSignVariant(modelIdRot3, VariantSettings.Rotation.R180))
            .register(8, createStandingCautionSignVariant(modelId, VariantSettings.Rotation.R180))
            .register(9, createStandingCautionSignVariant(modelIdRot1, VariantSettings.Rotation.R180))
            .register(10, createStandingCautionSignVariant(modelIdRot2, VariantSettings.Rotation.R180))
            .register(11, createStandingCautionSignVariant(modelIdRot3, VariantSettings.Rotation.R270))
            .register(12, createStandingCautionSignVariant(modelId, VariantSettings.Rotation.R270))
            .register(13, createStandingCautionSignVariant(modelIdRot1, VariantSettings.Rotation.R270))
            .register(14, createStandingCautionSignVariant(modelIdRot2, VariantSettings.Rotation.R270))
            .register(15, createStandingCautionSignVariant(modelIdRot3, VariantSettings.Rotation.R0));

        BlockStateSupplier blockStateSupplier = VariantsBlockStateSupplier.create(standing.get()).coordinate(variantMap);
        generator.blockStateCollector.accept(blockStateSupplier);
        generator.registerNorthDefaultHorizontalRotated(wall.get(), WALL_CAUTION_SIGN_MODEL_FACTORY);
        generator.registerItemModel(standing.get());
    }

    private static BlockStateVariant createStandingCautionSignVariant(Identifier modelId, VariantSettings.Rotation y) {
        var variant = BlockStateVariant.create().put(VariantSettings.MODEL, modelId);
        if (y != VariantSettings.Rotation.R0) variant.put(VariantSettings.Y, y);
        return variant;
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
