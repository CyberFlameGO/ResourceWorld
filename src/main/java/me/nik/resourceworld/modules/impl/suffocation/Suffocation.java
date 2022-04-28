package me.nik.resourceworld.modules.impl.suffocation;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.modules.ListenerModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class Suffocation extends ListenerModule {

    public Suffocation(ResourceWorld plugin) {
        super(Config.Setting.WORLD_DISABLE_SUFFOCATION.getBoolean(), plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION)) return;

        Player p = (Player) e.getEntity();

        if (p.getWorld().getName().equals(Config.Setting.WORLD_NAME.getString())) e.setCancelled(true);
    }
}