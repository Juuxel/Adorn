package juuxel.adorn.block.entity;

import com.google.common.base.Predicates;
import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.util.FluidStorageReference;
import juuxel.adorn.util.NbtUtil;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public final class KitchenSinkBlockEntityFabric extends KitchenSinkBlockEntity {
    public static final BlockApiLookup.BlockApiProvider<Storage<FluidVariant>, @Nullable Direction> FLUID_STORAGE_PROVIDER =
        (world, pos, state, blockEntity, context) -> {
            if (blockEntity instanceof KitchenSinkBlockEntityFabric kitchenSink) {
                return kitchenSink.storage;
            }

            return null;
        };

    private static final String NBT_FLUID = "Fluid";
    private static final String NBT_VOLUME = "Volume";

    private final SingleVariantStorage<FluidVariant> storage = new SingleVariantStorage<>() {
        @Override
        public long extract(FluidVariant extractedVariant, long maxAmount, TransactionContext transaction) {
            return super.extract(extractedVariant, maxAmount, transaction);
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return FluidConstants.BUCKET;
        }

        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected void onFinalCommit() {
            markDirtyAndSync();
        }
    };
    private final FluidReference fluidReference = new FluidStorageReference(storage);

    public KitchenSinkBlockEntityFabric(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public SingleVariantStorage<FluidVariant> getStorage() {
        return storage;
    }

    @Override
    public FluidReference getFluidReference() {
        return fluidReference;
    }

    @Override
    public boolean interactWithItem(ItemStack stack, PlayerEntity player, Hand hand) {
        // StorageUtil.move will mutate the stack and we need it for correct sounds (bottles!).
        var originalStack = stack.copy();
        var itemStorage = FluidStorage.ITEM.find(stack, ContainerItemContext.forPlayerInteraction(player, hand));
        if (itemStorage == null) return false;
        var hasSpace = storage.amount < storage.getCapacity();

        if (hasSpace) {
            var moved = StorageUtil.move(itemStorage, storage, Predicates.alwaysTrue(), Long.MAX_VALUE, null);

            if (moved > 0) {
                onFill(originalStack, player);
                markDirtyAndSync();
                return true;
            }
        }

        // Store fluid before moving (it might become empty!)
        var fluid = fluidReference.createSnapshot();
        var moved = StorageUtil.move(storage, itemStorage, Predicates.alwaysTrue(), Long.MAX_VALUE, null);

        if (moved > 0) {
            onPickUp(fluid, originalStack, player);
            markDirtyAndSync();
            return true;
        }

        return false;
    }

    @Override
    public boolean clearFluidsWithSponge() {
        if (!storage.variant.getFluid().isIn(FluidTags.WATER) || storage.amount == 0L) return false;
        storage.amount = 0;
        markDirtyAndSync();
        return true;
    }

    @Override
    protected FluidItemSound getFillSound(FluidReference fluid, ItemStack stack) {
        return super.getFillSound(fluid, stack).orElse(FluidVariantAttributes.getFillSound(FluidStorageReference.toFluidVariant(fluid)));
    }

    @Override
    protected FluidItemSound getEmptySound(FluidReference fluid, ItemStack stack) {
        return super.getEmptySound(fluid, stack).orElse(FluidVariantAttributes.getEmptySound(FluidStorageReference.toFluidVariant(fluid)));
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        storage.variant = NbtUtil.getWithCodec(nbt, NBT_FLUID, FluidVariant.CODEC, registries);
        storage.amount = nbt.getLong(NBT_VOLUME, 0);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        NbtUtil.putWithCodec(nbt, NBT_FLUID, FluidVariant.CODEC, storage.variant, registries);
        nbt.putLong(NBT_VOLUME, storage.amount);
    }

    @Override
    public int calculateComparatorOutput() {
        if (storage.amount == 0) {
            return 0;
        } else {
            return 1 + MathHelper.floor(14 * (double) storage.amount / (double) storage.getCapacity());
        }
    }
}
