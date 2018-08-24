package de.phyrone.libloader;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class LibLoader extends MavenDownloader {
    ClassLoader parentClassloader = new URLClassLoader(new URL[0], this.getClass().getClassLoader());

    private File repoFile;

    public LibLoader(File localRepo) {
        if (localRepo.exists() || localRepo.mkdirs()) {
            if (localRepo.isDirectory()) {
                repoFile = localRepo;
            } else {
                repoFile = localRepo.getParentFile();
            }
        }
    }

    public void shutdown() {
        super.shutdown();
        parentClassloader = null;
        System.gc();
    }


}
