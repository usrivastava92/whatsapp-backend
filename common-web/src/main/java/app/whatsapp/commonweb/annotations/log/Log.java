package app.whatsapp.commonweb.annotations.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface Log {

    enum Context {ARGS, RETURN_VALUE}

    Context[] contexts() default {};

}
