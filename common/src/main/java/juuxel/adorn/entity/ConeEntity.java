package juuxel.adorn.entity;

import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.component.ConeVariantComponent;
import juuxel.adorn.item.AdornItems;
import juuxel.adorn.lib.registry.AdornRegistryKeys;
import juuxel.adorn.platform.BlockBridge;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.InterpolationHandler;
import net.minecraft.world.entity.variant.VariantUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ConeEntity extends Entity {
    private static final EntityDataAccessor<Holder<ConeVariant>> VARIANT = SynchedEntityData.defineId(ConeEntity.class, AdornTrackedDataHandlers.CONE_VARIANT.get());
    private final InterpolationHandler interpolator = new InterpolationHandler(this);

    public ConeEntity(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    protected Component getTypeName() {
        return ConeVariant.getName(getVariant());
    }

    @Override
    public InterpolationHandler getInterpolation() {
        return interpolator;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.08;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        interpolator.interpolate();

        if (canSimulateMovement()) {
            travel();
        }

        if (!level().isClientSide() || isLocalInstanceAuthoritative()) {
            applyEffectsFromBlocks();
        }

        tickConeCramming();
    }

    private void tickConeCramming() {
        List<Entity> crammed = level().getPushableEntities(this, getBoundingBox());

        for (Entity entity : crammed) {
            if (entity.getType() == AdornEntities.CONE.get()) {
                entity.push(this);
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
        move(MoverType.SELF, getDeltaMovement());
        BlockPos pos = getBlockPosBelowThatAffectsMyMovement();
        float slipperiness = onGround() ? BlockBridge.get().getSlipperiness(level().getBlockState(pos), level(), pos, this) : 1;
        slipperiness = Math.min(1f, slipperiness);
        double horizontalSpeedMultiplier = slipperiness / (0.95 * getVariant().value().weight());
        var velocity = getDeltaMovement();
        setDeltaMovement(velocity.x * horizontalSpeedMultiplier, velocity.y, velocity.z * horizontalSpeedMultiplier);
    }

    private void travelInFluids() {
        applyFluidGravity();
        move(MoverType.SELF, getDeltaMovement());
        var fluidState = getFluidStateAtPos();
        double horizontalDrag = Math.exp(-0.03 * fluidState.getType().getTickDelay(level()));
        float verticalDrag = 0.8f;
        var velocity = getDeltaMovement();
        double verticalVelocity = velocity.y;

        if (fluidState.is(getVariant().value().floatsIn())) {
            boolean surfacing = level().getFluidState(blockPosition().above()).isEmpty();
            double gravityCoefficient = surfacing ? fluidState.getHeight(level(), blockPosition()) - Mth.frac(getY()) : 1;
            verticalVelocity += gravityCoefficient * getGravity();
        }

        verticalVelocity *= verticalDrag;
        setDeltaMovement(horizontalDrag * velocity.x, verticalVelocity, horizontalDrag * velocity.z);
    }

    private FluidState getFluidStateAtPos() {
        return level().getFluidState(blockPosition());
    }

    private void applyFluidGravity() {
        double gravity = getGravity();
        if (gravity != 0.0) {
            Vec3 velocity = getDeltaMovement();
            double velocityY = velocity.y;
            boolean falling = velocityY < 0;

            if (falling && Math.abs(velocityY - 0.005) >= 0.003 && Math.abs(velocityY - gravity / 16.0) < 0.003) {
                // Apply terminal speed
                velocityY = -0.003;
            } else {
                velocityY -= gravity / 16.0;
            }

            setDeltaMovement(velocity.x, velocityY, velocity.z);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(VARIANT, VariantUtils.getDefaultOrAny(registryAccess(), ConeVariant.Keys.ORANGE));
    }

    @Override
    public boolean hurtServer(ServerLevel world, DamageSource source, float amount) {
        if (isRemoved()) return true;
        if (isInvulnerableToBase(source)) return false;

        if (!(source.getEntity() instanceof Player player) || !player.getAbilities().instabuild) {
            drop(world);
        }
        kill(world);

        return true;
    }

    @Override
    public boolean fireImmune() {
        return !getVariant().value().canBurn();
    }

    private void drop(ServerLevel world) {
        if (world.getGameRules().get(GameRules.ENTITY_DROPS)) {
            spawnAtLocation(world, createItemStack());
        }
    }

    private ItemStack createItemStack() {
        var stack = new ItemStack(AdornItems.CONE.get());
        stack.set(AdornComponentTypes.CONE_VARIANT.get(), new ConeVariantComponent(getVariant()));
        return stack;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput view) {
        VariantUtils.readVariant(view, AdornRegistryKeys.CONE_VARIANT).ifPresent(this::setVariant);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput view) {
        VariantUtils.writeVariant(view, getVariant());
    }

    @Override
    public @Nullable <T> T get(DataComponentType<? extends T> type) {
        if (type == AdornComponentTypes.CONE_VARIANT.get()) {
            return castComponentValue(type, new ConeVariantComponent(getVariant()));
        }

        return super.get(type);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter from) {
        applyImplicitComponentIfPresent(from, AdornComponentTypes.CONE_VARIANT.get());
        super.applyImplicitComponents(from);
    }

    @Override
    protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
        if (type == AdornComponentTypes.CONE_VARIANT.get()) {
            setVariant(castComponentValue(AdornComponentTypes.CONE_VARIANT.get(), value).getVariant(registryAccess()).orElseThrow());
            return true;
        }

        return super.applyImplicitComponent(type, value);
    }

    public Holder<ConeVariant> getVariant() {
        return getEntityData().get(VARIANT);
    }

    public void setVariant(Holder<ConeVariant> variant) {
        getEntityData().set(VARIANT, variant);
    }

    @Override
    public ItemStack getPickResult() {
        return createItemStack();
    }
}
