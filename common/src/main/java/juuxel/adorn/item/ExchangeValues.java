package juuxel.adorn.item;

import java.math.BigDecimal;
import java.math.MathContext;

public final class ExchangeValues {
    public static final float STICK = 0.002f;
    public static final float SLAB = 0.02f; // wooden slabs are 0.03
    public static final float CHEST = 0.04f;
    public static final float BUCKET = 0.09f;
    public static final float GLASS_PANE = 0.05f;

    public static float round(float f) {
        return BigDecimal.valueOf(f)
            .multiply(BigDecimal.TWO)
            .round(new MathContext(1))
            .multiply(BigDecimal.valueOf(0.5))
            .floatValue();
    }
}
