package juuxel.adorn.item;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public final class AdornItems {
    public static final Registrar<Item> ITEMS = RegistrarFactory.get().create(RegistryKeys.ITEM);
    private static final FoodComponent DRINK_FOOD_COMPONENT = drinkFoodComponentBuilder().build();

    public static final Registered<Item> STONE_ROD = register("stone_rod", ItemWithDescription::new);
    public static final Registered<Item> MUG = register("mug", ItemWithDescription::new, () -> new Item.Settings().maxCount(16));
    public static final Registered<Item> HOT_CHOCOLATE = register("hot_chocolate",
        DrinkInMugItem::new,
        () -> new Item.Settings().food(DRINK_FOOD_COMPONENT, ConsumableComponents.DRINK).maxCount(1)
    );
    public static final Registered<Item> SWEET_BERRY_JUICE = register("sweet_berry_juice",
        DrinkInMugItem::new,
        () -> new Item.Settings().food(DRINK_FOOD_COMPONENT, ConsumableComponents.DRINK).maxCount(1)
    );
    public static final Registered<Item> GLOW_BERRY_TEA = register("glow_berry_tea",
        DrinkInMugItem::new,
        () -> new Item.Settings()
            .food(DRINK_FOOD_COMPONENT, ConsumableComponents.DRINK)
            .component(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS, new SuspiciousStewEffectsComponent(List.of(
                new SuspiciousStewEffectsComponent.StewEffect(StatusEffects.GLOWING, 400)
            )))
            .maxCount(1)
    );
    public static final Registered<Item> NETHER_WART_COFFEE = register("nether_wart_coffee",
        DrinkInMugItem::new,
        () -> new Item.Settings().food(DRINK_FOOD_COMPONENT, ConsumableComponents.DRINK).maxCount(1)
    );

    public static final Registered<Item> STONE_TORCH = ITEMS.register("stone_torch",
        key -> new VerticallyAttachableBlockItemWithDescription(
            AdornBlocks.STONE_TORCH_GROUND.get(),
            AdornBlocks.STONE_TORCH_WALL.get(),
            Direction.DOWN,
            new Item.Settings().registryKey(key).useBlockPrefixedTranslationKey()
        ));

    public static final Registered<Item> GUIDE_BOOK = register("guide_book",
        settings -> new AdornBookItem(AdornCommon.id("guide"), settings),
        () -> new Item.Settings().rarity(Rarity.UNCOMMON)
    );
    public static final Registered<Item> TRADERS_MANUAL = register("traders_manual",
        settings -> new AdornBookItem(AdornCommon.id("traders_manual"), settings));

    public static final Registered<Item> COPPER_NUGGET = register("copper_nugget", ItemWithDescription::new);
    public static final Registered<Item> WATERING_CAN = register("watering_can",
        WateringCanItem::new,
        () -> new Item.Settings()
            .component(AdornComponentTypes.FERTILIZER_LEVEL.get(), WateringCanItem.FertilizerLevel.ZERO)
            .component(AdornComponentTypes.WATER_LEVEL.get(), 0)
    );

    private static Registered<Item> register(String name, Function<Item.Settings, Item> factory) {
        return register(name, factory, Item.Settings::new);
    }

    private static Registered<Item> register(String name, Function<Item.Settings, Item> factory, Supplier<Item.Settings> settings) {
        return ITEMS.register(name, key -> factory.apply(settings.get().registryKey(key)));
    }

    private static FoodComponent.Builder drinkFoodComponentBuilder() {
        return new FoodComponent.Builder().nutrition(4).saturationModifier(0.3F).alwaysEdible();
    }

    public static void init() {
    }
}
