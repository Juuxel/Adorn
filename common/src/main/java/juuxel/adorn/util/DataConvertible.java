package juuxel.adorn.util;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public interface DataConvertible {
    void readData(ValueInput view);
    void writeData(ValueOutput view);
}
