package de.phyrone.libloader;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class LibLauncherTest {

    @Test
    void main() throws IOException {
        LibLauncher.main(new String[]{"from test"});
    }
}
