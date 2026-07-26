package juuxel.adorn.block.entity;

import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.menu.DrawerMenu;
import juuxel.adorn.util.AdornUtil;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.core.BlockPos;

public final class DrawerBlockEntity extends SimpleContainerBlockEntity {
    public DrawerBlockEntity(BlockPos pos, BlockState state) {
        super(AdornBlockEntities.DRAWER.get(), pos, state, 15);
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory inv) {
        return new DrawerMenu(syncId, inv, this, AdornUtil.menuContextOf(this));
    }
}
