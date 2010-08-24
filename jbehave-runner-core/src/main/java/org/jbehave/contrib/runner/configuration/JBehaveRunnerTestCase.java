package org.jbehave.contrib.runner.configuration;

import org.jbehave.contrib.runner.ScenarioFilter;
import org.jbehave.scenario.JUnitScenario;
import org.jbehave.scenario.PropertyBasedConfiguration;
import org.jbehave.scenario.reporters.ScenarioReporter;
import org.jbehave.scenario.steps.CandidateSteps;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * Test case for jbehave. Specialized in execution of a unique story.
 */
public class JBehaveRunnerTestCase extends JUnitScenario {
    protected File storyFile;
    private CandidateSteps[] candidateSteps;
    private PropertyBasedConfiguration configuration;
    private final Properties properties;

    public JBehaveRunnerTestCase(File storyFile, CandidateSteps[] candidateSteps, ScenarioReporter reporter, Properties properties, Class<?> configurationClass) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        this.storyFile = storyFile;
        this.candidateSteps = candidateSteps;
        this.properties = properties;
        Constructor<?>[] constructors = configurationClass.getConstructors();
        if (constructors.length != 1) {
            throw new IllegalArgumentException("Configuration class should have only one constructor with arguments {storyFile, outputFile, reporter} ");
        }
        configuration = (PropertyBasedConfiguration) constructors[0].newInstance(storyFile, getOutputFile(), reporter);
    }

    @Override
    public void runScenario() throws Throwable {
        File outputFile = getOutputFile();
        if (outputFile.exists()) {
            outputFile.delete();
        }
        ScenarioFilter scenarioRunner = new ScenarioFilter(properties);
        scenarioRunner.run(getClass(), configuration, candidateSteps);
    }


    /**
     * Get the output file for the xml results
     *
     * @return The output file for story results
     */
    public File getOutputFile() {
        File outputDirectory = new File(properties.getProperty("output.directory"));
        outputDirectory.mkdirs();
        File output = new File(outputDirectory.getAbsolutePath(), storyFile.getName() + "-result.html");
        return output;
    }

    @Override
    public String toString() {
        return storyFile.getName();
    }

}
