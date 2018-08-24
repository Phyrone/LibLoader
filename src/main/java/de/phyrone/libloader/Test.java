package de.phyrone.libloader;

import java.io.File;

public class Test {
    public static void main(String[] args) {
        LibLoader libLoader = new LibLoader(new File("libs/"));
        libLoader.setMaxParalelDownloads(10);
    }
}
