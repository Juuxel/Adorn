package juuxel.adorn.block.entity;

import juuxel.adorn.block.AdornBlockEntities;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.BlockPos;

public final class ShelfBlockEntity extends BaseContainerBlockEntity {
    public ShelfBlockEntity(BlockPos pos, BlockState state) {
        super(AdornBlockEntities.SHELF.get(), pos, state, 2);
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory inv) {
        // No menus for shelves
        return null;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }
}
