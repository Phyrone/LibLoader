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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class LibLoader {
    Method addURLMethod;
    private List<RemoteRepository> repositories = Collections.synchronizedList(Arrays.asList(
            new RemoteRepository.Builder("central", "default", "https://repo.maven.apache.org/maven2/").build(),
            new RemoteRepository.Builder("jitpack.io", "default", "https://jitpack.io/").build()
    ));
    private RepositorySystem system;
    private DefaultRepositorySystemSession defaultRepositorySystemSession = MavenRepositorySystemUtils.newSession();

    LibLoader() {
        if (!(this.getClass().getClassLoader() instanceof URLClassLoader))
            throw new ClassCastException("can't cast Current Classloader to UrlClassloader");
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

    public void addRepositroy(String url) {
        repositories.add(new RemoteRepository.Builder(UUID.randomUUID().toString(), "default", url).build());
    }

    public void addRepositroy(RemoteRepository repository) {
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
        addURLMethod.invoke(getClass().getClassLoader(), file.toURI().toURL());
    }

    private File resolveArtifact(Artifact artifact) throws ArtifactResolutionException {
        ArtifactRequest artifactRequest = new ArtifactRequest();
        artifactRequest.setArtifact(artifact);
        repositories.forEach(artifactRequest::addRepository);
        ArtifactResult result = system.resolveArtifact(defaultRepositorySystemSession, artifactRequest);
        return result.getArtifact().getFile();
    }
}
