package minegame159.thebestplugin.utils;

import org.bukkit.entity.Entity;

public class EntityTimer {
    public Entity entity;
    public int timer;

    public EntityTimer(Entity entity, int timer) {
        this.entity = entity;
        this.timer = timer;
    }
}
