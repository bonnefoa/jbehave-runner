package org.jbehave.contrib.finegrained;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;

public class JUnitReportingRunnerTest {

    @Mock
    private RunNotifier notifier;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void runUpExampleScenarioAndCheckNotifications() {
        JUnitReportingRunner runner = new JUnitReportingRunner(ExampleScenario.class);
        Description description = runner.getDescription();
        runner.run(notifier);
        verifyAllChildDescriptionsFired(description);
    }

    private void verifyAllChildDescriptionsFired(Description description) {
        verify(notifier).fireTestStarted(description);
        verify(notifier).fireTestFinished(description);
        for (Description child : description.getChildren()) {
            verifyAllChildDescriptionsFired(child);
        }
    }

}
