package de.phyrone.libloader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

public class LibLauncher {

    public static void main(String[] args) throws IOException {
        InputStream stream = LibLauncher.class.getClassLoader().getResourceAsStream("dependencies.properties");
        Properties properties = new Properties();
        properties.load(stream);
        stream.close();
        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[0], LibLauncher.class.getClassLoader());
            Class<?> clazz = Class.forName(properties.getProperty("main"), true, classLoader);
            start(clazz, args);
        } catch (ClassNotFoundException e) {
            System.err.println("Could no Found MainClass");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void start(Class<?> clazz, String[] args) {
        try {
            Object object = clazz.getDeclaredConstructor().newInstance();
            clazz.getDeclaredMethod("main", String[].class).invoke(object, (Object) args);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }


}
