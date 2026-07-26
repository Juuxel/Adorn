package juuxel.adorn.block.entity;

import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.menu.KitchenCupboardMenu;
import juuxel.adorn.util.AdornUtil;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.core.BlockPos;

public final class KitchenCupboardBlockEntity extends SimpleContainerBlockEntity {
    public KitchenCupboardBlockEntity(BlockPos pos, BlockState state) {
        super(AdornBlockEntities.KITCHEN_CUPBOARD.get(), pos, state, 15);
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory inv) {
        return new KitchenCupboardMenu(syncId, inv, this, AdornUtil.menuContextOf(this));
    }
}
