package juuxel.adorn.platform

import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.menu.MenuType
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.sound.SoundEvent

interface RegistrarFactory {
    fun block(): Registrar<Block>
    fun item(): Registrar<Item>
    fun blockEntity(): Registrar<BlockEntityType<*>>
    fun entity(): Registrar<EntityType<*>>
    fun menu(): Registrar<MenuType<*>>
    fun soundEvent(): Registrar<SoundEvent>
    fun recipeSerializer(): Registrar<RecipeSerializer<*>>
    fun recipeType(): Registrar<RecipeType<*>>
}
