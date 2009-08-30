package org.jbehave.contrib.finegrained;

import static org.hamcrest.CoreMatchers.equalTo;
import org.jbehave.Ensure;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

@SuppressWarnings("unused")
public class ExampleSteps extends Steps {
    Integer x;

    @Given("a variable x with value $value")
    public void aVariableXWithValue(int value) {
        x = value;
    }

    @When("I multiply x by $value")
    public void iMultiplyXBy(int value) {
        x = value * x;
    }

    @Then("x should equal $value")
    public void xShouldEqual(int value) {
        Ensure.ensureThat(x, equalTo(value));
    }

}
