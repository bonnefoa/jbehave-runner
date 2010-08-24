package org.jbehave.contrib.runner.annotation;

import org.jbehave.contrib.runner.JBehaveStoryRunner;

import java.lang.annotation.*;

/**
 * Specifie the runner for indivual story
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface StoryRunner {

    /**
     * @return a Runner class (must have a constructor that takes a Class and a file to run)
     */
    Class<? extends org.junit.runner.Runner> value() default JBehaveStoryRunner.class;
}

