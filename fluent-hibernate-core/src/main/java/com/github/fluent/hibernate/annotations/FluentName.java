package com.github.fluent.hibernate.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 *
 * @author V.Ladynev
 */
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface FluentName {

    /**
     * Prefix for a name.
     */
    String prefix() default "";

}
