package org.jbehave.contrib.runner.steps;

import org.hamcrest.core.Is;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Named;
import org.jbehave.scenario.annotations.Then;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Steps for addition
 */
public class AdditionSteps {
    private double valeur;


    @Given("une valeur initiale de <value>")
    public void anInitialValue(@Named("value") double valeur) {

        this.valeur = valeur;
    }

    @Then("le r�sultat doit �tre <result>")
    public void valueVerification(@Named("result") double result) {
        assertThat(valeur, Is.is(result));
    }
}
