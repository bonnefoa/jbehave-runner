package org.jbehave.contrib.runner.configuration;

import org.jbehave.scenario.PropertyBasedConfiguration;
import org.jbehave.scenario.parser.ScenarioDefiner;
import org.jbehave.scenario.reporters.DelegatingScenarioReporter;
import org.jbehave.scenario.reporters.HtmlPrintStreamScenarioReporter;
import org.jbehave.scenario.reporters.ScenarioReporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * JBehave configuration of story, outputfile and reporting
 */
public class JBehaveRunnerConfiguration extends PropertyBasedConfiguration {
    private final File story;
    private final File outputFile;
    private final ScenarioReporter reporter;

    public JBehaveRunnerConfiguration(File story, File outputFile, ScenarioReporter reporter) {
        this.story = story;
        this.outputFile = outputFile;
        this.reporter = reporter;
    }

    @Override
    public ScenarioDefiner forDefiningScenarios() {
        return new JBehaveRunnerScenarioDefiner(story.getAbsolutePath());
    }

    @Override
    public ScenarioReporter forReportingScenarios() {
        try {
            HtmlPrintStreamScenarioReporter htmlPrintStreamScenarioReporter = new HtmlPrintStreamScenarioReporter(
                    new PrintStream(new FileOutputStream(outputFile, true))
            );
            if (reporter != null) {
                return new DelegatingScenarioReporter(
                        htmlPrintStreamScenarioReporter, reporter);
            } else {
                return new DelegatingScenarioReporter(
                        htmlPrintStreamScenarioReporter);
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("OutputFile invalide", e);
        }
    }
}
