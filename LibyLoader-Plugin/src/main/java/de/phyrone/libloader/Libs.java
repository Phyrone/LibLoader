package de.phyrone.libloader;

import org.eclipse.aether.artifact.Artifact;

import java.io.IOException;

public class Libs {
    private static LibLoader libLoader;

    public static void setLibLoader(LibLoader libLoader) {
        Libs.libLoader = libLoader;
    }

    public static void require(String coordinates) throws IOException {
        libLoader.require(coordinates);
    }

    public static void require(Artifact artifact) throws IOException {
        libLoader.require(artifact);
    }
}
