package juuxel.adorn

import net.fabricmc.loader.api.FabricLoader
import org.spongepowered.asm.lib.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

internal class AdornMixinPlugin : IMixinConfigPlugin {
    private val conditions = mapOf<String, () -> Boolean>(
        "juuxel.adorn.mixin.fluidloggable.FluidloggableMixin" to { FabricLoader.getInstance().isModLoaded("towelette") },
        "juuxel.adorn.mixin.fluidloggable.ChairBlockMixin" to { FabricLoader.getInstance().isModLoaded("towelette") }
    )

    override fun shouldApplyMixin(targetClassName: String, mixinClassName: String) =
        conditions[mixinClassName]?.invoke() ?: true

    // Boilerplate
    override fun acceptTargets(myTargets: MutableSet<String>?, otherTargets: MutableSet<String>?) {}

    override fun onLoad(mixinPackage: String?) {}

    override fun preApply(
        targetClassName: String?, targetClass: ClassNode?, mixinClassName: String?, mixinInfo: IMixinInfo?
    ) {}

    override fun postApply(
        targetClassName: String?, targetClass: ClassNode?, mixinClassName: String?, mixinInfo: IMixinInfo?
    ) {}

    override fun getRefMapperConfig() = null

    override fun getMixins() = null
}
