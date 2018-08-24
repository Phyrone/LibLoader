package de.phyrone.libloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

public class LibLauncher {

    private Class<Runnable> runClass;

    private LibLauncher(String name) throws ClassNotFoundException {
        URLClassLoader classLoader = new URLClassLoader(new URL[0], this.getClass().getClassLoader());
        try {
            runClass = (Class<Runnable>) Class.forName(name, true, classLoader);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        InputStream stream = LibLauncher.class.getClassLoader().getResourceAsStream("dependencies.properties");
        Properties properties = new Properties();
        properties.load(stream);
        stream.close();
        try {
            new LibLauncher(properties.getProperty("main")).start();
        } catch (ClassNotFoundException e) {
            System.err.println("Could no Found MainClass");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void start() {
        try {
            runClass.newInstance().run();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
