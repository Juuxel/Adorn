package juuxel.adorn.client;

import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

record CustomModelKeyImpl<T>(CustomModelKey.Type<T> type, StandaloneModelKey<T> backing, Identifier id) implements CustomModelKey<T> {
}
