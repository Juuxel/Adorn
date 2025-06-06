package juuxel.adorn.platform.neo.client;

import juuxel.adorn.client.CustomModelKey;
import net.minecraft.client.util.ModelIdentifier;

record CustomModelKeyImpl(ModelIdentifier modelId) implements CustomModelKey {
}
