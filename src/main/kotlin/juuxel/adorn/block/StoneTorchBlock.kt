package juuxel.adorn.block

import io.github.juuxel.polyester.block.PolyesterBlock
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.Blocks
import net.minecraft.block.TorchBlock
import net.minecraft.block.WallTorchBlock
import net.minecraft.sound.BlockSoundGroup

class StoneTorchBlock : TorchBlock(settings), PolyesterBlock {
    override val name = "stone_torch"
    override val itemSettings = null
    override val hasDescription = true

    class Wall(settings: Settings) : WallTorchBlock(settings), PolyesterBlock {
        override val name = "wall_stone_torch"
        override val itemSettings = null
    }

    companion object {
        internal val settings = FabricBlockSettings.copy(Blocks.TORCH)
            .lightLevel(15)
            .sounds(BlockSoundGroup.STONE)
            .build()
    }
}
