package juuxel.adorn.entity;

import juuxel.adorn.block.SeatBlock;
import juuxel.adorn.platform.PlatformBridges;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// TODO: Rewrite using BlockAttachedEntity
public final class SeatEntity extends Entity {
    private static final EntityDataAccessor<BlockPos> SEAT_POS = SynchedEntityData.defineId(SeatEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final String NBT_SEAT_POS = "SeatPos";
    private BlockPos seatPos;

    public SeatEntity(EntityType<?> type, Level world) {
        super(type, world);
        noPhysics = true;
        setInvulnerable(true);
        seatPos = BlockPos.containing(position());
    }

    private void setSeatPos(BlockPos seatPos) {
        this.seatPos = seatPos;
        entityData.set(SEAT_POS, seatPos);
    }

    public void setPos(BlockPos pos) {
        if (level().isClientSide()) {
            throw new IllegalStateException("setPos must be called on the logical server");
        }
        absSnapTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        setSeatPos(pos);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        player.startRiding(this);
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean hurtServer(ServerLevel world, DamageSource source, float amount) {
        return false;
    }

    @Override
    public void remove(RemovalReason reason) {
        ejectPassengers();
        if (!level().isClientSide()) {
            PlatformBridges.get().getNetwork().sendToTracking(this, new ClientboundSetPassengersPacket(this));
        }
        var state = level().getBlockState(seatPos);
        if (state.getBlock() instanceof SeatBlock) {
            level().setBlockAndUpdate(seatPos, state.setValue(SeatBlock.OCCUPIED, false));
        }
        super.remove(reason);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean isInvisible() {
        return true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SEAT_POS, BlockPos.ZERO);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput view) {
        seatPos = view.read(NBT_SEAT_POS, BlockPos.CODEC).orElse(BlockPos.ZERO);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput view) {
        view.store(NBT_SEAT_POS, BlockPos.CODEC, seatPos);
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scaleFactor) {
        var seatPos = entityData.get(SEAT_POS);
        var state = level().getBlockState(seatPos);
        var block = state.getBlock();

        // Add the offset that comes from the block's shape
        var blockOffset = block instanceof SeatBlock seat ? seat.getSittingOffset(level(), state, seatPos) : 0.0;
        // Remove the inherent offset that comes from this entity not being directly where the block is
        var posOffset = getY() - seatPos.getY();

        return new Vec3(0, blockOffset - posOffset, 0);
    }

    @Override
    public void tick() {
        super.tick();

        if (!isVehicle()) {
            discard();
        }
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        BlockPos seatPos = entityData.get(SEAT_POS);
        BlockState state = level().getBlockState(seatPos);
        Block block = state.getBlock();
        Direction preferred = block instanceof SeatBlock seat ? seat.getPreferredDismountDirection(state, passenger) : passenger.getDirection();

        // try the following, in order
        // 1. at the seat pos
        // 2. offset to preferred
        // 3. offset to preferred CW, CCW
        // 4. offset to preferred.opposite()
        // 5. y + 1, middle
        // 6. the same steps but y shifted up
        // 7. if nothing else works, seatPos.up()

        @Nullable Direction[] directions = {
            null,
            preferred,
            preferred.getClockWise(),
            preferred.getCounterClockWise(),
            preferred.getOpposite()
        };
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        List<Vec3> positionCandidates = new ArrayList<>(10);

        for (int y = 0; y <= 1; y++) {
            for (Direction direction : directions) {
                pos.set(seatPos.getX(), seatPos.getY() + y, seatPos.getZ());
                if (direction != null) pos.move(direction);

                double height = level().getBlockFloorHeight(pos);

                if (DismountHelper.isBlockFloorValid(height)) {
                    positionCandidates.add(Vec3.upFromBottomCenterOf(pos, height));
                }
            }
        }

        for (Pose pose : passenger.getDismountPoses()) {
            for (Vec3 candidate : positionCandidates) {
                if (DismountHelper.canDismountTo(level(), candidate, passenger, pose)) {
                    passenger.setPose(pose);
                    return candidate;
                }
            }
        }

        // Horizontal center pos of the block above
        return Vec3.upFromBottomCenterOf(seatPos, 1.0);
    }

    // Should be called when the player logs out (incl. closing a singleplayer world)
    // to remove any seat entities they're riding. Otherwise, the game will recreate
    // an invalid seat entity when the player relogs.
    public static void stopSitting(Entity entity) {
        while (entity.isPassenger()) {
            var vehicle = entity.getVehicle();
            assert vehicle != null;
            if (vehicle instanceof SeatEntity) {
                vehicle.discard();
            }
            entity = vehicle;
        }
    }
}
