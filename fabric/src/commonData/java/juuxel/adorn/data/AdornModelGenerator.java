package juuxel.adorn.data;

import com.google.common.collect.Lists;
import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.block.StandingCautionSignBlock;
import juuxel.adorn.block.variant.BlockKind;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.BlockVariantSets;
import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.item.AdornItems;
import juuxel.adorn.lib.registry.AdornRegistryKeys;
import juuxel.adorn.lib.registry.Registered;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.renderer.item.properties.select.ComponentContents;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public final class AdornModelGenerator extends FabricModelProvider {
    public static final ScopedValue<HolderLookup.Provider> REGISTRIES = ScopedValue.newInstance();

    private static final TextureSlot PIPE_TEXTURE_KEY = TextureSlot.create("pipe");
    private static final ModelTemplate COPPER_PIPE_INVENTORY_MODEL = new ModelTemplate(
        Optional.of(AdornCommon.id("item/templates/copper_pipe")),
        Optional.empty(),
        PIPE_TEXTURE_KEY
    );

    private static final ModelTemplate CONE_MODEL = new ModelTemplate(
        Optional.of(AdornCommon.id("block/templates/cone")),
        Optional.empty(),
        TextureSlot.TOP,
        TextureSlot.SIDE,
        TextureSlot.BOTTOM
    );

    private static final TextureSlot SIGN_TEXTURE_KEY = TextureSlot.create("sign");
    private static final ModelTemplate WALL_CAUTION_SIGN_MODEL = new ModelTemplate(
        Optional.of(AdornCommon.id("block/templates/wall_caution_sign")),
        Optional.empty(),
        SIGN_TEXTURE_KEY
    );
    private static final TexturedModel.Provider WALL_CAUTION_SIGN_MODEL_FACTORY =
        TexturedModel.createDefault(block -> new TextureMapping().put(SIGN_TEXTURE_KEY, modifyPath(TextureMapping.getBlockTexture(block), path -> path.replace("wall_", ""))), WALL_CAUTION_SIGN_MODEL);
    private static final ModelTemplate STANDING_CAUTION_SIGN_ROT0_MODEL = new ModelTemplate(
        Optional.of(AdornCommon.id("block/templates/standing_caution_sign")),
        Optional.empty(),
        SIGN_TEXTURE_KEY
    );
    private static final TexturedModel.Provider STANDING_CAUTION_SIGN_ROT0_MODEL_FACTORY =
        TexturedModel.createDefault(block -> new TextureMapping().put(SIGN_TEXTURE_KEY, TextureMapping.getBlockTexture(block)), STANDING_CAUTION_SIGN_ROT0_MODEL);
    private static final ModelTemplate STANDING_CAUTION_SIGN_ROT1_MODEL = new ModelTemplate(
        Optional.of(AdornCommon.id("block/templates/standing_caution_sign_rot1")),
        Optional.of("_rot1"),
        SIGN_TEXTURE_KEY
    );
    private static final TexturedModel.Provider STANDING_CAUTION_SIGN_ROT1_MODEL_FACTORY =
        TexturedModel.createDefault(block -> new TextureMapping().put(SIGN_TEXTURE_KEY, TextureMapping.getBlockTexture(block)), STANDING_CAUTION_SIGN_ROT1_MODEL);
    private static final ModelTemplate STANDING_CAUTION_SIGN_ROT2_MODEL = new ModelTemplate(
        Optional.of(AdornCommon.id("block/templates/standing_caution_sign_rot2")),
        Optional.of("_rot2"),
        SIGN_TEXTURE_KEY
    );
    private static final TexturedModel.Provider STANDING_CAUTION_SIGN_ROT2_MODEL_FACTORY =
        TexturedModel.createDefault(block -> new TextureMapping().put(SIGN_TEXTURE_KEY, TextureMapping.getBlockTexture(block)), STANDING_CAUTION_SIGN_ROT2_MODEL);
    private static final ModelTemplate STANDING_CAUTION_SIGN_ROT3_MODEL = new ModelTemplate(
        Optional.of(AdornCommon.id("block/templates/standing_caution_sign_rot3")),
        Optional.of("_rot3"),
        SIGN_TEXTURE_KEY
    );
    private static final TexturedModel.Provider STANDING_CAUTION_SIGN_ROT3_MODEL_FACTORY =
        TexturedModel.createDefault(block -> new TextureMapping().put(SIGN_TEXTURE_KEY, TextureMapping.getBlockTexture(block)), STANDING_CAUTION_SIGN_ROT3_MODEL);

    private static final VariantMutator ADD_ROT1_SUFFIX = addRotSuffix(1);
    private static final VariantMutator ADD_ROT2_SUFFIX = addRotSuffix(2);
    private static final VariantMutator ADD_ROT3_SUFFIX = addRotSuffix(3);
    private static final PropertyDispatch<VariantMutator> STANDING_CAUTION_SIGN_OPERATIONS =
        PropertyDispatch.modify(StandingCautionSignBlock.ROTATION)
            .select(0, BlockModelGenerators.NOP)
            .select(1, ADD_ROT1_SUFFIX)
            .select(2, ADD_ROT2_SUFFIX)
            .select(3, BlockModelGenerators.Y_ROT_90.then(ADD_ROT3_SUFFIX))
            .select(4, BlockModelGenerators.Y_ROT_90)
            .select(5, BlockModelGenerators.Y_ROT_90.then(ADD_ROT1_SUFFIX))
            .select(6, BlockModelGenerators.Y_ROT_90.then(ADD_ROT2_SUFFIX))
            .select(7, BlockModelGenerators.Y_ROT_180.then(ADD_ROT3_SUFFIX))
            .select(8, BlockModelGenerators.Y_ROT_180)
            .select(9, BlockModelGenerators.Y_ROT_180.then(ADD_ROT1_SUFFIX))
            .select(10, BlockModelGenerators.Y_ROT_180.then(ADD_ROT2_SUFFIX))
            .select(11, BlockModelGenerators.Y_ROT_270.then(ADD_ROT3_SUFFIX))
            .select(12, BlockModelGenerators.Y_ROT_270)
            .select(13, BlockModelGenerators.Y_ROT_270.then(ADD_ROT1_SUFFIX))
            .select(14, BlockModelGenerators.Y_ROT_270.then(ADD_ROT2_SUFFIX))
            .select(15, ADD_ROT3_SUFFIX);

    private static VariantMutator addRotSuffix(int n) {
        return variant -> variant.withModel(variant.modelLocation().withSuffix("_rot" + n));
    }

    private final CompletableFuture<HolderLookup.Provider> registriesFuture;
    public HolderLookup.Provider registries;

    public AdornModelGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output);
        this.registriesFuture = registriesFuture;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        return registriesFuture.thenCompose(registries -> {
            this.registries = registries;
            return ScopedValue.where(REGISTRIES, registries).call(() -> super.run(writer));
        });
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {
        AdornBlocks.PAINTED_PLANKS.forEach((color, planks) -> {
            generator.family(planks)
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
        generator.registerSimpleItemModel(AdornBlocks.CANDLELIT_LANTERN.get(), AdornCommon.id("block/candlelit_lantern_standing"));
        generator.registerSimpleFlatItemModel(AdornBlocks.CHAIN_LINK_FENCE.get());
        generator.registerSimpleFlatItemModel(AdornBlocks.STONE_LADDER.get());
        generator.registerSimpleFlatItemModel(AdornBlocks.STONE_TORCH_GROUND.get());
        registerCopperPipe(generator, AdornBlocks.COPPER_PIPE, AdornBlocks.WAXED_COPPER_PIPE);
        registerCopperPipe(generator, AdornBlocks.EXPOSED_COPPER_PIPE, AdornBlocks.WAXED_EXPOSED_COPPER_PIPE);
        registerCopperPipe(generator, AdornBlocks.WEATHERED_COPPER_PIPE, AdornBlocks.WAXED_WEATHERED_COPPER_PIPE);
        registerCopperPipe(generator, AdornBlocks.OXIDIZED_COPPER_PIPE, AdornBlocks.WAXED_OXIDIZED_COPPER_PIPE);

        var coneVariantRegistry = registries.lookupOrThrow(AdornRegistryKeys.CONE_VARIANT);
        // Sort the keys in order to get a consistent and reproducible output.
        var coneVariants = coneVariantRegistry.listElementIds()
            .sorted(Comparator.comparing(ResourceKey::identifier))
            .toList();
        for (var variant : coneVariants) {
            registerCone(generator, variant);
        }
        generator.itemModelOutput.accept(
            AdornItems.CONE.get(),
            ItemModelUtils.select(
                new ComponentContents<>(AdornComponentTypes.CONE_VARIANT.get()),
                ItemModelUtils.plainModel(getConeModelId(ConeVariant.Keys.ORANGE)),
                Lists.transform(
                    coneVariants,
                    variant -> ItemModelUtils.when(
                        registries.getOrThrow(variant),
                        ItemModelUtils.plainModel(getConeModelId(variant))
                    )
                )
            )
        );

        generator.createNonTemplateHorizontalBlock(AdornBlocks.BARRICADE.get());
        registerCautionSign(generator, AdornBlocks.CAUTION_SIGN, AdornBlocks.WALL_CAUTION_SIGN);
        registerCautionSign(generator, AdornBlocks.BEE_CAUTION_SIGN, AdornBlocks.BEE_WALL_CAUTION_SIGN);
        registerCautionSign(generator, AdornBlocks.BOOK_CAUTION_SIGN, AdornBlocks.BOOK_WALL_CAUTION_SIGN);
        registerCautionSign(generator, AdornBlocks.CLIFF_CAUTION_SIGN, AdornBlocks.CLIFF_WALL_CAUTION_SIGN);
        registerCautionSign(generator, AdornBlocks.FORBIDDEN_CAUTION_SIGN, AdornBlocks.FORBIDDEN_WALL_CAUTION_SIGN);
        registerCautionSign(generator, AdornBlocks.HELMET_CAUTION_SIGN, AdornBlocks.HELMET_WALL_CAUTION_SIGN);
        registerCautionSign(generator, AdornBlocks.RAILS_CAUTION_SIGN, AdornBlocks.RAILS_WALL_CAUTION_SIGN);
        registerCautionSign(generator, AdornBlocks.SURPRISE_CAUTION_SIGN, AdornBlocks.SURPRISE_WALL_CAUTION_SIGN);
    }

    private static void forwardBlockModel(BlockModelGenerators generator, Registered<? extends Block> block) {
        generator.registerSimpleItemModel(block.get(), ModelLocationUtils.getModelLocation(block.get()));
    }

    private static void registerCopperPipe(BlockModelGenerators generator, Registered<? extends Block> base, Registered<? extends Block> waxed) {
        generator.registerSimpleItemModel(
            base.get().asItem(),
            COPPER_PIPE_INVENTORY_MODEL.create(
                base.get().asItem(),
                new TextureMapping().put(PIPE_TEXTURE_KEY, TextureMapping.getBlockTexture(base.get())),
                generator.modelOutput
            )
        );
        generator.itemModelOutput.copy(base.get().asItem(), waxed.get().asItem());
    }

    private static void registerCautionSign(BlockModelGenerators generator, Registered<? extends Block> standing, Registered<? extends Block> wall) {
        MultiVariant standingVariantRot0 = BlockModelGenerators.plainVariant(STANDING_CAUTION_SIGN_ROT0_MODEL_FACTORY.create(standing.get(), generator.modelOutput));
        STANDING_CAUTION_SIGN_ROT1_MODEL_FACTORY.create(standing.get(), generator.modelOutput);
        STANDING_CAUTION_SIGN_ROT2_MODEL_FACTORY.create(standing.get(), generator.modelOutput);
        STANDING_CAUTION_SIGN_ROT3_MODEL_FACTORY.create(standing.get(), generator.modelOutput);

        BlockModelDefinitionGenerator modelDefinitionCreator = MultiVariantGenerator.dispatch(standing.get(), standingVariantRot0)
            .with(STANDING_CAUTION_SIGN_OPERATIONS);

        generator.blockStateOutput.accept(modelDefinitionCreator);
        generator.createHorizontallyRotatedBlock(wall.get(), WALL_CAUTION_SIGN_MODEL_FACTORY);
        generator.registerSimpleFlatItemModel(standing.get());
    }

    private static Identifier getConeModelId(ResourceKey<ConeVariant> variant) {
        return AdornCommon.id("block/" + variant.identifier().getPath() + "_cone");
    }

    private static void registerCone(BlockModelGenerators generator, ResourceKey<ConeVariant> variant) {
        var modelId = getConeModelId(variant);
        var textures = new TextureMapping()
            .put(TextureSlot.TOP, new Material(modelId.withSuffix("_top")))
            .put(TextureSlot.SIDE, new Material(modelId.withSuffix("_side")))
            .put(TextureSlot.BOTTOM, new Material(modelId.withSuffix("_bottom")));
        CONE_MODEL.create(modelId, textures, generator.modelOutput);
    }

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
        registerFlat(generator, AdornBlocks.PICKET_FENCE);

        registerFlat(generator, AdornItems.GLOW_BERRY_TEA);
        registerFlat(generator, AdornItems.GUIDE_BOOK);
        registerFlat(generator, AdornItems.HOT_CHOCOLATE);
        registerFlat(generator, AdornItems.MUG);
        registerFlat(generator, AdornItems.NETHER_WART_COFFEE);
        registerFlat(generator, AdornItems.STONE_ROD);
        registerFlat(generator, AdornItems.SWEET_BERRY_JUICE);
        registerFlat(generator, AdornItems.TRADERS_MANUAL);
        generator.generateFlatItem(AdornItems.WATERING_CAN.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
    }

    private static void registerFlat(ItemModelGenerators generator, Registered<? extends ItemLike> item) {
        generator.generateFlatItem(item.get().asItem(), ModelTemplates.FLAT_ITEM);
    }

    private static Material modifyPath(Material material, UnaryOperator<String> op) {
        return new Material(material.sprite().withPath(op), material.forceTranslucent());
    }
}
