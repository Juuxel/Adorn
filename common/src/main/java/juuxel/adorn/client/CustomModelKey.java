package juuxel.adorn.client;

import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.resources.Identifier;

public interface CustomModelKey<T> {
    Type<BlockStateModel> BLOCK_STATE_MODEL = new Type<>();

    Identifier id();

    final class Type<T> {
        private Type() {
        }
    }
}
