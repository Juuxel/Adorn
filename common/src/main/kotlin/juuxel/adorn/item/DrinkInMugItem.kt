package juuxel.adorn.item

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundEvent
import net.minecraft.util.UseAction
import net.minecraft.world.World

open class DrinkInMugItem(settings: Settings) : ItemWithDescription(settings) {
    override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
        val result = super.finishUsing(stack, world, user)
        return if (user is PlayerEntity && user.abilities.creativeMode) result else ItemStack(AdornItems.MUG)
    }

    override fun getUseAction(stack: ItemStack): UseAction =
        UseAction.DRINK

    override fun getEatSound(): SoundEvent = drinkSound
}
