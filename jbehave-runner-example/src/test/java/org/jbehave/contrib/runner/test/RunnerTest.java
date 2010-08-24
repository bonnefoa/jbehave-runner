package org.jbehave.contrib.runner.test;

import org.jbehave.contrib.runner.annotation.Stories;
import org.jbehave.contrib.runner.configuration.JBehaveRunnerConfiguration;
import org.jbehave.contrib.runner.configuration.JBehaveRunnerProperties;
import org.jbehave.contrib.runner.configuration.JBehaveRunnerTestCase;
import org.jbehave.contrib.runner.steps.PizzaSteps;
import org.jbehave.contrib.runner.suite.StorySuite;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.hamcrest.core.AnyOf;
import org.hamcrest.core.Is;
import org.hamcrest.number.IsGreaterThan;
import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.mockito.Mockito;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertThat;

/**
 * Test for scenarios executors
 */
public class RunnerTest {

    private JBehaveRunnerTestCase ExampleTestCase;
    private StorySuite testSuite;

    private URL exampleScenarios1 = getClass().getResource("exampleScenarios.story");
    private URL badScenarios = getClass().getResource("badScenarios.story");
    private URL pendingScenarios = getClass().getResource("pendingScenarios.story");

    @Test
    public void testListFiles() throws Exception {
        FrameworkMethod method = Mockito.mock(FrameworkMethod.class);
        Stories stories = Mockito.mock(Stories.class);
        Mockito.when(stories.stories()).thenReturn(".*\\.story");
        Mockito.when(method.getAnnotation(Stories.class)).thenReturn(stories);
        testSuite = new StorySuite(getClass(), method);
        List<File> storyFiles = testSuite.getStoryFiles();
        assertThat(storyFiles.size(), new IsGreaterThan<Integer>(2));
    }

    @Test
    public void testExecutionScenarioAndWriteOutput() throws Throwable {
        File story = new File(exampleScenarios1.toURI());
        assertThat(story.exists(), Is.is(true));
        Properties properties = new Properties();
        JBehaveRunnerProperties.loadDefaults(properties);
        ExampleTestCase = new JBehaveRunnerTestCase(story, PizzaSteps.getCandidateSteps(), null, properties, JBehaveRunnerConfiguration.class);
        ExampleTestCase.runScenario();

        XpathEngine engine = XMLUnit.newXpathEngine();
        FileInputStream stream = new FileInputStream(ExampleTestCase.getOutputFile());
        Document document = XMLUnit.buildControlDocument(new InputSource(stream));
        NodeList matchingNodes = engine.getMatchingNodes("*//step/@outcome", document);

        for (int i = 0; i < matchingNodes.getLength(); i++) {
            String stepStatus = matchingNodes.item(i).getNodeValue();
            assertThat(stepStatus, Is.is("ok"));
        }
        stream.close();
    }

    @Test
    public void pendingScenarioShouldBeWrittenInOutput() throws Throwable {
        File story = new File(pendingScenarios.toURI());
        Properties properties = new Properties();
        JBehaveRunnerProperties.loadDefaults(properties);
        ExampleTestCase = new JBehaveRunnerTestCase(story, PizzaSteps.getCandidateSteps(), null, properties, JBehaveRunnerConfiguration.class);
        ExampleTestCase.runScenario();

        XpathEngine engine = XMLUnit.newXpathEngine();
        FileInputStream stream = new FileInputStream(ExampleTestCase.getOutputFile());
        Document document = XMLUnit.buildControlDocument(new InputSource(stream));
        NodeList matchingNodes = engine.getMatchingNodes("*//step", document);

        for (int i = 0; i < matchingNodes.getLength(); i++) {
            Node node = matchingNodes.item(i);
            if (node.getTextContent().contains("--")) {
                assertThat(node.getAttributes().getNamedItem("outcome").getNodeValue(),
                        IsEqualIgnoringCase.equalToIgnoringCase("pending"));
            } else {
                assertThat(node.getAttributes().getNamedItem("outcome").getNodeValue(),
                        AnyOf.anyOf(IsEqualIgnoringCase.equalToIgnoringCase("ignored"),
                                IsEqualIgnoringCase.equalToIgnoringCase("ok")));
            }
        }
    }
}
