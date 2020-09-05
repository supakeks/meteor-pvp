package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.Stats;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SuicideCommand extends MyCommand {
    public SuicideCommand() {
        super("suicide", "Kills you.", null, null);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            ((Player) sender).setHealth(0);

            Stats.INSTANCE.deaths--;
            Stats.INSTANCE.changed();
        }
        return true;
    }
}
