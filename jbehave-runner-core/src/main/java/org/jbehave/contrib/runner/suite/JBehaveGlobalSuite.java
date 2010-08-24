package org.jbehave.contrib.runner.suite;

import org.jbehave.contrib.runner.annotation.Stories;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Custom runner for junit. Manage the suite and the execution of the tree runner.<br />
 * It will create a StorySuite for each test function in the test class.
 */
public class JBehaveGlobalSuite extends Suite {

    /**
     * List of methodRunners. A runner is an execution unit for junit.
     */
    private List<Runner> methodRunners = new ArrayList<Runner>();
    private Class<?> klass;

    public JBehaveGlobalSuite(Class<?> klass) throws Exception {
        super(klass, Collections.<Runner>emptyList());
        this.klass = klass;
        methodRunners = getMethodRunner();
    }

    /**
     * Get the list of story runner by applying the given filter
     *
     * @param filter method filter specific to junit
     * @return list of runner passing the filter
     * @throws Exception
     */
    private List<Runner> getMethodRunnerFiltered(Filter filter) throws Exception {
        List<Runner> runnerList = new ArrayList<Runner>();
        List<FrameworkMethod> frameworkMethods = getTestClass().getAnnotatedMethods(Stories.class);
        for (FrameworkMethod frameworkMethod : frameworkMethods) {
            if (filter.describe().contains(frameworkMethod.getName())) {
                runnerList.add(new StorySuite(getTestClass().getJavaClass(), frameworkMethod));
            }
        }
        return runnerList;
    }

    /**
     * Get all the story runner from the test classe.
     *
     * @return List of story runner to execute
     * @throws Exception
     */
    private List<Runner> getMethodRunner() throws Exception {
        List<Runner> runnerList = new ArrayList<Runner>();
        List<FrameworkMethod> frameworkMethods = getTestClass().getAnnotatedMethods(Stories.class);
        for (FrameworkMethod frameworkMethod : frameworkMethods) {
            runnerList.add(new StorySuite(getTestClass().getJavaClass(), frameworkMethod));
        }
        return runnerList;
    }

    @Override
    public void filter(Filter filter) throws NoTestsRemainException {
        try {
            methodRunners = getMethodRunnerFiltered(filter);
        } catch (Exception e) {
            throw new NoTestsRemainException();
        }
        if (methodRunners.size() == 0) {
            throw new NoTestsRemainException();
        }
    }

    @Override
    protected List<Runner> getChildren() {
        return methodRunners;
    }

    @Override
    protected Description describeChild(Runner child) {
        return child.getDescription();
    }

    @Override
    public Description getDescription() {
        Description description = Description.createSuiteDescription(klass);
        for (Runner runner : getChildren()) {
            description.addChild(describeChild(runner));
        }
        return description;
    }

}