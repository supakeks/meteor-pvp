package minegame159.meteorpvp.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import minegame159.meteorpvp.duels.Duel;
import minegame159.meteorpvp.duels.Duels;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Regions {
    public static ProtectedRegion KITCREATOR;

    public static ProtectedRegion OW_SPAWN;
    public static ProtectedRegion OW_AC_BARRIER;
    public static ProtectedRegion OW_PVP;

    public static ProtectedRegion NETHER_SPAWN;
    public static ProtectedRegion NETHER_AC_BARRIER;
    public static ProtectedRegion NETHER_PVP;

    public static void onEnable() {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager OW = container.get(BukkitAdapter.adapt(Utils.OVERWORLD));
        RegionManager NETHER = container.get(BukkitAdapter.adapt(Utils.NETHER));

        KITCREATOR = OW.getRegion("kitcreator");

        OW_SPAWN = OW.getRegion("spawn");
        OW_AC_BARRIER = OW.getRegion("ac_barrier");
        OW_PVP = OW.getRegion("pvp");

        NETHER_SPAWN = NETHER.getRegion("spawn");
        NETHER_AC_BARRIER = NETHER.getRegion("ac_barrier");
        NETHER_PVP = NETHER.getRegion("pvp");
    }

    public static boolean isIn(ProtectedRegion region,Location pos) {
        return region.contains(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
    }

    public static boolean isIn(ProtectedRegion region, Entity entity) {
        return isIn(region, entity.getLocation());
    }

    public static boolean isInAnyPvp(Player player, boolean duels) {
        if (player.getWorld() == Utils.OVERWORLD) {
            return isIn(OW_PVP, player) || Duels.INSTANCE.get(player) != null;
        }

        if (duels) {
            Duel duel = Duels.INSTANCE.get(player);
            if (duel != null) return true;
        }

        return isIn(NETHER_PVP, player);
    }

    public static boolean isInAnyPvp(Player player) {
        return isInAnyPvp(player, true);
    }

    public static boolean isInAnyOW(Player player) {
        return isIn(OW_SPAWN, player) || isIn(OW_PVP, player) || isIn(KITCREATOR, player);
    }

    public static boolean isInAnyNether(Player player) {
        return isIn(NETHER_SPAWN, player) || isIn(NETHER_PVP, player);
    }

    public static boolean isInAnyBuildable(Location location) {
        if (location.getWorld() == Utils.OVERWORLD) {
            return isIn(OW_PVP, location) || Duels.INSTANCE.overworldNormal.isIn(location) || Duels.INSTANCE.overworldFlat.isIn(location);
        }

        return isIn(NETHER_PVP, location) || Duels.INSTANCE.netherNormal.isIn(location) || Duels.INSTANCE.netherFlat.isIn(location);
    }

    public static Region toWERegion(ProtectedRegion region) {
        return new CuboidRegion(region.getMinimumPoint(), region.getMaximumPoint());
    }
}
