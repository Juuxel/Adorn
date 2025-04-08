package juuxel.adorn.data.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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
import net.minecraft.client.item.ItemAsset;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(targets = "net.minecraft.client.data.ModelProvider$ItemAssets")
abstract class ModelProviderItemAssetsMixin {
    @WrapOperation(method = "method_65470", at = @At(value = "INVOKE", target = "Ljava/util/Map;containsKey(Ljava/lang/Object;)Z", ordinal = 1))
    private boolean dontGenerateUnnecessaryItemAssets(Map<Identifier, ItemAsset> itemAssets, Object key, Operation<Boolean> original, Item item) {
        // if the key is an item, resolve it
        if (key instanceof Item i) {
            key = Registries.ITEM.getId(i);
        }

        Block block = ((BlockItem) item).getBlock();
        return itemAssets.containsKey(key) || block instanceof BenchBlock
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
