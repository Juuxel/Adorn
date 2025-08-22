package juuxel.adorn.data;

import com.google.common.collect.Lists;
import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.block.StandingCautionSignBlock;
import juuxel.adorn.block.variant.BlockKind;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.BlockVariantSets;
import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.component.ConeVariantComponent;
import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.item.AdornItems;
import juuxel.adorn.lib.registry.AdornRegistryKeys;
import juuxel.adorn.lib.registry.Registered;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.client.data.BlockModelDefinitionCreator;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.BlockStateVariantMap;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.ItemModels;
import net.minecraft.client.data.Model;
import net.minecraft.client.data.ModelIds;
import net.minecraft.client.data.Models;
import net.minecraft.client.data.TextureKey;
import net.minecraft.client.data.TextureMap;
import net.minecraft.client.data.TexturedModel;
import net.minecraft.client.data.VariantsBlockModelDefinitionCreator;
import net.minecraft.client.render.item.property.select.ComponentSelectProperty;
import net.minecraft.client.render.model.json.ModelVariantOperator;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.data.DataWriter;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public final class AdornModelGenerator extends FabricModelProvider {
    private static final TextureKey PIPE_TEXTURE_KEY = TextureKey.of("pipe");
    private static final Model COPPER_PIPE_INVENTORY_MODEL = new Model(
        Optional.of(AdornCommon.id("item/templates/copper_pipe")),
        Optional.empty(),
        PIPE_TEXTURE_KEY
    );

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

    private static final ModelVariantOperator ADD_ROT1_SUFFIX = addRotSuffix(1);
    private static final ModelVariantOperator ADD_ROT2_SUFFIX = addRotSuffix(2);
    private static final ModelVariantOperator ADD_ROT3_SUFFIX = addRotSuffix(3);
    private static final BlockStateVariantMap<ModelVariantOperator> STANDING_CAUTION_SIGN_OPERATIONS =
        BlockStateVariantMap.operations(StandingCautionSignBlock.ROTATION)
            .register(0, BlockStateModelGenerator.NO_OP)
            .register(1, ADD_ROT1_SUFFIX)
            .register(2, ADD_ROT2_SUFFIX)
            .register(3, BlockStateModelGenerator.ROTATE_Y_90.then(ADD_ROT3_SUFFIX))
            .register(4, BlockStateModelGenerator.ROTATE_Y_90)
            .register(5, BlockStateModelGenerator.ROTATE_Y_90.then(ADD_ROT1_SUFFIX))
            .register(6, BlockStateModelGenerator.ROTATE_Y_90.then(ADD_ROT2_SUFFIX))
            .register(7, BlockStateModelGenerator.ROTATE_Y_180.then(ADD_ROT3_SUFFIX))
            .register(8, BlockStateModelGenerator.ROTATE_Y_180)
            .register(9, BlockStateModelGenerator.ROTATE_Y_180.then(ADD_ROT1_SUFFIX))
            .register(10, BlockStateModelGenerator.ROTATE_Y_180.then(ADD_ROT2_SUFFIX))
            .register(11, BlockStateModelGenerator.ROTATE_Y_270.then(ADD_ROT3_SUFFIX))
            .register(12, BlockStateModelGenerator.ROTATE_Y_270)
            .register(13, BlockStateModelGenerator.ROTATE_Y_270.then(ADD_ROT1_SUFFIX))
            .register(14, BlockStateModelGenerator.ROTATE_Y_270.then(ADD_ROT2_SUFFIX))
            .register(15, ADD_ROT3_SUFFIX);

    private static ModelVariantOperator addRotSuffix(int n) {
        return variant -> variant.withModel(variant.modelId().withSuffixedPath("_rot" + n));
    }

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

        forwardBlockModel(generator, BlockVariantSets.get(BlockKind.SHELF, BlockVariant.IRON));
        forwardBlockModel(generator, AdornBlocks.BREWER);
        forwardBlockModel(generator, AdornBlocks.TRADING_STATION);
        forwardBlockModel(generator, AdornBlocks.CRATE);
        forwardBlockModel(generator, AdornBlocks.BRICK_CHIMNEY);
        forwardBlockModel(generator, AdornBlocks.STONE_BRICK_CHIMNEY);
        forwardBlockModel(generator, AdornBlocks.NETHER_BRICK_CHIMNEY);
        forwardBlockModel(generator, AdornBlocks.RED_NETHER_BRICK_CHIMNEY);
        forwardBlockModel(generator, AdornBlocks.COBBLESTONE_CHIMNEY);
        forwardBlockModel(generator, AdornBlocks.PRISMARINE_CHIMNEY);
        forwardBlockModel(generator, AdornBlocks.MAGMATIC_PRISMARINE_CHIMNEY);
        forwardBlockModel(generator, AdornBlocks.SOULFUL_PRISMARINE_CHIMNEY);
        forwardBlockModel(generator, AdornBlocks.APPLE_CRATE);
        forwardBlockModel(generator, AdornBlocks.WHEAT_CRATE);
        forwardBlockModel(generator, AdornBlocks.CARROT_CRATE);
        forwardBlockModel(generator, AdornBlocks.POTATO_CRATE);
        forwardBlockModel(generator, AdornBlocks.MELON_CRATE);
        forwardBlockModel(generator, AdornBlocks.WHEAT_SEED_CRATE);
        forwardBlockModel(generator, AdornBlocks.MELON_SEED_CRATE);
        forwardBlockModel(generator, AdornBlocks.PUMPKIN_SEED_CRATE);
        forwardBlockModel(generator, AdornBlocks.BEETROOT_CRATE);
        forwardBlockModel(generator, AdornBlocks.BEETROOT_SEED_CRATE);
        forwardBlockModel(generator, AdornBlocks.SWEET_BERRY_CRATE);
        forwardBlockModel(generator, AdornBlocks.COCOA_BEAN_CRATE);
        forwardBlockModel(generator, AdornBlocks.NETHER_WART_CRATE);
        forwardBlockModel(generator, AdornBlocks.SUGAR_CANE_CRATE);
        forwardBlockModel(generator, AdornBlocks.EGG_CRATE);
        forwardBlockModel(generator, AdornBlocks.HONEYCOMB_CRATE);
        forwardBlockModel(generator, AdornBlocks.LIL_TATER_CRATE);
        generator.registerParentedItemModel(AdornBlocks.CANDLELIT_LANTERN.get(), AdornCommon.id("block/candlelit_lantern_standing"));
        generator.registerItemModel(AdornBlocks.CHAIN_LINK_FENCE.get());
        generator.registerItemModel(AdornBlocks.STONE_LADDER.get());
        generator.registerItemModel(AdornBlocks.STONE_TORCH_GROUND.get());
        registerCopperPipe(generator, AdornBlocks.COPPER_PIPE, AdornBlocks.WAXED_COPPER_PIPE);
        registerCopperPipe(generator, AdornBlocks.EXPOSED_COPPER_PIPE, AdornBlocks.WAXED_EXPOSED_COPPER_PIPE);
        registerCopperPipe(generator, AdornBlocks.WEATHERED_COPPER_PIPE, AdornBlocks.WAXED_WEATHERED_COPPER_PIPE);
        registerCopperPipe(generator, AdornBlocks.OXIDIZED_COPPER_PIPE, AdornBlocks.WAXED_OXIDIZED_COPPER_PIPE);

        var coneVariantRegistry = registries.getOrThrow(AdornRegistryKeys.CONE_VARIANT);
        // Sort the keys in order to get a consistent and reproducible output.
        var coneVariants = coneVariantRegistry.streamKeys()
            .sorted(Comparator.comparing(RegistryKey::getValue))
            .toList();
        for (var variant : coneVariants) {
            registerCone(generator, variant);
        }
        generator.itemModelOutput.accept(
            AdornItems.CONE.get(),
            ItemModels.select(
                new ComponentSelectProperty<>(AdornComponentTypes.CONE_VARIANT.get()),
                ItemModels.basic(getConeModelId(ConeVariant.Keys.ORANGE)),
                Lists.transform(
                    coneVariants,
                    variant -> ItemModels.switchCase(
                        new ConeVariantComponent(variant),
                        ItemModels.basic(getConeModelId(variant))
                    )
                )
            )
        );

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

    private static void forwardBlockModel(BlockStateModelGenerator generator, Registered<? extends Block> block) {
        generator.registerParentedItemModel(block.get(), ModelIds.getBlockModelId(block.get()));
    }

    private static void registerCopperPipe(BlockStateModelGenerator generator, Registered<? extends Block> base, Registered<? extends Block> waxed) {
        generator.registerItemModel(
            base.get().asItem(),
            COPPER_PIPE_INVENTORY_MODEL.upload(
                base.get().asItem(),
                new TextureMap().put(PIPE_TEXTURE_KEY, TextureMap.getId(base.get())),
                generator.modelCollector
            )
        );
        generator.itemModelOutput.acceptAlias(base.get().asItem(), waxed.get().asItem());
    }

    private static void registerCautionSign(BlockStateModelGenerator generator, Registered<? extends Block> standing, Registered<? extends Block> wall) {
        WeightedVariant standingVariantRot0 = BlockStateModelGenerator.createWeightedVariant(STANDING_CAUTION_SIGN_ROT0_MODEL_FACTORY.upload(standing.get(), generator.modelCollector));
        STANDING_CAUTION_SIGN_ROT1_MODEL_FACTORY.upload(standing.get(), generator.modelCollector);
        STANDING_CAUTION_SIGN_ROT2_MODEL_FACTORY.upload(standing.get(), generator.modelCollector);
        STANDING_CAUTION_SIGN_ROT3_MODEL_FACTORY.upload(standing.get(), generator.modelCollector);

        BlockModelDefinitionCreator modelDefinitionCreator = VariantsBlockModelDefinitionCreator.of(standing.get(), standingVariantRot0)
            .coordinate(STANDING_CAUTION_SIGN_OPERATIONS);

        generator.blockStateCollector.accept(modelDefinitionCreator);
        generator.registerNorthDefaultHorizontalRotated(wall.get(), WALL_CAUTION_SIGN_MODEL_FACTORY);
        generator.registerItemModel(standing.get());
    }

    private static Identifier getConeModelId(RegistryKey<ConeVariant> variant) {
        return AdornCommon.id("block/" + variant.getValue().getPath() + "_cone");
    }

    private static void registerCone(BlockStateModelGenerator generator, RegistryKey<ConeVariant> variant) {
        var modelId = getConeModelId(variant);
        var textures = new TextureMap()
            .put(TextureKey.TOP, modelId.withSuffixedPath("_top"))
            .put(TextureKey.SIDE, modelId.withSuffixedPath("_side"))
            .put(TextureKey.BOTTOM, modelId.withSuffixedPath("_bottom"));
        CONE_MODEL.upload(modelId, textures, generator.modelCollector);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        registerFlat(generator, AdornBlocks.PICKET_FENCE);

        registerFlat(generator, AdornItems.COPPER_NUGGET);
        registerFlat(generator, AdornItems.GLOW_BERRY_TEA);
        registerFlat(generator, AdornItems.GUIDE_BOOK);
        registerFlat(generator, AdornItems.HOT_CHOCOLATE);
        registerFlat(generator, AdornItems.MUG);
        registerFlat(generator, AdornItems.NETHER_WART_COFFEE);
        registerFlat(generator, AdornItems.STONE_ROD);
        registerFlat(generator, AdornItems.SWEET_BERRY_JUICE);
        registerFlat(generator, AdornItems.TRADERS_MANUAL);
        generator.register(AdornItems.WATERING_CAN.get(), Models.HANDHELD);
    }

    private static void registerFlat(ItemModelGenerator generator, Registered<? extends ItemConvertible> item) {
        generator.register(item.get().asItem(), Models.GENERATED);
    }
}
