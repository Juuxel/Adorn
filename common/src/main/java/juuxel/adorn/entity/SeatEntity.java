package juuxel.adorn.entity;

import juuxel.adorn.block.SeatBlock;
import juuxel.adorn.platform.PlatformBridges;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Dismounting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

// TODO: Rewrite using BlockAttachedEntity
public final class SeatEntity extends Entity {
    private static final TrackedData<BlockPos> SEAT_POS = DataTracker.registerData(SeatEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    private static final String NBT_SEAT_POS = "SeatPos";
    private BlockPos seatPos;

    public SeatEntity(EntityType<?> type, World world) {
        super(type, world);
        noClip = true;
        setInvulnerable(true);
        seatPos = BlockPos.ofFloored(getPos());
    }

    private void setSeatPos(BlockPos seatPos) {
        this.seatPos = seatPos;
        dataTracker.set(SEAT_POS, seatPos);
    }

    public void setPos(BlockPos pos) {
        if (getWorld().isClient) {
            throw new IllegalStateException("setPos must be called on the logical server");
        }
        updatePosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        setSeatPos(pos);
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        player.startRiding(this);
        return ActionResult.SUCCESS;
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }

    @Override
    public void remove(RemovalReason reason) {
        removeAllPassengers();
        if (!getWorld().isClient) {
            PlatformBridges.get().getNetwork().sendToTracking(this, new EntityPassengersSetS2CPacket(this));
        }
        var state = getWorld().getBlockState(seatPos);
        if (state.getBlock() instanceof SeatBlock) {
            getWorld().setBlockState(seatPos, state.with(SeatBlock.OCCUPIED, false));
        }
        super.remove(reason);
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public boolean isInvisible() {
        return true;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(SEAT_POS, BlockPos.ORIGIN);
    }

    @Override
    protected void readCustomData(ReadView view) {
        seatPos = view.read(NBT_SEAT_POS, BlockPos.CODEC).orElse(BlockPos.ORIGIN);
    }

    @Override
    protected void writeCustomData(WriteView view) {
        view.put(NBT_SEAT_POS, BlockPos.CODEC, seatPos);
    }

    @Override
    protected Vec3d getPassengerAttachmentPos(Entity passenger, EntityDimensions dimensions, float scaleFactor) {
        var seatPos = dataTracker.get(SEAT_POS);
        var state = getWorld().getBlockState(seatPos);
        var block = state.getBlock();

        // Add the offset that comes from the block's shape
        var blockOffset = block instanceof SeatBlock seat ? seat.getSittingOffset(getWorld(), state, seatPos) : 0.0;
        // Remove the inherent offset that comes from this entity not being directly where the block is
        var posOffset = getY() - seatPos.getY();

        return new Vec3d(0, blockOffset - posOffset, 0);
    }

    @Override
    public void tick() {
        super.tick();

        if (!hasPassengers()) {
            discard();
        }
    }

    @Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        BlockPos seatPos = dataTracker.get(SEAT_POS);
        BlockState state = getWorld().getBlockState(seatPos);
        Block block = state.getBlock();
        Direction preferred = block instanceof SeatBlock seat ? seat.getPreferredDismountDirection(state, passenger) : passenger.getHorizontalFacing();

        // try the following, in order
        // 1. at the seat pos
        // 2. offset to preferred
        // 3. offset to preferred CW, CCW
        // 4. offset to preferred.opposite()
        // 5. y + 1, middle
        // 6. the same steps but y shifted up
        // 7. if nothing else works, seatPos.up()

        Direction[] directions = {
            null,
            preferred,
            preferred.rotateYClockwise(),
            preferred.rotateYCounterclockwise(),
            preferred.getOpposite()
        };
        BlockPos.Mutable pos = new BlockPos.Mutable();
        List<Vec3d> positionCandidates = new ArrayList<>(10);

        for (int y = 0; y <= 1; y++) {
            for (Direction direction : directions) {
                pos.set(seatPos.getX(), seatPos.getY() + y, seatPos.getZ());
                if (direction != null) pos.move(direction);

                double height = getWorld().getDismountHeight(pos);

                if (Dismounting.canDismountInBlock(height)) {
                    positionCandidates.add(Vec3d.ofCenter(pos, height));
                }
            }
        }

        for (EntityPose pose : passenger.getPoses()) {
            for (Vec3d candidate : positionCandidates) {
                if (Dismounting.canPlaceEntityAt(getWorld(), candidate, passenger, pose)) {
                    passenger.setPose(pose);
                    return candidate;
                }
            }
        }

        // Horizontal center pos of the block above
        return Vec3d.ofCenter(seatPos, 1.0);
    }

    // Should be called when the player logs out (incl. closing a singleplayer world)
    // to remove any seat entities they're riding. Otherwise, the game will recreate
    // an invalid seat entity when the player relogs.
    public static void stopSitting(Entity entity) {
        while (entity.hasVehicle()) {
            var vehicle = entity.getVehicle();
            assert vehicle != null;
            if (vehicle instanceof SeatEntity) {
                vehicle.discard();
            }
            entity = vehicle;
        }
    }
}
