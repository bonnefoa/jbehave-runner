package org.jbehave.contrib.runner.configuration;

import org.jbehave.contrib.runner.annotation.StepFilter;
import org.jbehave.contrib.runner.annotation.Stories;
import org.junit.runners.model.FrameworkMethod;

import java.io.IOException;
import java.util.Properties;

/**
 * Properties configuration for jbehave
 */
public class JBehaveRunnerProperties {

    public static void loadDefaults(Properties properties) {
        try {
            properties.load(JBehaveRunnerProperties.class.getResourceAsStream("default.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Properties getPropertiesForMethod(FrameworkMethod frameworkMethod) {
        Properties properties = new Properties();
        loadDefaults(properties);
        Stories annotation = frameworkMethod.getAnnotation(Stories.class);
        if (!annotation.stories().equals(properties.get("story.filter"))) {
            properties.setProperty("story.filter", annotation.stories());
        }
        StepFilter stepFilter = frameworkMethod.getAnnotation(StepFilter.class);
        if (stepFilter != null) {
            properties.setProperty("step.filter", stepFilter.steps());
        }
        return properties;
    }
}
