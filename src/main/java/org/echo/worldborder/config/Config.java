package org.echo.worldborder.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.echo.worldborder.Border;
import org.echo.worldborder.Main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private Main plugin;
    private File file;
    private YamlConfiguration config;

    private Map<String, Border> borders;
    private boolean enforce;

    public Config(Main plugin) {
        this.plugin = plugin;
        this.config = loadConfigYaml("config.yml");
        this.borders = new HashMap<>();
        loadWorldBorders();
        loadConfig();
    }

    private YamlConfiguration loadConfigYaml(String file_name) {

        if(!this.plugin.getDataFolder().exists()) {
            this.plugin.getDataFolder().mkdir();
        }

        this.file = new File(this.plugin.getDataFolder(), file_name);

        if (!this.file.exists()) {
            this.plugin.saveResource(file_name, false);
        }

        return YamlConfiguration.loadConfiguration(this.file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadConfig() {
        this.enforce = config.getBoolean("enforce", false);
    }

    private void loadWorldBorders() {

        ConfigurationSection bordersSection = config.getConfigurationSection("worlds");
        if (bordersSection != null) {
            for (String worldName : bordersSection.getKeys(false)) {
                ConfigurationSection borderSection = bordersSection.getConfigurationSection(worldName);
                if (borderSection != null) {
                    int borderSize = borderSection.getInt("border_size");
                    int centerX = borderSection.getInt("center_x");
                    int centerZ = borderSection.getInt("center_z");

                    Border border = new Border(borderSize, centerX, centerZ);
                    this.borders.put(worldName, border);
                }
            }
        }
    }
    public void removeBorder(String worldName) {
        // Supprimer la bordure de la liste
        Border removedBorder = borders.remove(worldName);

        if (removedBorder != null) {
            // Supprimer la bordure de la configuration
            config.set("worlds." + worldName, null);
            save();
        }
    }

    public void addBorder(String worldName, Border border) {
        borders.put(worldName, border);

        ConfigurationSection bordersSection = config.getConfigurationSection("worlds");
        if (bordersSection == null) {
            bordersSection = config.createSection("worlds");
        }

        ConfigurationSection borderSection = bordersSection.createSection(worldName);
        borderSection.set("border_size", border.getBorderSize());
        borderSection.set("center_x", border.getCenterX());
        borderSection.set("center_z", border.getCenterZ());

        save();
    }

    public Map<String, Border> getBorders() {
        return borders;
    }

    public Border getBorder(String worldName) {
        return borders.get(worldName);
    }

    public boolean isEnforce() {
        return enforce;
    }
}
