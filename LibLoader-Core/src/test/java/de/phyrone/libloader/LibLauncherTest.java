package de.phyrone.libloader;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LibLauncherTest {

    @Test
    void main() throws IOException {
        LibLauncher.main(new String[]{"from test"});
    }
}
