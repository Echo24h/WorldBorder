package org.echo.worldborder;

import org.bukkit.plugin.java.JavaPlugin;
import org.echo.worldborder.commands.Commands;
import org.echo.worldborder.config.Config;
import org.echo.worldborder.config.Messages;
import org.echo.worldborder.listener.TeleportListener;
import org.echo.worldborder.listener.MovementListener;

public final class Main extends JavaPlugin {

    private Config config;
    private BordersManager manager;
    private Messages messages;

    @Override
    public void onEnable() {
        this.config = new Config(this);
        this.manager = new BordersManager(this);
        this.messages = new Messages(this);
        manager.createBorders();
        // Enregistrement de la commande
        getCommand("border").setExecutor(new Commands(this));
        getCommand("worldborder").setExecutor(new Commands(this));
        getServer().getPluginManager().registerEvents(new TeleportListener(this), this);
        getServer().getPluginManager().registerEvents(new MovementListener(this), this);
    }

    @Override
    public void onDisable() { }

    public void reloadPlugin() {
        this.messages = new Messages(this);
        manager.removeBorders();
        this.config = new Config(this);
        manager.createBorders();
    }

    public Config getMyConfig() {
        return config;
    }

    public BordersManager getManager() {
        return manager;
    }

    public Messages getMessages() {
        return messages;
    }

}
