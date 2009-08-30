package org.jbehave.contrib.finegrained;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import org.jbehave.scenario.steps.Steps;

/**
 * Utils methodes for class
 */
public class ClassUtils {

    static Class getOrCreateClass(String classTitle) {
        Class clazz = Steps.class;
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass evalClass;
            try {
                return ClassLoader.getSystemClassLoader().loadClass(classTitle);
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found, creating " + classTitle);
                evalClass = pool.makeClass(classTitle);
            }
            clazz = evalClass.toClass();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return clazz;
    }
}
