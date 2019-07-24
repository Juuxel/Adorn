package juuxel.polyester.block

import net.minecraft.util.Identifier

data class WoodType(val id: Identifier) {
    companion object {
        val OAK = WoodType(Identifier("minecraft", "oak"))
        val SPRUCE = WoodType(Identifier("minecraft", "spruce"))
        val BIRCH = WoodType(Identifier("minecraft", "birch"))
        val JUNGLE = WoodType(Identifier("minecraft", "jungle"))
        val ACACIA = WoodType(Identifier("minecraft", "acacia"))
        val DARK_OAK = WoodType(Identifier("minecraft", "dark_oak"))
    }
}
