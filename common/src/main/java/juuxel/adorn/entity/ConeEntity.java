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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

// TODO: Placement sounds
public final class ConeEntity extends Entity {
    private static final TrackedData<RegistryEntry<ConeVariant>> VARIANT = DataTracker.registerData(ConeEntity.class, AdornTrackedDataHandlers.CONE_VARIANT.get());
    private final PositionInterpolator interpolator = new PositionInterpolator(this);

    public ConeEntity(EntityType<?> type, World world) {
        super(type, world);
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
        applyGravity();
        move(MovementType.SELF, getVelocity());
        updateVelocityInAir();
        tickBlockCollision();
    }

    private void updateVelocityInAir() {
        BlockPos pos = getVelocityAffectingPos();
        float slipperiness = isOnGround() ? BlockBridge.get().getSlipperiness(getWorld().getBlockState(pos), getWorld(), pos, this) : 1;
        slipperiness = Math.min(1f, slipperiness);
        double horizontalSpeedMultiplier = slipperiness / (0.95 * getVariant().value().weight());
        var velocity = getVelocity();
        setVelocity(velocity.x * horizontalSpeedMultiplier, velocity.y, velocity.z * horizontalSpeedMultiplier);
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
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        Variants.readVariantFromNbt(nbt, getRegistryManager(), AdornRegistryKeys.CONE_VARIANT).ifPresent(this::setVariant);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        Variants.writeVariantToNbt(nbt, getVariant());
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
