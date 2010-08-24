package org.jbehave.contrib.runner.steps;

import org.hamcrest.core.Is;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.*;

import static org.junit.Assert.assertThat;

/**
 * Steps for pizza
 */
public class PizzaSteps {

    private StatePizza statePizza;

    private int pizzaBought;

    @Given("l'utilisateur aime la pizza")
    public void userLikePizza() {
        statePizza = StatePizza.LIKE;
    }

    @Given("l'utilisateur n'aime pas la pizza")
    public void userDontLikePizza() {
        statePizza = StatePizza.DONT_LIKE;
    }

    @Given("l'utilisateur adore la pizza")
    public void userLovePizza() {
        statePizza = StatePizza.LOVE;
    }

    @When("l'utilisateur passe devant une pizzeria")
    public void goInFrontOfPizzaria() {
        switch (statePizza) {
            case DONT_LIKE:
                pizzaBought = 0;
                break;
            case LIKE:
                pizzaBought = 1;
                break;
            case LOVE:
                pizzaBought = 2;
                break;
        }
    }

    @Then("l'utilisateur achete $number pizza")
    public void checkNumberPizza(int number) {
        assertThat(pizzaBought, Is.is(number));
    }

    public static CandidateSteps[] getCandidateSteps() {
        StepsConfiguration configuration = new StepsConfiguration();
        StepMonitor monitor = new SilentStepMonitor();
        configuration.useMonitor(monitor);
        return new StepsFactory(configuration).createCandidateSteps(new PizzaSteps());
    }
}
