package org.Teev.playerKillingMoneyIndex;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public final class PlayerKillingMoneyIndex extends JavaPlugin implements Listener {

    private File dataFile;
    private FileConfiguration dataConfig;

    @Override
    public void onEnable() {
        createDataFile();
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("----------\n\n\n");
        getLogger().info("playerKillingMoneyIndex has been loaded.");
        getLogger().info("----------\n");

    }

    @Override
    public @NotNull Path getDataPath() {
        return super.getDataPath();
    }

    @Override
    public void onDisable() {
        saveDataFile();
        getLogger().info("playerKillingMoneyIndex has been disabled.");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() != null && event.getEntity().getKiller().getType() == EntityType.PLAYER) {

            UUID victimUUID = event.getEntity().getUniqueId();
            UUID killerUUID = event.getEntity().getKiller().getUniqueId();

            int currentKills = dataConfig.getInt("players." + killerUUID + ".kills", 0);
            dataConfig.set("players." + killerUUID + ".kills", currentKills + 1);

            int currentDeaths = dataConfig.getInt("players." + victimUUID + ".deaths", 0);
            dataConfig.set("players." + victimUUID + ".deaths", currentDeaths + 1);

            saveDataFile();
        }
    }

    private void createDataFile() {
        dataFile = new File(getDataFolder(), "playerKillingMoneyIndex_kills_deaths.yml");

        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            try {
                dataFile.createNewFile();
                getLogger().info("Created new kills_deaths.yml file.");
            } catch (IOException e) {
                getLogger().severe("Could not create kills_deaths.yml file.");
                e.printStackTrace();
            }
        }

        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    private void saveDataFile() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            getLogger().severe("Could not save data to kills_deaths.yml file.");
            e.printStackTrace();
        }
    }
}
