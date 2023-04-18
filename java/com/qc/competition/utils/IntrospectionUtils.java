package com.qc.competition.utils;

import java.util.ArrayList;
import java.util.List;

public class IntrospectionUtils {
    public static List<Class> getHierarchyClasses(Object o) {
        Class currentClass = o.getClass();
        List<Class> classes = getHierarchy(currentClass);
        return classes;
    }

    public static List<Class> getHierarchy(Class aClass) {
        List<Class> classes = new ArrayList<>();
        do {
            classes.add(aClass);
            aClass = aClass.getSuperclass();
        } while (aClass.getSuperclass() != null);
        return classes;
    }

    public static List<String> getHierarchyClassSimpleName(Class aClass) {
        List<Class> classes = getHierarchy(aClass);
        List<String> classesSimpleName = new ArrayList<>();
        for (Class allClass : classes) {
            classesSimpleName.add(allClass.getSimpleName());
        }
        return classesSimpleName;
    }

    public static List<String> getHierarchyClassName(Class aClass) {
        List<Class> classes = getHierarchy(aClass);
        List<String> classesSimpleName = new ArrayList<>();
        for (Class allClass : classes) {
            classesSimpleName.add(allClass.getName());
        }
        return classesSimpleName;
    }
}
