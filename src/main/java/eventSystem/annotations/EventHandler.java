package eventSystem.annotations;

import java.lang.annotation.*;

/**
 * This annotation is used to identify methods that are called when an event occurs. You can set a topic to restrict access to handler. Default is '*'
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventHandler {
    String value() default "*";
}
