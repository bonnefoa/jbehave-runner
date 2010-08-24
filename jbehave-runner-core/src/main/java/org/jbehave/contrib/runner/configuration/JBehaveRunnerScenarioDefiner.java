package org.jbehave.contrib.runner.configuration;

import org.apache.commons.io.FileUtils;
import org.jbehave.scenario.RunnableScenario;
import org.jbehave.scenario.definition.StoryDefinition;
import org.jbehave.scenario.errors.ScenarioNotFoundException;
import org.jbehave.scenario.parser.PatternScenarioParser;
import org.jbehave.scenario.parser.ScenarioDefiner;
import org.jbehave.scenario.parser.ScenarioParser;

import java.io.File;
import java.io.IOException;

/**
 * Scenario definer for ea. Allow to read a unique story File.
 */
public class JBehaveRunnerScenarioDefiner implements ScenarioDefiner {

    private final ScenarioParser parser;
    private String storyPath;

    public JBehaveRunnerScenarioDefiner(String pathScenario) {
        this.storyPath = pathScenario;
        parser = new PatternScenarioParser();
    }

    public StoryDefinition loadScenarioDefinitionsFor(Class<? extends RunnableScenario> scenarioClass) {
        try {
            return parser.defineStoryFrom(FileUtils.readFileToString(new File(storyPath)), storyPath);
        } catch (IOException e) {
            throw new ScenarioNotFoundException("Path '" + scenarioClass + "' could not be found ");
        }
    }

    public StoryDefinition loadScenarioDefinitionsFor(String storyPath) {
        try {
            return parser.defineStoryFrom(FileUtils.readFileToString(new File(storyPath)), storyPath);
        } catch (IOException e) {
            throw new ScenarioNotFoundException("Path '" + storyPath + "' could not be found ");
        }
    }

}
