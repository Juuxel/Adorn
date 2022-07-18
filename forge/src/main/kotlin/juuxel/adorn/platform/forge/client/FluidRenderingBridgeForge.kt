package juuxel.adorn.platform.forge.client

import juuxel.adorn.client.FluidRenderingBridge
import juuxel.adorn.fluid.FluidReference
import juuxel.adorn.fluid.FluidUnit
import juuxel.adorn.platform.forge.util.toFluidStack
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
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions
import net.minecraftforge.registries.ForgeRegistries

object FluidRenderingBridgeForge : FluidRenderingBridge {
    @OnlyIn(Dist.CLIENT)
    override fun getStillSprite(volume: FluidReference): Sprite? {
        val fluid: Fluid = volume.fluid
        val atlas = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
        return atlas.apply(IClientFluidTypeExtensions.of(fluid).getStillTexture(volume.toFluidStack()))
    }

    @OnlyIn(Dist.CLIENT)
    override fun getColor(volume: FluidReference, world: BlockRenderView?, pos: BlockPos?): Int {
        val fluid: Fluid = volume.fluid
        return if (world != null && pos != null) {
            IClientFluidTypeExtensions.of(fluid).getTintColor(fluid.defaultState, world, pos)
        } else {
            IClientFluidTypeExtensions.of(fluid).getTintColor(volume.toFluidStack())
        }
    }

    @OnlyIn(Dist.CLIENT)
    override fun fillsFromTop(volume: FluidReference): Boolean {
        val fluid: Fluid = volume.fluid
        return fluid.fluidType.isLighterThanAir
    }

    @OnlyIn(Dist.CLIENT)
    override fun getTooltip(volume: FluidReference, context: TooltipContext, maxAmountInLitres: Int?): List<Text> = buildList {
        val fluid: Fluid = volume.fluid
        val stack = volume.toFluidStack()
        val name = stack.displayName
        add(Text.empty().append(name).styled(fluid.fluidType.getRarity(stack).styleModifier))

        if (maxAmountInLitres != null) {
            add(volume.getAmountText(maxAmountInLitres.toLong(), FluidUnit.LITRE))
        } else {
            add(volume.getAmountText())
        }

        // Append ID if advanced
        if (context.isAdvanced) {
            add(Text.literal(ForgeRegistries.FLUIDS.getKey(fluid).toString()).formatted(Formatting.DARK_GRAY))
        }
    }
}
