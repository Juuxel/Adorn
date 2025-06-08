package juuxel.adorn.platform.neo.client;

import juuxel.adorn.client.CustomModelKey;
import net.minecraft.util.Identifier;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

record CustomModelKeyImpl<T>(CustomModelKey.Type<T> type, StandaloneModelKey<T> backing) implements CustomModelKey<T> {
    @Override
    public Identifier id() {
        return backing.getModelId();
    }
}
