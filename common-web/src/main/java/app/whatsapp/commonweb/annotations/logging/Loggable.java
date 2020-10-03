package app.whatsapp.commonweb.annotations.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Loggable {

    enum Context {ARGS, RETURN_VALUE}

    Context[] contexts() default {};

}
