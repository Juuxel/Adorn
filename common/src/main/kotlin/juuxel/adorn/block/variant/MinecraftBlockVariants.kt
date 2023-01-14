package juuxel.adorn.block.variant

object MinecraftBlockVariants : BlockVariantSet {
    override val woodVariants: List<BlockVariant> = listOf(
        BlockVariant.OAK,
        BlockVariant.SPRUCE,
        BlockVariant.BIRCH,
        BlockVariant.JUNGLE,
        BlockVariant.ACACIA,
        BlockVariant.DARK_OAK,
        BlockVariant.MANGROVE,
        BlockVariant.CRIMSON,
        BlockVariant.WARPED,
    )
    override val stoneVariants: List<BlockVariant> = listOf(
        BlockVariant.STONE,
        BlockVariant.COBBLESTONE,
        BlockVariant.SANDSTONE,
        BlockVariant.DIORITE,
        BlockVariant.ANDESITE,
        BlockVariant.GRANITE,
        BlockVariant.BRICK,
        BlockVariant.STONE_BRICK,
        BlockVariant.RED_SANDSTONE,
        BlockVariant.NETHER_BRICK,
        BlockVariant.BASALT,
        BlockVariant.BLACKSTONE,
        BlockVariant.RED_NETHER_BRICK,
        BlockVariant.PRISMARINE,
        BlockVariant.QUARTZ,
        BlockVariant.END_STONE_BRICK,
        BlockVariant.PURPUR,
        BlockVariant.POLISHED_BLACKSTONE,
        BlockVariant.POLISHED_BLACKSTONE_BRICK,
        BlockVariant.POLISHED_DIORITE,
        BlockVariant.POLISHED_ANDESITE,
        BlockVariant.POLISHED_GRANITE,
        BlockVariant.PRISMARINE_BRICK,
        BlockVariant.DARK_PRISMARINE,
        BlockVariant.CUT_SANDSTONE,
        BlockVariant.SMOOTH_SANDSTONE,
        BlockVariant.CUT_RED_SANDSTONE,
        BlockVariant.SMOOTH_RED_SANDSTONE,
        BlockVariant.SMOOTH_STONE,
        BlockVariant.MOSSY_COBBLESTONE,
        BlockVariant.MOSSY_STONE_BRICK,
        BlockVariant.DEEPSLATE,
        BlockVariant.COBBLED_DEEPSLATE,
        BlockVariant.POLISHED_DEEPSLATE,
        BlockVariant.DEEPSLATE_BRICK,
        BlockVariant.DEEPSLATE_TILE,
    )

    override fun addVariants(consumer: BlockVariantSet.CustomVariantConsumer) {
        consumer.add(BlockVariant.IRON, BlockVariantGroup.METAL, listOf(BlockKind.SHELF))

        for (glass in BlockVariant.STAINED_GLASS.values) {
            consumer.add(glass, BlockVariantGroup.GLASS, listOf())
        }

        for (wool in BlockVariant.WOOLS.values) {
            consumer.add(wool, BlockVariantGroup.WOOL, listOf())
        }
    }

    override fun sortVariants(sorter: BlockVariantSet.Sorter) {
        sorter.moveAfter(variant = BlockVariant.IRON, after = woodVariants.last())
    }
}
