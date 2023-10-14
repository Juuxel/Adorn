package juuxel.adorn.item

import juuxel.adorn.fluid.FluidUnit
import juuxel.adorn.fluid.StepMaximum
import juuxel.adorn.platform.FluidBridge
import juuxel.adorn.util.color
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FarmlandBlock
import net.minecraft.block.Fertilizable
import net.minecraft.block.FluidDrainable
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtElement
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.RaycastContext
import net.minecraft.world.World
import net.minecraft.world.WorldEvents
import net.minecraft.world.event.GameEvent
import kotlin.math.min

// TODO: Recipes, recipe advancements
// TODO: Fertilizer tooltip, description
// TODO: Texture
// TODO: Translation
class WateringCanItem(settings: Settings) : ItemWithDescription(settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)
        var success = false

        val hitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY)
        if (hitResult.type != HitResult.Type.BLOCK) {
            return TypedActionResult.pass(stack)
        }

        val nbt = stack.getOrCreateNbt()
        var waterLevel = nbt.getInt(NBT_WATER_LEVEL)
        val pos = hitResult.blockPos
        val state = world.getBlockState(pos)
        val block = state.block

        if (waterLevel < MAX_WATER_LEVEL) {
            // Check for drainable water

            // Note: we have a water check because we can't revert changes for non-water fluid sources
            if (block is FluidDrainable && world.getFluidState(pos).isOf(Fluids.WATER)) {
                val drained = block.tryDrainFluid(user, world, pos, state)

                if (drained.isOf(Items.WATER_BUCKET)) {
                    waterLevel = min(waterLevel + WATER_LEVELS_PER_BUCKET, MAX_WATER_LEVEL)
                    nbt.putInt(NBT_WATER_LEVEL, waterLevel)
                    success = true
                }
            } else {
                val drained = FluidBridge.get().drain(world, pos, null, hitResult.side.opposite, Fluids.WATER, FLUID_DRAIN_PREDICATE)

                if (drained != null) {
                    val amount = FluidUnit.convert(drained.amount, from = drained.unit, to = FluidUnit.LITRE)
                    val levels = (amount / FLUID_DRAIN_PREDICATE.step).toInt()
                    waterLevel = min(waterLevel + levels, MAX_WATER_LEVEL)
                    nbt.putInt(NBT_WATER_LEVEL, waterLevel)
                    success = true
                }
            }
        }

        if (!success && waterLevel > 0) {
            success = true

            waterLevel--
            nbt.putInt(NBT_WATER_LEVEL, waterLevel)
            world.emitGameEvent(user, GameEvent.ITEM_INTERACT_FINISH, pos)

            val mut = BlockPos.Mutable()
            for (xo in -1..1) {
                for (zo in -1..1) {
                    mut.set(pos.x + xo, pos.y, pos.z + zo)
                    water(world, mut, user, stack)

                    if (world is ServerWorld) {
                        spawnParticlesAt(world, mut, hitResult.pos.y)
                    }
                }
            }
        }

        return if (success) {
            TypedActionResult.success(stack, world.isClient())
        } else {
            TypedActionResult.pass(stack)
        }
    }

    private fun water(world: World, pos: BlockPos, player: PlayerEntity, stack: ItemStack) {
        val nbt = stack.getOrCreateNbt()
        val fertilizerLevel = nbt.getInt(NBT_FERTILIZER_LEVEL)
        val state = world.getBlockState(pos)
        val block = state.block

        if (fertilizerLevel > 0 && world.random.nextInt(9) == 0) {
            if (block is Fertilizable && block.isFertilizable(world, pos, state)) {
                if (world is ServerWorld && block.canGrow(world, world.random, pos, state)) {
                    block.grow(world, world.random, pos, state)
                }

                world.syncWorldEvent(player, WorldEvents.BONE_MEAL_USED, pos, 5)
            }

            nbt.putInt(NBT_FERTILIZER_LEVEL, fertilizerLevel - 1)
        }

        if (!world.isClient) {
            if (block is FarmlandBlock) {
                waterFarmlandBlock(world, pos, state)
            } else if (!state.isFullCube(world, pos)) { // We can't water through full cubes
                val downPos = pos.down()
                val downState = world.getBlockState(downPos)

                if (downState.block is FarmlandBlock) {
                    waterFarmlandBlock(world, downPos, downState)
                }
            }
        }
    }

    private fun waterFarmlandBlock(world: World, pos: BlockPos, state: BlockState) {
        val moisture = state.get(FarmlandBlock.MOISTURE)

        if (moisture < FarmlandBlock.MAX_MOISTURE) {
            val moistureChange = world.random.nextBetween(2, 6)
            val newMoisture = min(moisture + moistureChange, FarmlandBlock.MAX_MOISTURE)
            world.setBlockState(pos, state.with(FarmlandBlock.MOISTURE, newMoisture), Block.NOTIFY_LISTENERS)
        }
    }

    override fun isItemBarVisible(stack: ItemStack): Boolean
        = true

    override fun getItemBarStep(stack: ItemStack): Int {
        if (stack.hasNbt()) {
            val nbt = stack.nbt!!

            if (nbt.contains(NBT_WATER_LEVEL, NbtElement.INT_TYPE.toInt())) {
                val waterLevel = MathHelper.clamp(nbt.getInt(NBT_WATER_LEVEL), 0, MAX_WATER_LEVEL)
                return MathHelper.lerp(WATER_LEVEL_DIVISOR * waterLevel, 0, ITEM_BAR_STEPS)
            }
        }

        return 0
    }

    override fun getItemBarColor(stack: ItemStack): Int {
        val nbt = stack.getOrCreateNbt()
        val rg = MathHelper.clampedMap(
            nbt.getInt(NBT_FERTILIZER_LEVEL).toFloat(),
            // From:
            0f, MAX_FERTILIZER_LEVEL.toFloat(),
            // To:
            0.4f, 1f
        )
        return color(red = rg, green = rg, blue = 1f)
    }

    companion object {
        private const val NBT_WATER_LEVEL = "WaterLevel"
        private const val NBT_FERTILIZER_LEVEL = "FertilizerLevel"
        private const val ITEM_BAR_STEPS = 13
        private const val MAX_WATER_LEVEL = 50
        const val MAX_FERTILIZER_LEVEL = 32
        private const val WATER_LEVEL_DIVISOR = 1f / MAX_WATER_LEVEL
        private const val WATER_LEVELS_PER_BUCKET = 10

        private val FLUID_DRAIN_PREDICATE = StepMaximum(min = 0L, max = 1000L, step = 1000L / WATER_LEVELS_PER_BUCKET, unit = FluidUnit.LITRE)

        private fun spawnParticlesAt(world: ServerWorld, pos: BlockPos, y: Double) {
            val px = pos.x.toDouble() + 0.3 + world.random.nextDouble() * 0.4
            val py = y + 0.1
            val pz = pos.z.toDouble() + 0.3 + world.random.nextDouble() * 0.4
            val vx = world.random.nextDouble() * 0.2 - 0.1
            val vy = 0.1
            val vz = world.random.nextDouble() * 0.2 - 0.1
            world.spawnParticles(ParticleTypes.SPLASH, px, py, pz, 4, vx, vy, vz, 0.5)
        }
    }
}
