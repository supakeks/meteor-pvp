package minegame159.thebestplugin.listeners;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.NumberConversions;

import java.util.HashMap;
import java.util.Map;

public class AntiCheatListener implements Listener {
    private final Map<Player, Location> lastValidPositions = new HashMap<>();
    private final Map<Player, Location> lastValidPositionsOnGround = new HashMap<>();
    private final Map<Player, Integer> inAirTicks = new HashMap<>();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        lastValidPositions.remove(event.getPlayer());
        lastValidPositionsOnGround.remove(event.getPlayer());
        inAirTicks.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        lastValidPositions.remove(event.getPlayer());
        lastValidPositionsOnGround.remove(event.getPlayer());
        inAirTicks.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        lastValidPositions.remove(event.getEntity());
        lastValidPositionsOnGround.remove(event.getEntity());
        inAirTicks.remove(event.getEntity());
    }

    @EventHandler
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        lastValidPositions.remove(event.getPlayer());
        lastValidPositionsOnGround.remove(event.getPlayer());
        inAirTicks.remove(event.getPlayer());
    }

    @EventHandler
    public void onTick(ServerTickEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isDead() || player.getGameMode() != GameMode.SURVIVAL) continue;

            Location pos = player.getLocation();
            Location lastPos = lastValidPositions.get(player);
            if (lastPos == null) lastPos = pos;

            Location teleportPos = null;

            double horizontalDistance = NumberConversions.square(pos.getX() - lastPos.getX()) + NumberConversions.square(pos.getZ() - lastPos.getZ());

            // Speed check
            if (horizontalDistance > 1) {
                // Moving too fast
                teleportPos = lastPos;
                sendDebug(player, "Speed, value %.3f", horizontalDistance);
            } else {
                // Flight check
                double verticalDistance = pos.getY() - lastPos.getY();

                if (!isOnGround(player) && !player.isSwimming()) {
                    // Flight - In Air check
                    if (verticalDistance < 0.1 && verticalDistance > -0.1) {
                        int airTicks = inAirTicks.getOrDefault(player, 0);
                        airTicks++;

                        if (airTicks > 8) {
                            // Flying
                            airTicks = 0;
                            teleportPos = lastValidPositionsOnGround.getOrDefault(player, lastPos);
                            sendDebug(player, "Flight - In Air");
                        }

                        inAirTicks.put(player, airTicks);
                    } else {
                        inAirTicks.put(player, 0);
                    }

                    // Flight - Fast vertical acceleration check
                    if (verticalDistance > 0.754) {
                        teleportPos = lastValidPositionsOnGround.getOrDefault(player, lastPos);
                        sendDebug(player, "Flight - Fast Acceleration, value %.3f", verticalDistance);
                    }
                }
            }

            if (teleportPos == null) {
                // Passed all checks
                lastValidPositions.put(player, pos);
                if (isOnGround(player)) {
                    lastValidPositionsOnGround.put(player, pos);
                }
            } else {
                // Didn't pass some checks
                player.teleport(teleportPos);
            }
        }
    }

    private void sendDebug(Player player, String format, Object... args) {
        player.sendMessage(String.format("%s[%sAC%s] %s", ChatColor.GRAY, ChatColor.RED, ChatColor.GRAY, ChatColor.WHITE) + String.format(format, args));
    }

    private boolean isOnGround(Player player) {
        Location location = player.getLocation();
        double w = player.getWidth() / 2;

        if (location.add(w, 0, w).getBlock().getRelative(BlockFace.DOWN).getType().isSolid()) return true;
        if (location.subtract(w * 2, 0, w * 2).getBlock().getRelative(BlockFace.DOWN).getType().isSolid()) return true;
        if (location.add(w, 0, w).add(w, 0, -w).getBlock().getRelative(BlockFace.DOWN).getType().isSolid()) return true;
        return location.subtract(w, 0, -w).add(-w, 0, w).getBlock().getRelative(BlockFace.DOWN).getType().isSolid();
    }
}