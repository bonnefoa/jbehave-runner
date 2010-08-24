package org.jbehave.contrib.runner.reporter;

import org.jbehave.contrib.runner.description.DescriptionHelper;
import org.jbehave.scenario.definition.Blurb;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.definition.StoryDefinition;
import org.jbehave.scenario.reporters.ScenarioReporter;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Scenario reporter to use by Junit.
 */
public class JBehaveRunnerReporter implements ScenarioReporter {
    private RunNotifier notifier;

    private ArrayList<Description> currentScenariosList;


    public void beforeStory(StoryDefinition story, boolean embeddedStory) {
    }

    public void beforeStory(Blurb blurb) {
    }

    public void afterStory(boolean embeddedStory) {
    }

    public void afterStory() {
    }

    private Description currentScenario;

    public void beforeScenario(String title) {
        currentScenario = getScenarioDescription(title);
    }

    public void afterScenario() {
    }

    public void givenScenarios(List<String> givenScenarios) {
    }

    public void examplesTable(ExamplesTable table) {
    }

    public void beforeExamples(List<String> steps, ExamplesTable table) {
    }

    public void examplesTableRow(Map<String, String> tableRow) {
    }

    private Map<String, String> currentRow;

    public void example(Map<String, String> tableRow) {
        currentRow = tableRow;
    }

    public void afterExamples() {
    }

    private Description currentStep;

    public void successful(String step) {
        currentStep = getCurrentStep(step);
        notifier.fireTestStarted(currentStep);
        notifier.fireTestFinished(currentStep);
    }

    public void pending(String step) {
        currentStep = getCurrentStep(step);
        notifier.fireTestIgnored(currentStep);
    }

    public void notPerformed(String step) {
        currentStep = getCurrentStep(step);
        notifier.fireTestIgnored(currentStep);
    }

    public void failed(String step, Throwable e) {
        currentStep = getCurrentStep(step);
        notifier.fireTestStarted(currentStep);
        notifier.fireTestFailure(new Failure(currentStep, e));
        notifier.fireTestFinished(currentStep);
    }

    public void setNotifier(RunNotifier notifier) {
        this.notifier = notifier;
    }

    public void setDescription(Description description) {
        currentScenariosList = description.getChildren();
    }

    private Description getScenarioDescription(String scenarioTitle) {
        for (Description scenarioDescription : currentScenariosList) {
            if (scenarioDescription.getDisplayName().contains(scenarioTitle)) {
                return scenarioDescription;
            }
        }
        return null;
    }

    private Description getCurrentStep(String step) {
        // Test if it a step
        String stepDescriptionId = DescriptionHelper.getStepDescriptionId(step, currentScenario.getDisplayName());
        for (Description stepDescription : currentScenario.getChildren()) {
            if (stepDescription.getDisplayName().contains(stepDescriptionId)) {
                return stepDescription;
            }
        }
        // Test if it a table
        stepDescriptionId = DescriptionHelper.getStepDescriptionId(step, currentRow, currentRow.toString());
        for (Description rowDescription : currentScenario.getChildren()) {
            for (Description stepRowDescription : rowDescription.getChildren()) {
                if (stepRowDescription.getDisplayName().contains(stepDescriptionId)) {
                    return stepRowDescription;
                }
            }
        }
        return null;
    }
}
