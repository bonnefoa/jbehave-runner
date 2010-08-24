package org.jbehave.contrib.runner.annotation;

import java.lang.annotation.*;

/**
 * Specifie the step class to use
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
public @interface StepClass {

    /**
     * @return a class defining step
     */
    public abstract Class<?> value();
}