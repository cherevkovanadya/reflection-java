package org.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

public class Injector {
    public Object inject(Object object) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("dependencies.properties"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File dependencies.properties not found", e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading dependencies.properties", e);
        }

        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(AutoInjectable.class)) {
                Class<?> fieldType = field.getType();
                String implementationClassName = properties.getProperty(fieldType.getName());
                if (implementationClassName != null) {
                    try {
                        Class<?> implementationClass = Class.forName(implementationClassName);
                        Object implementationInstance = implementationClass.getDeclaredConstructor().newInstance();
                        field.setAccessible(true);
                        field.set(object, implementationInstance);
                    } catch (Exception e) {
                        throw new RuntimeException("Error creating instance for " + fieldType, e);
                    }
                } else {
                    throw new RuntimeException("Implementation not found for " + fieldType);
                }
            }
        }
        return object;
    }
}
