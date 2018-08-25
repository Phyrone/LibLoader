package de.phyrone.libloader.bukkit;

import de.phyrone.libloader.LibLoader;
import de.phyrone.libloader.Libs;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPlugin extends JavaPlugin {
    @Override
    public void onLoad() {
        Libs.setLibLoader(new LibLoader());
    }
}
