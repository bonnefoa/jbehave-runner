package org.jbehave.contrib.runner;

import org.jbehave.contrib.runner.annotation.JBehaveConfiguration;
import org.jbehave.contrib.runner.annotation.StepClass;
import org.jbehave.contrib.runner.configuration.JBehaveRunnerTestCase;
import org.jbehave.contrib.runner.description.DescriptionHelper;
import org.jbehave.contrib.runner.reporter.JBehaveRunnerReporter;
import org.apache.commons.io.FileUtils;
import org.jbehave.scenario.RunnableScenario;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.definition.ScenarioDefinition;
import org.jbehave.scenario.definition.StoryDefinition;
import org.jbehave.scenario.i18n.I18nKeyWords;
import org.jbehave.scenario.parser.PatternScenarioParser;
import org.jbehave.scenario.steps.*;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.io.File;
import java.util.*;

/**
 * runner for junit Suite. This runner is dedicated to a unique story file.
 * It is used by suite to create a list of runner for each stories.
 */
public class JBehaveStoryRunner extends BlockJUnit4ClassRunner {

    private final File storyFile;
    private FrameworkMethod frameworkMethod;
    private StoryDefinition storyDefinition;
    private JBehaveRunnerReporter jBehaveReporter;
    private Properties properties;

    public JBehaveStoryRunner(Class<?> type, File storyFile, FrameworkMethod frameworkMethod, Properties properties) throws Exception {
        super(type);

        this.storyFile = storyFile;
        this.frameworkMethod = frameworkMethod;
        this.properties = properties;
        PatternScenarioParser patternScenarioParser = new PatternScenarioParser(new I18nKeyWords());
        storyDefinition = patternScenarioParser.defineStoryFrom(FileUtils.readFileToString(storyFile));
        jBehaveReporter = new JBehaveRunnerReporter();
    }

    public Object createTest() throws Exception {
        return getTestClass().getOnlyConstructor().newInstance();
    }

    private RunnableScenario createRunner() throws Exception {
        Class<?> stepClass = frameworkMethod.getAnnotation(StepClass.class).value();
        Class<?> configurationClass = getTestClass().getJavaClass().getAnnotation(JBehaveConfiguration.class).value();
        Object stepInstance = stepClass.getConstructors()[0].newInstance();
        StepsConfiguration configuration = new StepsConfiguration();
        StepMonitor monitor = new SilentStepMonitor();
        configuration.useMonitor(monitor);
        CandidateSteps[] candidateSteps = new StepsFactory(configuration).createCandidateSteps(stepInstance);
        return new JBehaveRunnerTestCase(storyFile, candidateSteps, jBehaveReporter, properties, configurationClass);
    }

    @Override
    protected void validateConstructor(List<Throwable> errors) {
        validateOnlyOneConstructor(errors);
    }

    @Override
    public void run(RunNotifier notifier) {
        EachTestNotifier testNotifier = new EachTestNotifier(notifier, getDescription());
        jBehaveReporter.setNotifier(notifier);
        jBehaveReporter.setDescription(getDescription());
        try {
            Statement statement = classBlock(notifier);
            statement.evaluate();
        } catch (AssumptionViolatedException e) {
            testNotifier.fireTestIgnored();
        } catch (StoppedByUserException e) {
            throw e;
        } catch (Throwable e) {
            testNotifier.addFailure(e);
        }
    }

    @Override
    protected List<FrameworkMethod> getChildren() {
        return Collections.singletonList(frameworkMethod);
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        EachTestNotifier testNotifier = new EachTestNotifier(notifier, getDescription());
        try {
            methodBlock(method).evaluate();
            createRunner().runScenario();
        } catch (AssumptionViolatedException e) {
            testNotifier.fireTestIgnored();
        } catch (StoppedByUserException e) {
            throw e;
        } catch (Throwable e) {
            testNotifier.addFailure(e);
        }
    }

    @Override
    public Description getDescription() {
        String storyParent = storyDefinition.getBlurb().asString().replaceAll("Story: ", "story.");
        Description storyDescription = Description.createSuiteDescription(storyParent);

        List<ScenarioDefinition> listScenarios = storyDefinition.getScenarios();
        for (ScenarioDefinition scenarioDefinition : listScenarios) {
            // Each scenario
            String scenarioParent = storyParent + "." + scenarioDefinition.getTitle();
            Description scenarioDescription = Description.createSuiteDescription(scenarioParent);

            for (Description description : getStepsChild(scenarioDefinition, scenarioDescription.getDisplayName(), scenarioParent)) {
                scenarioDescription.addChild(description);
            }
            storyDescription.addChild(scenarioDescription);
        }
        return storyDescription;
    }

    /**
     * Get list of description for each step if it is a simple scenario.
     * Get list of description for each row and for each steps if it is a example table.
     *
     * @param scenarioDefinition Scenario definition containing steps and table
     * @param displayName        Identification of the scenario
     * @param parent
     * @return List of description corresponding of children for the scenario
     */
    private List<Description> getStepsChild(ScenarioDefinition scenarioDefinition, String displayName, String parent) {
        List<Description> resultDescription = new ArrayList<Description>();
        ExamplesTable examplesTable = scenarioDefinition.getTable();
        // If it is a table
        if (examplesTable.getRowCount() > 0) {
            for (Map<String, String> currentRow : examplesTable.getRows()) {
                resultDescription.add(createSuiteForRow(currentRow, scenarioDefinition, parent));
            }
            return resultDescription;
        }

        // If it is a normal step
        for (String steps : scenarioDefinition.getSteps()) {
            resultDescription.add(DescriptionHelper.createStepDescription(steps, displayName));
        }
        return resultDescription;
    }

    /**
     * Create the description for the row test
     *
     * @param currentRow         Row to test
     * @param scenarioDefinition Scenario definition containing steps
     * @param parent
     * @return Corresponding description
     */
    private Description createSuiteForRow(Map<String, String> currentRow, ScenarioDefinition scenarioDefinition, String parent) {
        String rowParent = parent + "." + currentRow.toString();
        Description rowDescription = Description.createSuiteDescription(rowParent);
        for (String currentStep : scenarioDefinition.getSteps()) {
            rowDescription.addChild(DescriptionHelper.createStepDescription(currentStep, currentRow, currentRow.toString()));
        }
        return rowDescription;
    }

}
