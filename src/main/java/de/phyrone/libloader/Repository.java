package de.phyrone.libloader;

import java.net.URL;

public class Repository {
    private URL url;
private String username;
private String password;
    public Repository(URL url) {
        this.url = url;
    }

    public Repository(URL url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public URL getUrl() {
        return url;
    }
}
