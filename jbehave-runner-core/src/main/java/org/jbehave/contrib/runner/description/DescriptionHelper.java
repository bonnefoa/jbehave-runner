package org.jbehave.contrib.runner.description;

import org.jbehave.scenario.steps.CandidateStep;
import org.junit.runner.Description;
import org.junit.runners.model.TestClass;

import java.util.Map;

/**
 * Helper to generate test description
 */
public class DescriptionHelper {
    public static Description createStepDescription(String steps, String displayName) {
        return Description.createSuiteDescription(getStepDescriptionId(steps, displayName));
    }

    public static Description createStepDescription(String currentStep, Map<String, String> currentRow, String displayName) {
        return Description.createSuiteDescription(getStepDescriptionId(currentStep, currentRow, displayName));
    }

    public static String getStepDescriptionId(String steps, String displayName) {
        return steps + " " + displayName.substring(0, 9);
    }

    public static String getStepDescriptionId(String currentStep, Map<String, String> currentRow, String displayName) {
        return (filterStep(currentStep, currentRow) + " " + displayName).replaceAll(CandidateStep.PARAMETER_VALUE_START, "").replaceAll(CandidateStep.PARAMETER_VALUE_END, "");
    }

    private static String filterStep(String currentStep, Map<String, String> currentRow) {
        String result = currentStep;
        for (Map.Entry<String, String> rowEntrySet : currentRow.entrySet()) {
            result = result.replaceAll('<' + rowEntrySet.getKey() + '>', rowEntrySet.getValue());
        }
        return result;
    }
}
