package org.echo.worldborder.listener;

import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.echo.worldborder.Main;

public class MovementListener implements Listener {

    private final Main plugin;

    public MovementListener(Main plugin) { this.plugin = plugin; }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (plugin.getMyConfig().isEnforce()) {
            WorldBorder border = event.getTo().getWorld().getWorldBorder();
            if (!border.isInside(event.getTo())) {
                event.getPlayer().teleport(plugin.getManager().clampToBorder(event.getTo()));
                event.getPlayer().sendMessage(plugin.getMessages().getOutOfBounds(event.getTo().getWorld().getName()));
            }
        }

    }
}
