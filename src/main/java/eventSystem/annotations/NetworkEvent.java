package eventSystem.annotations;

import java.lang.annotation.*;

/**
 * This annotation is used to identify event source
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NetworkEvent {
}
