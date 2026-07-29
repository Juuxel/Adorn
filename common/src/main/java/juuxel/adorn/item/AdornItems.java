package juuxel.adorn.item;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.component.ItemDescription;
import juuxel.adorn.entity.AdornEntities;
import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.lib.registry.KeyedRegistrar;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public final class AdornItems {
    public static final KeyedRegistrar<Item> ITEMS = RegistrarFactory.get().create(Registries.ITEM);
    private static final FoodProperties DRINK_FOOD_COMPONENT = drinkFoodComponentBuilder().build();

    public static final Registered<Item> STONE_ROD = registerWithDescription("stone_rod", Item::new);
    public static final Registered<Item> MUG = registerWithDescription("mug", Item::new, () -> new Item.Properties().stacksTo(16));
    public static final Registered<Item> HOT_CHOCOLATE = registerWithDescription("hot_chocolate",
        DrinkInMugItem::new,
        () -> new Item.Properties().food(DRINK_FOOD_COMPONENT, Consumables.DEFAULT_DRINK).stacksTo(1)
    );
    public static final Registered<Item> SWEET_BERRY_JUICE = registerWithDescription("sweet_berry_juice",
        DrinkInMugItem::new,
        () -> new Item.Properties().food(DRINK_FOOD_COMPONENT, Consumables.DEFAULT_DRINK).stacksTo(1)
    );
    public static final Registered<Item> GLOW_BERRY_TEA = registerWithDescription("glow_berry_tea",
        DrinkInMugItem::new,
        () -> new Item.Properties()
            .food(DRINK_FOOD_COMPONENT, Consumables.DEFAULT_DRINK)
            .component(DataComponents.SUSPICIOUS_STEW_EFFECTS, new SuspiciousStewEffects(List.of(
                new SuspiciousStewEffects.Entry(MobEffects.GLOWING, 400)
            )))
            .stacksTo(1)
    );
    public static final Registered<Item> NETHER_WART_COFFEE = registerWithDescription("nether_wart_coffee",
        DrinkInMugItem::new,
        () -> new Item.Properties().food(DRINK_FOOD_COMPONENT, Consumables.DEFAULT_DRINK).stacksTo(1)
    );

    public static final Registered<Item> STONE_TORCH = ITEMS.register("stone_torch",
        key -> new StandingAndWallBlockItem(
            AdornBlocks.STONE_TORCH_GROUND.get(),
            AdornBlocks.STONE_TORCH_WALL.get(),
            Direction.DOWN,
            new Item.Properties()
                .setId(key)
                .useBlockDescriptionPrefix()
                .component(AdornComponentTypes.DESCRIPTION.get(), ItemDescription.ofBlock(key.identifier()))
        ));

    public static final Registered<Item> GUIDE_BOOK = register("guide_book",
        settings -> new AdornBookItem(AdornCommon.id("guide"), settings),
        key -> new Item.Properties().rarity(Rarity.UNCOMMON)
    );
    public static final Registered<Item> TRADERS_MANUAL = register("traders_manual",
        settings -> new AdornBookItem(AdornCommon.id("traders_manual"), settings));

    // TODO: DELETE
    public static final Registered<Item> COPPER_NUGGET = registerWithDescription("copper_nugget", Item::new);
    public static final Registered<Item> WATERING_CAN = registerWithDescription("watering_can",
        WateringCanItem::new,
        () -> new Item.Properties()
            .component(AdornComponentTypes.FERTILIZER_LEVEL.get(), WateringCanItem.FertilizerLevel.ZERO)
            .component(AdornComponentTypes.WATER_LEVEL.get(), 0)
    );

    public static final Registered<Item> CONE = register("cone",
        ConeItem::new,
        key -> new Item.Properties()
            .overrideDescription(ConeVariant.DEFAULT_TRANSLATION_KEY)
            .delayedHolderComponent(AdornComponentTypes.CONE_VARIANT.get(), ConeVariant.Keys.ORANGE)
            .component(AdornComponentTypes.DESCRIPTION.get(), ItemDescription.ofEntity(AdornEntities.CONE.key().identifier()))
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

    private static Registered<Item> register(String name, Function<Item.Properties, Item> factory) {
        return register(name, factory, key -> new Item.Properties());
    }

    private static Registered<Item> registerWithDescription(String name, Function<Item.Properties, Item> factory) {
        return registerWithDescription(name, factory, Item.Properties::new);
    }

    private static Registered<Item> registerWithDescription(String name, Function<Item.Properties, Item> factory, Supplier<Item.Properties> settings) {
        return register(name, factory, key -> addDescription(settings.get(), key));
    }

    private static Registered<Item> register(String name, Function<Item.Properties, Item> factory, Function<ResourceKey<Item>, Item.Properties> settings) {
        return ITEMS.register(name, key -> factory.apply(settings.apply(key).setId(key)));
    }

    private static FoodProperties.Builder drinkFoodComponentBuilder() {
        return new FoodProperties.Builder().nutrition(4).saturationModifier(0.3F).alwaysEdible();
    }

    private static Item.Properties addDescription(Item.Properties settings, ResourceKey<Item> key) {
        return settings.component(AdornComponentTypes.DESCRIPTION.get(), ItemDescription.ofItem(key.identifier()));
    }

    private static Registered<Item> registerCautionSign(String name, Registered<? extends Block> standing, Registered<? extends Block> wall) {
        return register(
            name,
            settings -> new StandingAndWallBlockItem(
                standing.get(),
                wall.get(),
                Direction.DOWN,
                settings
            ),
            key -> new Item.Properties()
                .useBlockDescriptionPrefix()
                .component(AdornComponentTypes.DESCRIPTION.get(), ItemDescription.ofBlock(AdornCommon.id("caution_sign")))
        );
    }

    public static void init() {
    }
}
