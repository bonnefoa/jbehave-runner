package org.jbehave.contrib.runner.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Stories to test
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Stories {

    /**
     * Optionally specify a regexp to launch stories
     */
    String stories() default ".*story";
}
