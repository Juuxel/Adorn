package juuxel.adorn.item;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.component.ConeVariantComponent;
import juuxel.adorn.component.ItemDescription;
import juuxel.adorn.entity.AdornEntities;
import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.lib.registry.KeyedRegistrar;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public final class AdornItems {
    public static final KeyedRegistrar<Item> ITEMS = RegistrarFactory.get().create(RegistryKeys.ITEM);
    private static final FoodComponent DRINK_FOOD_COMPONENT = drinkFoodComponentBuilder().build();

    public static final Registered<Item> STONE_ROD = registerWithDescription("stone_rod", Item::new);
    public static final Registered<Item> MUG = registerWithDescription("mug", Item::new, () -> new Item.Settings().maxCount(16));
    public static final Registered<Item> HOT_CHOCOLATE = registerWithDescription("hot_chocolate",
        DrinkInMugItem::new,
        () -> new Item.Settings().food(DRINK_FOOD_COMPONENT, ConsumableComponents.DRINK).maxCount(1)
    );
    public static final Registered<Item> SWEET_BERRY_JUICE = registerWithDescription("sweet_berry_juice",
        DrinkInMugItem::new,
        () -> new Item.Settings().food(DRINK_FOOD_COMPONENT, ConsumableComponents.DRINK).maxCount(1)
    );
    public static final Registered<Item> GLOW_BERRY_TEA = registerWithDescription("glow_berry_tea",
        DrinkInMugItem::new,
        () -> new Item.Settings()
            .food(DRINK_FOOD_COMPONENT, ConsumableComponents.DRINK)
            .component(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS, new SuspiciousStewEffectsComponent(List.of(
                new SuspiciousStewEffectsComponent.StewEffect(StatusEffects.GLOWING, 400)
            )))
            .maxCount(1)
    );
    public static final Registered<Item> NETHER_WART_COFFEE = registerWithDescription("nether_wart_coffee",
        DrinkInMugItem::new,
        () -> new Item.Settings().food(DRINK_FOOD_COMPONENT, ConsumableComponents.DRINK).maxCount(1)
    );

    public static final Registered<Item> STONE_TORCH = ITEMS.register("stone_torch",
        key -> new VerticallyAttachableBlockItem(
            AdornBlocks.STONE_TORCH_GROUND.get(),
            AdornBlocks.STONE_TORCH_WALL.get(),
            Direction.DOWN,
            new Item.Settings()
                .registryKey(key)
                .useBlockPrefixedTranslationKey()
                .component(AdornComponentTypes.DESCRIPTION.get(), ItemDescription.ofBlock(key.getValue()))
        ));

    public static final Registered<Item> GUIDE_BOOK = register("guide_book",
        settings -> new AdornBookItem(AdornCommon.id("guide"), settings),
        key -> new Item.Settings().rarity(Rarity.UNCOMMON)
    );
    public static final Registered<Item> TRADERS_MANUAL = register("traders_manual",
        settings -> new AdornBookItem(AdornCommon.id("traders_manual"), settings));

    public static final Registered<Item> COPPER_NUGGET = registerWithDescription("copper_nugget", Item::new);
    public static final Registered<Item> WATERING_CAN = registerWithDescription("watering_can",
        WateringCanItem::new,
        () -> new Item.Settings()
            .component(AdornComponentTypes.FERTILIZER_LEVEL.get(), WateringCanItem.FertilizerLevel.ZERO)
            .component(AdornComponentTypes.WATER_LEVEL.get(), 0)
    );

    public static final Registered<Item> CONE = register("cone",
        ConeItem::new,
        key -> new Item.Settings()
            .translationKey(ConeVariant.DEFAULT_TRANSLATION_KEY)
            .component(AdornComponentTypes.CONE_VARIANT.get(), new ConeVariantComponent(ConeVariant.Keys.ORANGE))
            .component(AdornComponentTypes.DESCRIPTION.get(), ItemDescription.ofEntity(AdornEntities.CONE.key().getValue()))
            .equippable(EquipmentSlot.HEAD)
    );

    public static final Registered<Item> CAUTION_SIGN = registerCautionSign("caution_sign",
        AdornBlocks.CAUTION_SIGN,
        AdornBlocks.WALL_CAUTION_SIGN
    );
    public static final Registered<Item> BEE_CAUTION_SIGN = registerCautionSign("bee_caution_sign",
        AdornBlocks.BEE_CAUTION_SIGN,
        AdornBlocks.BEE_WALL_CAUTION_SIGN
    );
    public static final Registered<Item> BOOK_CAUTION_SIGN = registerCautionSign("book_caution_sign",
        AdornBlocks.BOOK_CAUTION_SIGN,
        AdornBlocks.BOOK_WALL_CAUTION_SIGN
    );
    public static final Registered<Item> CLIFF_CAUTION_SIGN = registerCautionSign("cliff_caution_sign",
        AdornBlocks.CLIFF_CAUTION_SIGN,
        AdornBlocks.CLIFF_WALL_CAUTION_SIGN
    );
    public static final Registered<Item> FORBIDDEN_CAUTION_SIGN = registerCautionSign("forbidden_caution_sign",
        AdornBlocks.FORBIDDEN_CAUTION_SIGN,
        AdornBlocks.FORBIDDEN_WALL_CAUTION_SIGN
    );
    public static final Registered<Item> HELMET_CAUTION_SIGN = registerCautionSign("helmet_caution_sign",
        AdornBlocks.HELMET_CAUTION_SIGN,
        AdornBlocks.HELMET_WALL_CAUTION_SIGN
    );
    public static final Registered<Item> RAILS_CAUTION_SIGN = registerCautionSign("rails_caution_sign",
        AdornBlocks.RAILS_CAUTION_SIGN,
        AdornBlocks.RAILS_WALL_CAUTION_SIGN
    );
    public static final Registered<Item> SURPRISE_CAUTION_SIGN = registerCautionSign("surprise_caution_sign",
        AdornBlocks.SURPRISE_CAUTION_SIGN,
        AdornBlocks.SURPRISE_WALL_CAUTION_SIGN
    );

    private static Registered<Item> register(String name, Function<Item.Settings, Item> factory) {
        return register(name, factory, key -> new Item.Settings());
    }

    private static Registered<Item> registerWithDescription(String name, Function<Item.Settings, Item> factory) {
        return registerWithDescription(name, factory, Item.Settings::new);
    }

    private static Registered<Item> registerWithDescription(String name, Function<Item.Settings, Item> factory, Supplier<Item.Settings> settings) {
        return register(name, factory, key -> addDescription(settings.get(), key));
    }

    private static Registered<Item> register(String name, Function<Item.Settings, Item> factory, Function<RegistryKey<Item>, Item.Settings> settings) {
        return ITEMS.register(name, key -> factory.apply(settings.apply(key).registryKey(key)));
    }

    private static FoodComponent.Builder drinkFoodComponentBuilder() {
        return new FoodComponent.Builder().nutrition(4).saturationModifier(0.3F).alwaysEdible();
    }

    private static Item.Settings addDescription(Item.Settings settings, RegistryKey<Item> key) {
        return settings.component(AdornComponentTypes.DESCRIPTION.get(), ItemDescription.ofItem(key.getValue()));
    }

    private static Registered<Item> registerCautionSign(String name, Registered<? extends Block> standing, Registered<? extends Block> wall) {
        return register(
            name,
            settings -> new VerticallyAttachableBlockItem(
                standing.get(),
                wall.get(),
                Direction.DOWN,
                settings
            ),
            key -> new Item.Settings()
                .useBlockPrefixedTranslationKey()
                .component(AdornComponentTypes.DESCRIPTION.get(), ItemDescription.ofBlock(AdornCommon.id("caution_sign")))
        );
    }

    public static void init() {
    }
}
