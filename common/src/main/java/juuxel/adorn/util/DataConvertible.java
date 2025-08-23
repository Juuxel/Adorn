package juuxel.adorn.util;

import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;

public interface DataConvertible {
    void readData(ReadView view);
    void writeData(WriteView view);
}
