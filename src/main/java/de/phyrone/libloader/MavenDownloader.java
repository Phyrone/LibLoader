package de.phyrone.libloader;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MavenDownloader {
    public static final String VERSION_SEPERATOR = ":";
    ThreadFactory factory = new ThreadFactoryBuilder().setDaemon(false).setPriority(4).setNameFormat("DownloadTask[%d]").build();
    ThreadPoolExecutor executor = new ThreadPoolExecutor(0, 1, 10L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), factory);
    private List<Repository> repos = new ArrayList<Repository>();

    {

        try {
            addRepository(new URL("https://repo.maven.apache.org/maven2/"));
            addRepository(new URL("https://jitpack.io/"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    void addRepository(Repository repository) {
        repos.add(repository);
    }

    void addRepository(URL url) {
        addRepository(new Repository(url));
    }

    boolean isUrl(String exeptedUrl) {
        try {
            new URL(exeptedUrl);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public void setMaxParalelDownloads(int maxParalelDownloads) {
        executor.setMaximumPoolSize(maxParalelDownloads < 1 ? 1 : maxParalelDownloads);
    }

    List<URL> toUrls(String downloadString) {
        try {
            return Arrays.asList(new URL(downloadString));
        } catch (MalformedURLException e) {
            String version = null;
            String id = null;
            if (downloadString.contains(":")) {
                String[] splited = downloadString.split(VERSION_SEPERATOR, 2);
                id = splited[0];
                version = splited[1];
            } else {
                id = downloadString;
                version = "latest";
            }


        }

        return null;
    }public static final String SPLITERATOR = ".";

    public void shutdown() {
        executor.shutdown();
        try {
            executor.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class DownloadTask implements Runnable {

        public void run() {

        }
    }
}
