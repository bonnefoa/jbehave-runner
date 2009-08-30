package org.jbehave.contrib.finegrained;

import org.jbehave.scenario.steps.Steps;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UseSteps {

    Class<? extends Steps> value();

}
