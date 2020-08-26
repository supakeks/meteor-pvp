package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.Kit;
import minegame159.thebestplugin.Kits;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class KitsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        Inventory gui = Bukkit.createInventory(player, 9 * 6, Kits.GUI_TITLE);

        int i = 0;
        for (Kit kit : Kits.INSTANCE.getKits()) {
            if (i >= 9 * 6) break;

            ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
            ItemMeta meta = itemStack.getItemMeta();

            meta.setDisplayName(kit.name);
            List<String> lore = new ArrayList<>(1);
            lore.add(Bukkit.getOfflinePlayer(kit.author).getName());
            meta.setLore(lore);

            itemStack.setItemMeta(meta);
            gui.setItem(i, itemStack);

            i++;
        }

        player.openInventory(gui);
        return true;
    }
}