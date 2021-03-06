package minegame159.meteorpvp.commands;

import org.bukkit.Bukkit;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;

public class Commands {
    public static final List<MyCommand> COMMANDS = new ArrayList<>();

    public static void register() {
        COMMANDS.clear();

        for (Class<? extends MyCommand> klass : new Reflections("minegame159.meteorpvp.commands").getSubTypesOf(MyCommand.class)) {
            try {
                MyCommand command = klass.newInstance();
                COMMANDS.add(command);
                Bukkit.getCommandMap().register("meteorpvp", command);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
