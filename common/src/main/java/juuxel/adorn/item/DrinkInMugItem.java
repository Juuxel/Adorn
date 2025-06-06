package juuxel.adorn.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.world.World;

public class DrinkInMugItem extends Item {
    public DrinkInMugItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        var result = super.finishUsing(stack, world, user);
        return user instanceof PlayerEntity player && player.getAbilities().creativeMode ? result : new ItemStack(AdornItems.MUG.get());
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }
}
