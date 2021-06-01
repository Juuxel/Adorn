package juuxel.adorn.upgrade

object BlockEntityUpdates {
    private val updates: Map<String, String> = mapOf(
        "adorn:oak_shelf" to "adorn:shelf",
        "adorn:oak_kitchen_cupboard" to "adorn:kitchen_cupboard",
        "adorn:oak_drawer" to "adorn:drawer"
    )

    @JvmStatic
    operator fun get(str: String): String = updates.getOrDefault(str, str)
}
