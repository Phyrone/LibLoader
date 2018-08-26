package de.phyrone.libloader;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class LibLoader {
    private Method addURLMethod;
    private List<RemoteRepository> repositories = Collections.synchronizedList(
            new ArrayList<>(
                    Arrays.asList(
                            new RemoteRepository.Builder("central", "default", "https://repo.maven.apache.org/maven2/").build(),
                            new RemoteRepository.Builder("jitpack.io", "default", "https://jitpack.io/").build()
                    )
            )
    );
    private RepositorySystem system;
    private DefaultRepositorySystemSession defaultRepositorySystemSession = MavenRepositorySystemUtils.newSession();
    private ClassLoader classLoader;

    public LibLoader() {
        if (!(this.getClass().getClassLoader() instanceof URLClassLoader))
            throw new ClassCastException("can't cast Current Classloader to UrlClassloader");
        init((URLClassLoader) this.getClass().getClassLoader());
    }

    public LibLoader(URLClassLoader classLoader) {
        init(classLoader);
    }

    private void init(URLClassLoader classLoader) {
        this.classLoader = classLoader;
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
        system = locator.getService(RepositorySystem.class);
        LocalRepository localRepository = new LocalRepository(System.getProperty("user.home", "/") + "/.m2");
        defaultRepositorySystemSession.setLocalRepositoryManager(system.newLocalRepositoryManager(defaultRepositorySystemSession, localRepository));
        try {
            addURLMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            addURLMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void addRepository(String url) {
        addRepository(new RemoteRepository.Builder(UUID.randomUUID().toString(), "default", url).build());
    }

    public void addRepository(RemoteRepository repository) {
        repositories.add(repository);
    }


    public void require(String coordinates) throws IOException {
        require(new DefaultArtifact(coordinates));
    }

    public void require(Artifact artifact) throws IOException {
        try {
            addFileToClassLoader(
                    resolveArtifact(artifact)
            );
        } catch (MalformedURLException | InvocationTargetException | IllegalAccessException | ArtifactResolutionException e) {
            throw new IOException("could not Load Library!");
        }


    }


    private void addFileToClassLoader(File file) throws MalformedURLException, InvocationTargetException, IllegalAccessException {
        addURLMethod.invoke(classLoader, file.toURI().toURL());
    }

    private File resolveArtifact(Artifact artifact) throws ArtifactResolutionException {
        ArtifactRequest artifactRequest = new ArtifactRequest();
        artifactRequest.setArtifact(artifact);
        repositories.forEach(artifactRequest::addRepository);
        ArtifactResult result = system.resolveArtifact(defaultRepositorySystemSession, artifactRequest);
        return result.getArtifact().getFile();
    }
}
