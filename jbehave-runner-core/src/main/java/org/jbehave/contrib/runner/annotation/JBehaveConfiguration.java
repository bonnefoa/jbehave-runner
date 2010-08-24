package org.jbehave.contrib.runner.annotation;

import java.lang.annotation.*;

/**
 * Annotation to specifie configuration
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface JBehaveConfiguration {

    /**
     * @return the configuration class
     */
    public abstract Class<?> value();
}
