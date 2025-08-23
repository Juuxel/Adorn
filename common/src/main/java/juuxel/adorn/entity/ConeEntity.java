package juuxel.adorn.entity;

import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.component.ConeVariantComponent;
import juuxel.adorn.item.AdornItems;
import juuxel.adorn.lib.registry.AdornRegistryKeys;
import juuxel.adorn.platform.BlockBridge;
import net.minecraft.component.ComponentType;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.PositionInterpolator;
import net.minecraft.entity.Variants;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ConeEntity extends Entity {
    private static final TrackedData<RegistryEntry<ConeVariant>> VARIANT = DataTracker.registerData(ConeEntity.class, AdornTrackedDataHandlers.CONE_VARIANT.get());
    private final PositionInterpolator interpolator = new PositionInterpolator(this);

    public ConeEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected Text getDefaultName() {
        return ConeVariant.getName(getVariant());
    }

    @Override
    public PositionInterpolator getInterpolator() {
        return interpolator;
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
        interpolator.tick();

        if (canMoveVoluntarily()) {
            travel();
        }

        if (!getWorld().isClient() || isLogicalSideForUpdatingMovement()) {
            tickBlockCollision();
        }

        tickConeCramming();
    }

    private void tickConeCramming() {
        List<Entity> crammed = getWorld().getCrammedEntities(this, getBoundingBox());

        for (Entity entity : crammed) {
            if (entity.getType() == AdornEntities.CONE.get()) {
                entity.pushAwayFrom(this);
            }
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
        double horizontalSpeedMultiplier = slipperiness / (0.95 * getVariant().value().weight());
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

        if (fluidState.isIn(getVariant().value().floatsIn())) {
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
        builder.add(VARIANT, Variants.getOrDefaultOrThrow(getRegistryManager(), ConeVariant.Keys.ORANGE));
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        if (isRemoved()) return true;
        if (isAlwaysInvulnerableTo(source)) return false;

        if (!(source.getAttacker() instanceof PlayerEntity player) || !player.getAbilities().creativeMode) {
            drop(world);
        }
        kill(world);

        return true;
    }

    @Override
    public boolean isFireImmune() {
        return !getVariant().value().canBurn();
    }

    private void drop(ServerWorld world) {
        if (world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
            dropStack(world, createItemStack());
        }
    }

    private ItemStack createItemStack() {
        var stack = new ItemStack(AdornItems.CONE.get());
        stack.set(AdornComponentTypes.CONE_VARIANT.get(), new ConeVariantComponent(getVariant()));
        return stack;
    }

    @Override
    protected void readCustomData(ReadView view) {
        Variants.readVariantFromNbt(view, AdornRegistryKeys.CONE_VARIANT).ifPresent(this::setVariant);
    }

    @Override
    protected void writeCustomData(WriteView view) {
        Variants.writeVariantToNbt(view, getVariant());
    }

    @Override
    public @Nullable <T> T get(ComponentType<? extends T> type) {
        if (type == AdornComponentTypes.CONE_VARIANT.get()) {
            return castComponentValue(type, new ConeVariantComponent(getVariant()));
        }

        return super.get(type);
    }

    @Override
    protected void copyComponentsFrom(ComponentsAccess from) {
        copyComponentFrom(from, AdornComponentTypes.CONE_VARIANT.get());
        super.copyComponentsFrom(from);
    }

    @Override
    protected <T> boolean setApplicableComponent(ComponentType<T> type, T value) {
        if (type == AdornComponentTypes.CONE_VARIANT.get()) {
            setVariant(castComponentValue(AdornComponentTypes.CONE_VARIANT.get(), value).getVariant(getRegistryManager()).orElseThrow());
            return true;
        }

        return super.setApplicableComponent(type, value);
    }

    public RegistryEntry<ConeVariant> getVariant() {
        return getDataTracker().get(VARIANT);
    }

    public void setVariant(RegistryEntry<ConeVariant> variant) {
        getDataTracker().set(VARIANT, variant);
    }

    @Override
    public ItemStack getPickBlockStack() {
        return createItemStack();
    }
}
