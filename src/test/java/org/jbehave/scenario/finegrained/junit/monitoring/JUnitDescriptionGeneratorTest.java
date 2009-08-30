package org.jbehave.scenario.finegrained.junit.monitoring;

import javassist.CannotCompileException;
import org.hamcrest.BaseMatcher;
import static org.hamcrest.CoreMatchers.equalTo;
import org.hamcrest.Matcher;
import static org.jbehave.Ensure.ensureThat;
import org.jbehave.scenario.JUnitScenario;
import org.jbehave.scenario.definition.ScenarioDefinition;
import org.jbehave.scenario.definition.StoryDefinition;
import org.jbehave.scenario.steps.CandidateStep;
import org.jbehave.scenario.steps.Steps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import static org.mockito.Matchers.anyObject;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class JUnitDescriptionGeneratorTest {

    @Mock
    CandidateStep candidateStep;
    @Mock
    Steps steps;
    @Mock
    StoryDefinition story;

    private JUnitDescriptionGenerator generator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(steps.getSteps()).thenReturn(new CandidateStep[]{candidateStep});
        Mockito.when(candidateStep.matches((String) anyObject())).thenReturn(true);
        generator = new JUnitDescriptionGenerator();
    }

    @Test
    public void shouldGenerateDescriptionForTopLevelScenario() throws CannotCompileException {
        String scenarioTitle = "MyTitle";
        ScenarioDefinition scenario = new ScenarioDefinition(scenarioTitle);
        Description description = generator.createDescriptionFrom(scenario, steps);
        ensureThat(description, equalTo(Description.createTestDescription(ClassUtils.getOrCreateClass(scenarioTitle), scenarioTitle)));
    }

    @Test
    public void shouldGenerateDescriptionForStep() {
        ScenarioDefinition scenario = new ScenarioDefinition("MyTitle", "Step1");
        Description description = generator.createDescriptionFrom(scenario, steps);
        ensureThat(description.getChildren().size(), equalTo(1));
        ensureThat(description.getChildren().get(0), equalTo(Description.createTestDescription(steps.getClass(), "Step1 - Scenario: MyTitle")));
    }

    @Test
    public void shouldGenerateDescriptionForStory() {
        Mockito.when(story.getScenarios()).thenReturn(Collections.<ScenarioDefinition>emptyList());
        Description description = generator.createDescriptionFrom(story, steps, JUnitScenario.class);
        ensureThat(description, equalTo(Description.createSuiteDescription(JUnitScenario.class)));
    }

    @Test
    public void shouldGenerateDescriptionForScenarioChildOfStory() {
        String scenarioTitle = "MyTitle";
        Mockito.when(story.getScenarios()).thenReturn(Arrays.asList(new ScenarioDefinition(scenarioTitle)));
        Description description = generator.createDescriptionFrom(story, steps, JUnitScenario.class);
        ensureThat(description.getChildren().size(), equalTo(1));
        ensureThat(description.getChildren().get(0), equalTo(Description.createTestDescription(ClassUtils.getOrCreateClass(scenarioTitle), scenarioTitle)));
    }

    @Test
    public void shouldCopeWithSeeminglyDuplicateSteps() throws Exception {
        ScenarioDefinition scenario = new ScenarioDefinition("MyTitle", "Step1", "Step2", "Step3", "Step2", "Step2");
        Description description = generator.createDescriptionFrom(scenario, steps);
        ensureThat(description.getChildren().size(), equalTo(5));
        ensureThat(description, allChildrenHaveUniqueDisplayNames());
    }

    private Matcher<Description> allChildrenHaveUniqueDisplayNames() {
        return new BaseMatcher<Description>() {

            private Description junitDescription;

            public boolean matches(Object item) {
                junitDescription = (Description) item;
                Set<String> displayNames = new HashSet<String>();
                for (Description child : junitDescription.getChildren()) {
                    displayNames.add(child.getDisplayName());
                }
                return displayNames.size() == junitDescription.getChildren().size();
            }

            public void describeTo(org.hamcrest.Description description) {
                description.appendText("Children of description do not have unique display names");
                for (Description child : junitDescription.getChildren()) {
                    description.appendText(child.getDisplayName());
                }
            }

        };
    }
}