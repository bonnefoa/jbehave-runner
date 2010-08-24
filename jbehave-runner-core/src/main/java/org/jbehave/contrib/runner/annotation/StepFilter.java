package org.jbehave.contrib.runner.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Filter on steps
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface StepFilter {

    /**
     * Optionally specify a regexp to filter steps
     */
    public abstract String steps() default ".*";
}