package de.phyrone.libloader.bungee;

import de.phyrone.libloader.LibLoader;
import de.phyrone.libloader.Libs;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeePlugin extends Plugin {
    @Override
    public void onLoad() {
        Libs.setLibLoader(new LibLoader());
    }
}
