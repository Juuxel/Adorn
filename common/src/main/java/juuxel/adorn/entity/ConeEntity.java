package juuxel.adorn.entity;

import juuxel.adorn.item.AdornItems;
import juuxel.adorn.platform.BlockBridge;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public final class ConeEntity extends Entity {
    private static final TrackedData<Byte> VARIANT = DataTracker.registerData(ConeEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final String NBT_VARIANT = "Variant";

    public ConeEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected Text getDefaultName() {
        return getVariant().displayName();
    }

    @Override
    protected double getGravity() {
        return 0.08;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        if (canMoveVoluntarily()) {
            travel();
        }

        tickConeCramming();
    }

    private void tickConeCramming() {
        List<Entity> crammed = getWorld().getOtherEntities(this, getBoundingBox(), entity -> entity.getType() == AdornEntities.CONE.get());

        for (Entity entity : crammed) {
            entity.pushAwayFrom(this);
        }
    }

    private void travel() {
        if (EntityBridge.get().isInFluid(this)) {
            travelInFluids();
        } else {
            travelInAir();
        }
    }

    private void travelInAir() {
        applyGravity();
        move(MovementType.SELF, getVelocity());
        BlockPos pos = getVelocityAffectingPos();
        float slipperiness = isOnGround() ? BlockBridge.get().getSlipperiness(getWorld().getBlockState(pos), getWorld(), pos, this) : 1;
        slipperiness = Math.min(1f, slipperiness);
        double horizontalSpeedMultiplier = slipperiness / (0.95 * getVariant().weight());
        var velocity = getVelocity();
        setVelocity(velocity.x * horizontalSpeedMultiplier, velocity.y, velocity.z * horizontalSpeedMultiplier);
    }

    private void travelInFluids() {
        applyFluidGravity();
        move(MovementType.SELF, getVelocity());
        var fluidState = getFluidStateAtPos();
        double horizontalDrag = Math.exp(-0.03 * fluidState.getFluid().getTickRate(getWorld()));
        float verticalDrag = 0.8f;
        var velocity = getVelocity();
        double verticalVelocity = velocity.y;

        if (getVariant().floatsIn(fluidState)) {
            boolean surfacing = getWorld().getFluidState(getBlockPos().up()).isEmpty();
            double gravityCoefficient = surfacing ? fluidState.getHeight(getWorld(), getBlockPos()) - MathHelper.fractionalPart(getY()) : 1;
            verticalVelocity += gravityCoefficient * getFinalGravity();
        }

        verticalVelocity *= verticalDrag;
        setVelocity(horizontalDrag * velocity.x, verticalVelocity, horizontalDrag * velocity.z);
    }

    private FluidState getFluidStateAtPos() {
        return getWorld().getFluidState(getBlockPos());
    }

    private void applyFluidGravity() {
        double gravity = getFinalGravity();
        if (gravity != 0.0) {
            Vec3d velocity = getVelocity();
            double velocityY = velocity.y;
            boolean falling = velocityY < 0;

            if (falling && Math.abs(velocityY - 0.005) >= 0.003 && Math.abs(velocityY - gravity / 16.0) < 0.003) {
                // Apply terminal speed
                velocityY = -0.003;
            } else {
                velocityY -= gravity / 16.0;
            }

            setVelocity(velocity.x, velocityY, velocity.z);
        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(VARIANT, (byte) ConeVariant.ORANGE.ordinal());
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (isRemoved()) return true;
        if (isInvulnerableTo(source)) return false;

        if (getWorld() instanceof ServerWorld world && !(source.getAttacker() instanceof PlayerEntity player && player.getAbilities().creativeMode)) {
            drop(world);
        }
        kill();

        return true;
    }

    @Override
    public boolean isFireImmune() {
        return !getVariant().canBurn();
    }

    private void drop(ServerWorld world) {
        if (world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
            dropStack(createItemStack());
        }
    }

    private ItemStack createItemStack() {
        return new ItemStack(AdornItems.CONES.getEager(getVariant()));
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        setVariant(Objects.requireNonNullElse(ConeVariant.fromId(nbt.getString(NBT_VARIANT)), ConeVariant.ORANGE));
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putString(NBT_VARIANT, getVariant().id());
    }

    public ConeVariant getVariant() {
        return ConeVariant.fromOrdinal(getDataTracker().get(VARIANT));
    }

    public void setVariant(ConeVariant variant) {
        getDataTracker().set(VARIANT, (byte) variant.ordinal());
    }

    @Override
    public ItemStack getPickBlockStack() {
        return createItemStack();
    }
}
