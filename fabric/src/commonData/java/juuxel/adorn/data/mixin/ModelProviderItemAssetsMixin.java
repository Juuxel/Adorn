package juuxel.adorn.data.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import juuxel.adorn.block.BenchBlock;
import juuxel.adorn.block.CandlelitLanternBlock;
import juuxel.adorn.block.ChairBlock;
import juuxel.adorn.block.CoffeeTableBlock;
import juuxel.adorn.block.DrawerBlock;
import juuxel.adorn.block.KitchenCounterBlock;
import juuxel.adorn.block.KitchenCupboardBlock;
import juuxel.adorn.block.KitchenSinkBlock;
import juuxel.adorn.block.PlatformBlock;
import juuxel.adorn.block.PostBlock;
import juuxel.adorn.block.ShelfBlock;
import juuxel.adorn.block.SofaBlock;
import juuxel.adorn.block.StepBlock;
import juuxel.adorn.block.TableBlock;
import juuxel.adorn.block.TableLampBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.client.data.ModelProvider$ItemAssets")
abstract class ModelProviderItemAssetsMixin {
    @ModifyExpressionValue(method = "method_65470", at = @At(value = "INVOKE", target = "Ljava/util/Map;containsKey(Ljava/lang/Object;)Z", ordinal = 1))
    private boolean dontGenerateUnnecessaryItemAssets(boolean disable, Item item) {
        Block block = ((BlockItem) item).getBlock();
        return disable || block instanceof BenchBlock
            || block instanceof CandlelitLanternBlock
            || block instanceof ChairBlock
            || block instanceof CoffeeTableBlock
            || block instanceof DrawerBlock
            || block instanceof KitchenCounterBlock
            || block instanceof KitchenCupboardBlock
            || block instanceof KitchenSinkBlock
            || block instanceof PlatformBlock
            || block instanceof PostBlock
            || block instanceof ShelfBlock
            || block instanceof SofaBlock
            || block instanceof StepBlock
            || block instanceof TableBlock
            || block instanceof TableLampBlock;
    }
}
