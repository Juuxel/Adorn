package juuxel.adorn.platform.forge.client

import juuxel.adorn.client.FluidRenderingBridge
import juuxel.adorn.fluid.FluidReference
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.texture.Sprite
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.fluid.Fluid
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockRenderView
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.RenderProperties
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.registries.ForgeRegistries

object FluidRenderingBridgeForge : FluidRenderingBridge {
    private fun stackOf(volume: FluidReference): FluidStack =
        FluidStack(volume.fluid, volume.amount.toInt(), volume.nbt)

    @OnlyIn(Dist.CLIENT)
    override fun getStillSprite(volume: FluidReference): Sprite? {
        val fluid: Fluid = volume.fluid
        val atlas = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
        return atlas.apply(RenderProperties.get(fluid).getStillTexture(stackOf(volume)))
    }

    @OnlyIn(Dist.CLIENT)
    override fun getColor(volume: FluidReference, world: BlockRenderView?, pos: BlockPos?): Int {
        val fluid: Fluid = volume.fluid
        return RenderProperties.get(fluid).getColorTint(stackOf(volume))
    }

    @OnlyIn(Dist.CLIENT)
    override fun fillsFromTop(volume: FluidReference): Boolean {
        val fluid: Fluid = volume.fluid
        return fluid.fluidType.isLighterThanAir
    }

    @OnlyIn(Dist.CLIENT)
    override fun getTooltip(volume: FluidReference, context: TooltipContext, maxAmountInLitres: Int?): List<Text> = buildList {
        val fluid: Fluid = volume.fluid
        val stack = stackOf(volume)
        val name = stack.displayName
        add(Text.empty().append(name).styled(fluid.fluidType.getRarity(stack).styleModifier))

        if (maxAmountInLitres != null) {
            add(Text.translatable("gui.adorn.litres_fraction", volume.amount, maxAmountInLitres))
        } else {
            add(Text.translatable("gui.adorn.litres", volume.amount))
        }

        // Append ID if advanced
        if (context.isAdvanced) {
            add(Text.literal(ForgeRegistries.FLUIDS.getKey(fluid).toString()).formatted(Formatting.DARK_GRAY))
        }
    }
}
