package juuxel.adorn.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;

public class DrinkInMugItem extends Item {
    public DrinkInMugItem(Properties settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        var result = super.finishUsingItem(stack, world, user);
        return user instanceof Player player && player.getAbilities().instabuild ? result : new ItemStack(AdornItems.MUG.get());
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.DRINK;
    }
}
