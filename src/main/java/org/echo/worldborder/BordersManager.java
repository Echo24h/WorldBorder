package org.echo.worldborder;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Map;

public class BordersManager {

    private Main plugin;

    public BordersManager(Main plugin) {
        this.plugin = plugin;
    }

    public void addBorder(String worldName, int borderSize, int centerX, int centerZ) {
        Border border = new Border(borderSize, centerX, centerZ);
        if (createBorder(worldName, border, false)) {
            plugin.getMyConfig().addBorder(worldName, border);
        }
    }

    public void deleteBorder(String worldName) {
        removeBorder(worldName);
        plugin.getMyConfig().removeBorder(worldName);
    }

    public void createBorders() {
        for (Map.Entry<String, Border> entry : this.plugin.getMyConfig().getBorders().entrySet()) {
            createBorder(entry.getKey(), entry.getValue(), true);
        }
    }

    public void removeBorders() {
        for (Map.Entry<String, Border> entry : this.plugin.getMyConfig().getBorders().entrySet()) {
            removeBorder(entry.getKey());
        }
    }

    private void removeBorder(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            org.bukkit.WorldBorder worldBorder = world.getWorldBorder();
            worldBorder.reset();
        }
    }

    private boolean createBorder(String worldName, Border border, boolean isForLoad) {
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            int borderSize = border.getBorderSize();
            int centerX = border.getCenterX();
            int centerZ = border.getCenterZ();

            org.bukkit.WorldBorder worldBorder = world.getWorldBorder();
            worldBorder.setSize(borderSize);
            worldBorder.setCenter(centerX, centerZ);
            return true;
        }
        else {
            if (isForLoad)
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[WorldBorder] ERROR: Unable to create world border " + ChatColor.YELLOW + worldName + ChatColor.RED + " because this world does not exist");
            return false;
        }
    }

    public boolean isInBorder(Location location) {

        Border border = plugin.getMyConfig().getBorder(location.getWorld().getName());

        if (border != null)
            if (border.isInBorder(location))
                return true;

        return false;
    }
}
