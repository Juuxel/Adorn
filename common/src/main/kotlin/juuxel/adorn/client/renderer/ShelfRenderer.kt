package juuxel.adorn.client.renderer

import juuxel.adorn.block.ShelfBlock
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.inventory.Inventory
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3f

@Environment(EnvType.CLIENT)
class ShelfRenderer : BlockEntityRenderer<BlockEntity> {
    override fun render(
        be: BlockEntity, tickDelta: Float,
        matrices: MatrixStack, vertexConsumerProvider: VertexConsumerProvider, light: Int, overlay: Int
    ) {
        // TODO: Do the seeds here need extra attention
        be as Inventory

        val facing = be.cachedState[ShelfBlock.FACING]

        // For first item
        val tx1 = when (facing) {
            Direction.SOUTH, Direction.WEST -> 12 / 16.0
            else -> 4 / 16.0
        }
        val tz1 = when (facing) {
            Direction.NORTH, Direction.WEST -> 12 / 16.0
            else -> 4 / 16.0
        }

        // For second item
        val tx2 = when (facing) {
            Direction.NORTH, Direction.WEST -> 12 / 16.0
            else -> 4 / 16.0
        }
        val tz2 = when (facing) {
            Direction.NORTH, Direction.EAST -> 12 / 16.0
            else -> 4 / 16.0
        }

        val itemRenderer = MinecraftClient.getInstance().itemRenderer

        matrices.push()
        matrices.translate(tx1, 9.6 / 16.0, tz1)
        matrices.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE)
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(ITEM_1_Y_ROT + 180 - facing.asRotation()))
        itemRenderer.renderItem(
            be.getStack(0),
            ModelTransformation.Mode.FIXED,
            light,
            overlay,
            matrices,
            vertexConsumerProvider,
            0 // seed
        )
        matrices.pop()

        matrices.push()
        matrices.translate(tx2, 9.6 / 16.0, tz2)
        matrices.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE)
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(ITEM_2_Y_ROT + 180 - facing.asRotation()))
        itemRenderer.renderItem(
            be.getStack(1),
            ModelTransformation.Mode.FIXED,
            light,
            overlay,
            matrices,
            vertexConsumerProvider,
            0 // seed
        )
        matrices.pop()
    }

    companion object {
        private const val ITEM_SCALE: Float = 0.5f
        private const val ITEM_1_Y_ROT: Float = 10f
        private const val ITEM_2_Y_ROT: Float = -17f
    }
}
