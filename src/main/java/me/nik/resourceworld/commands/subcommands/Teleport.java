package me.nik.resourceworld.commands.subcommands;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.utils.ColourUtils;
import me.nik.resourceworld.utils.TeleportUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.UUID;

public class Teleport extends SubCommand {
    Plugin plugin = ResourceWorld.getPlugin(ResourceWorld.class);
    private HashMap<UUID, Long> cooldown = new HashMap<UUID, Long>();
    private int cdtime = plugin.getConfig().getInt("teleport_cooldown");
    private HashMap<UUID, Long> delay = new HashMap<UUID, Long>();
    private int delaytime = plugin.getConfig().getInt("teleport_delay");

    @Override
    public String getName() {
        return "TP";
    }

    @Override
    public String getDescription() {
        return "Randomly Teleport To The Resource World!";
    }

    @Override
    public String getSyntax() {
        return "/Resource TP";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length > 0){
            if (!plugin.getConfig().getBoolean("enabled")) {
                player.sendMessage(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("not_exist")));
            } else if (!player.hasPermission("rw.tp")) {
                player.sendMessage(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("no_perm")));
            } else if (cooldown.containsKey(player.getUniqueId())) {
                long secondsleft = ((cooldown.get(player.getUniqueId()) / 1000) + cdtime) - (System.currentTimeMillis() / 1000);
                if (secondsleft > 0) {
                    player.sendMessage(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("cooldown_message")) + secondsleft + " Seconds");
                } else {
                    player.sendMessage(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("teleport_delay")) + plugin.getConfig().getInt("teleport_delay") + " Seconds");
                    cooldown.put(player.getUniqueId(), System.currentTimeMillis());
                    BukkitScheduler scheduler = plugin.getServer().getScheduler();
                    scheduler.runTaskLater(ResourceWorld.getPlugin(ResourceWorld.class), new Runnable() {
                        @Override
                        public void run() {
                            World world = Bukkit.getWorld(plugin.getConfig().getString("world_name"));
                            player.teleport(new TeleportUtils().generateLocation(world));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(plugin.getConfig().getString("effect")), plugin.getConfig().getInt("duration") * 20, plugin.getConfig().getInt("amplifier")));
                        }
                    }, plugin.getConfig().getInt("teleport_delay") * 20);
                }
            }else{
                player.sendMessage(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("teleport_delay")) + plugin.getConfig().getInt("teleport_delay") + " Seconds");
                cooldown.put(player.getUniqueId(), System.currentTimeMillis());
                BukkitScheduler scheduler = plugin.getServer().getScheduler();
                scheduler.runTaskLater(ResourceWorld.getPlugin(ResourceWorld.class), new Runnable() {
                    @Override
                    public void run() {
                        World world = Bukkit.getWorld(plugin.getConfig().getString("world_name"));
                        player.teleport(new TeleportUtils().generateLocation(world));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(plugin.getConfig().getString("effect")), plugin.getConfig().getInt("duration") * 20, plugin.getConfig().getInt("amplifier")));
                    }
                }, plugin.getConfig().getInt("teleport_delay") * 20);
            }
        }
    }
}