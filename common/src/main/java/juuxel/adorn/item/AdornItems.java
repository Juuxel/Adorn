package juuxel.adorn.item;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.lib.registry.KeyedRegistrar;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.RegisteredMap;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import juuxel.adorn.platform.ItemBridge;
import net.minecraft.block.Block;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Direction;

public final class AdornItems {
    public static final KeyedRegistrar<Item> ITEMS = RegistrarFactory.get().create(RegistryKeys.ITEM);
    private static final FoodComponent DRINK_FOOD_COMPONENT = drinkFoodComponentBuilder().build();

    public static final Registered<Item> STONE_ROD = ITEMS.register("stone_rod", () -> new ItemWithDescription(new Item.Settings()));
    public static final Registered<Item> MUG = ITEMS.register("mug", () -> new ItemWithDescription(new Item.Settings().maxCount(16)));
    public static final Registered<Item> HOT_CHOCOLATE = ITEMS.register("hot_chocolate",
        () -> new DrinkInMugItem(new Item.Settings().food(DRINK_FOOD_COMPONENT).maxCount(1)));
    public static final Registered<Item> SWEET_BERRY_JUICE = ITEMS.register("sweet_berry_juice",
        () -> new DrinkInMugItem(new Item.Settings().food(DRINK_FOOD_COMPONENT).maxCount(1)));
    public static final Registered<Item> GLOW_BERRY_TEA = ITEMS.register("glow_berry_tea",
        () -> new DrinkInMugItem(
            new Item.Settings()
                .food(drinkFoodComponentBuilder().statusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 400), 1.0f).build())
                .maxCount(1)
        ));
    public static final Registered<Item> NETHER_WART_COFFEE = ITEMS.register("nether_wart_coffee",
        () -> new DrinkInMugItem(new Item.Settings().food(DRINK_FOOD_COMPONENT).maxCount(1)));

    public static final Registered<Item> STONE_TORCH = ITEMS.register("stone_torch",
        () -> new VerticallyAttachableBlockItemWithDescription(
            AdornBlocks.STONE_TORCH_GROUND.get(),
            AdornBlocks.STONE_TORCH_WALL.get(),
            new Item.Settings(),
            Direction.DOWN
        ));

    public static final Registered<Item> GUIDE_BOOK = ITEMS.register("guide_book",
        () -> new AdornBookItem(AdornCommon.id("guide"), new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final Registered<Item> TRADERS_MANUAL = ITEMS.register("traders_manual",
        () -> new AdornBookItem(AdornCommon.id("traders_manual"), new Item.Settings()));

    public static final Registered<Item> COPPER_NUGGET = ITEMS.register("copper_nugget", () -> new ItemWithDescription(new Item.Settings()));
    public static final Registered<Item> WATERING_CAN = ITEMS.register("watering_can",
        () -> new WateringCanItem(
            new Item.Settings()
                .component(AdornComponentTypes.FERTILIZER_LEVEL.get(), 0)
                .component(AdornComponentTypes.WATER_LEVEL.get(), 0)
        ));

    public static final RegisteredMap<ConeVariant, Item> CONES = Registrar.registerBy(
        ConeVariant.values(),
        variant -> ITEMS.register(variant.id() + "_cone", () -> ItemBridge.get().createConeItem(variant, new Item.Settings()))
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

    private static Registered<Item> registerCautionSign(String name, Registered<? extends Block> standing, Registered<? extends Block> wall) {
        return ITEMS.register(
            name,
            settings -> new VerticallyAttachableBlockItemWithDescription(
                standing.get(),
                wall.get(),
                new Item.Settings(),
                Direction.DOWN
            )
        );
    }

    private static FoodComponent.Builder drinkFoodComponentBuilder() {
        return new FoodComponent.Builder().nutrition(4).saturationModifier(0.3F).alwaysEdible();
    }

    public static void init() {
    }
}
