package de.phyrone.libloader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public class LibLauncher {

    private static final String REPOSITORY_IDENTIFIER = "repository=";

    private static final String ARTIFACT_IDENTIFIER = "artifact=";

    private static final String MAINCLASS_IDENTIFIER = "main=";
    private static final Pattern NEWLINE = Pattern.compile("\r?\n");

    public static void main(String[] args) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (InputStream stream = LibLauncher.class.getClassLoader().getResourceAsStream("dependencies.properties")) {
            while (stream.available() > 0) {
                sb.appendCodePoint(stream.read());
            }
        }
        String[] lines = NEWLINE.split(sb.toString());
        Set<String> artifacts = new HashSet<>();
        Set<String> repositories = new HashSet<>();
        AtomicReference<String> mainClass = new AtomicReference<>();
        Arrays.stream(lines).forEach(line -> {
            if (line.startsWith(MAINCLASS_IDENTIFIER)) {
                mainClass.set(line.replaceFirst(MAINCLASS_IDENTIFIER, ""));
            } else if (line.startsWith(ARTIFACT_IDENTIFIER)) {
                artifacts.add(line.replaceFirst(ARTIFACT_IDENTIFIER, ""));
            } else if (line.startsWith(REPOSITORY_IDENTIFIER)) {
                repositories.add(line.replaceFirst(REPOSITORY_IDENTIFIER, ""));
            }
        });

        LibLoader loader = new LibLoader();
        repositories.forEach(loader::addRepository);
        artifacts.forEach(coordinates -> {
            try {
                loader.require(coordinates);
            } catch (IOException e) {
                System.out.println("Could not load" + coordinates);
                e.printStackTrace();
            }
        });

        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[0], LibLauncher.class.getClassLoader());
            Class<?> clazz = Class.forName(mainClass.get(), true, classLoader);
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
