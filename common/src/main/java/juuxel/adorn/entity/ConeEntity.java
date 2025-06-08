package juuxel.adorn.entity;

import juuxel.adorn.item.AdornItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.PositionInterpolator;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;

// TODO: Drops
// TODO: Placement sounds
public final class ConeEntity extends Entity {
    private static final TrackedData<DyeColor> COLOR = DataTracker.registerData(ConeEntity.class, AdornTrackedDataHandlers.DYE_COLOR.get());
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
        setVelocity(getVelocity().multiply(0.98));
        tickBlockCollision();
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(COLOR, DyeColor.ORANGE);
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        if (isRemoved()) return true;
        if (isAlwaysInvulnerableTo(source)) return false;

        if (!(source.getAttacker() instanceof PlayerEntity player) || !player.getAbilities().creativeMode) {
            drop();
        }
        kill(world);

        return true;
    }

    private void drop() {
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    public DyeColor getColor() {
        return getDataTracker().get(COLOR);
    }

    public void setColor(DyeColor color) {
        getDataTracker().set(COLOR, color);
    }

    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(AdornItems.CONES.getEager(getColor()));
    }
}
