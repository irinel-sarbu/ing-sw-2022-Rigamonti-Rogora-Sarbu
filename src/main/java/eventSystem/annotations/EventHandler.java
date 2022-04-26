package eventSystem.annotations;

import java.lang.annotation.*;

/**
 * This annotation is used to identify methods that are called when an event occurs
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventHandler {
}
