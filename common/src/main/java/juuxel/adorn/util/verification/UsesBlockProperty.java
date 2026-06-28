package juuxel.adorn.util.verification;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface UsesBlockProperty {
    Class<? extends Enum<?>>[] value();
}
