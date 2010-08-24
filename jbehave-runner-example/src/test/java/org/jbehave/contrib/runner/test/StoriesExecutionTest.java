package org.jbehave.contrib.runner.test;

import org.jbehave.contrib.runner.annotation.JBehaveConfiguration;
import org.jbehave.contrib.runner.annotation.StepClass;
import org.jbehave.contrib.runner.annotation.Stories;
import org.jbehave.contrib.runner.configuration.JBehaveRunnerConfiguration;
import org.jbehave.contrib.runner.steps.PizzaSteps;
import org.jbehave.contrib.runner.suite.JBehaveGlobalSuite;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Launch with custom runner
 */
@RunWith(JBehaveGlobalSuite.class)
@JBehaveConfiguration(JBehaveRunnerConfiguration.class)
public class StoriesExecutionTest {

//    @Test
//    @Stories(stories = "exampleTable.story")
//    @StepClass(AdditionSteps.class)
//    public void runTable() throws Throwable {
//    }

    @Test
    @Stories(stories = ".*Scenarios.story")
    @StepClass(PizzaSteps.class)
    public void runScenarios() throws Throwable {
    }
}
