package online.umbcraft.plugins;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class DeathMessage extends JavaPlugin {

    private String death_string;

    @Override
    public void onEnable() {

        File configFile = new File(this.getDataFolder(), "config.yml");
        if (!configFile.exists())
            saveDefaultConfig();

        death_string = getConfig().getString("death-message");
        Bukkit.getServer().getPluginManager().registerEvents( new DeathListener(this), this);
    }

    public String getDeathString() {
        return death_string;
    }

    @Override
    public void onDisable() {
    }
}
