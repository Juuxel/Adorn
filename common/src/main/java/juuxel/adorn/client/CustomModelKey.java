package juuxel.adorn.client;

import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.util.Identifier;

public interface CustomModelKey<T> {
    Type<BlockStateModel> BLOCK_STATE_MODEL = new Type<>();

    Identifier id();

    final class Type<T> {
        private Type() {
        }
    }
}
