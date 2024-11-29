package org.echo.worldborder.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.echo.worldborder.Main;

import java.io.File;

public class Messages {

    Main plugin;
    private YamlConfiguration yaml;
    private File file;

    // Constructeur de la classe
    public Messages(Main plugin) {
        this.plugin = plugin;
        this.yaml = loadConfig("messages.yml");
    }

    // Méthode pour charger les messages depuis le fichier de configuration
    private YamlConfiguration loadConfig(String file_name) {

        if (!this.plugin.getDataFolder().exists()) {
            this.plugin.getDataFolder().mkdir();
        }

        this.file = new File(this.plugin.getDataFolder(), file_name);

        if (!this.file.exists()) {
            this.plugin.saveResource(file_name, false);
        }

        return YamlConfiguration.loadConfiguration(this.file);
    }

    // Méthode pour récupérer un message à partir de sa clé
    public String getMessage(String key) {
        String message = yaml.getString(key);
        if (message != null) {
            return ChatColor.translateAlternateColorCodes('&', message);
        }
        return "";
    }

    // Méthode pour récupérer un message avec des remplacements
    private String getFormattedMessage(String key, String... replacements) {

        String message = getMessage(key);

        for (int i = 0; i < replacements.length; i += 2) {
            String placeholder = replacements[i];
            String replacement = replacements[i + 1];
            message = message.replace(placeholder, replacement);
        }
        return message;
    }

    public String getPermissionCommand() {
        return getMessage("permission-command");
    }

    public String getOutOfBounds(String worldName) {
        return getFormattedMessage("out-of-bounds", "{world}", worldName);
    }
}
