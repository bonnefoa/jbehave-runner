package org.jbehave.contrib.runner.suite;

import org.jbehave.contrib.runner.annotation.StoryRunner;
import org.jbehave.contrib.runner.configuration.JBehaveRunnerProperties;
import org.jbehave.contrib.runner.JBehaveStoryRunner;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Custom runner for junit. Embed all needed informations for running multiple stories.<br />
 * It will contains all runner for
 */
public class StorySuite extends Suite {
    /**
     * File filter for story file
     */
    private final FileFilter filter;

    /**
     * root directory for story files
     */
    private final File rootDirectory;

    /**
     * List of listStoryRunner.
     */
    private final ArrayList<Runner> listStoryRunner = new ArrayList<Runner>();
    /**
     * Filtering regexp when searching for stories
     */
    private final FrameworkMethod frameworkMethod;
    private final Properties properties;

    public StorySuite(Class<?> klass, final FrameworkMethod frameworkMethod) throws Exception {
        super(klass, Collections.<Runner>emptyList());
        properties = JBehaveRunnerProperties.getPropertiesForMethod(frameworkMethod);
        this.frameworkMethod = frameworkMethod;

        filter = new FileFilter() {
            public boolean accept(File pathname) {
                return (pathname.isDirectory() || pathname.getName().matches(properties.getProperty("story.filter")));
            }
        };

        this.rootDirectory = new File(ClassLoader.getSystemClassLoader().getResource("").toURI());
        if (!rootDirectory.exists())
            throw new IllegalArgumentException("Directory does not exist " + rootDirectory.getAbsolutePath());
        if (!rootDirectory.isDirectory()) throw new IllegalArgumentException("The given file is not a directory");

        listStoryRunner.addAll(getStoryRunnerList());
    }

    /**
     * Recursively add content of directory
     *
     * @param currentFile Current file
     * @param res         List where results are added
     */
    private void addDirectoryContent(File currentFile, List<File> res) {
        if (currentFile.isDirectory()) {
            for (File file : currentFile.listFiles(filter)) {
                addDirectoryContent(file, res);
            }
        } else {
            res.add(currentFile);
        }
    }

    /**
     * Get the list of story files in the root directory
     *
     * @return List of story file
     */
    public List<File> getStoryFiles() {
        ArrayList<File> list = new ArrayList<File>();
        addDirectoryContent(rootDirectory, list);
        return list;
    }

    /**
     * Create a StoryRunner for each story found.
     *
     * @return list of story runners
     * @throws Exception
     */
    private List<Runner> getStoryRunnerList() throws Exception {
        List<Runner> runnerList = new ArrayList<Runner>();
        for (File file : getStoryFiles()) {
            runnerList.add(createStoryRunner(getTestClass().getJavaClass(), file));
        }
        return runnerList;
    }

    @Override
    protected List<Runner> getChildren() {
        return listStoryRunner;
    }

    @Override
    public Description getDescription() {
        Description description = Description.createSuiteDescription("Suite for method : " + frameworkMethod.getName());
        for (Runner runner : getChildren()) {
            description.addChild(describeChild(runner));
        }
        return description;
    }

    /**
     * Create a story runner for the given story
     *
     * @param javaClass Test class
     * @param storyFile Story file
     * @return the story runner specific to the story file
     * @throws Exception
     */
    private Runner createStoryRunner(Class<?> javaClass, File storyFile) throws Exception {
        StoryRunner annotation = getTestClass().getJavaClass().getAnnotation(StoryRunner.class);
        Class aClass;
        if (annotation != null) {
            aClass = annotation.value();
        } else {
            aClass = JBehaveStoryRunner.class;
        }
        return (Runner) aClass.getConstructors()[0].newInstance(javaClass, storyFile, frameworkMethod, properties);
    }

}