package com.weebly.openboxtechnologies.hnrchat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

class Listener implements org.bukkit.event.Listener {

    private JavaPlugin plugin;

    Listener(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent e) {
        BukkitRunnable r = new BukkitRunnable() {
            @Override
            public void run() {
                String teamName = hnrchat.perms.getHighestRank(e.getPlayer());
                try {
                    hnrchat.scoreboard.registerNewTeam(teamName);
                } catch (IllegalArgumentException a) {

                }
                String scoreboardName = hnrchat.headFormat;
                scoreboardName.replaceAll("%rank%", teamName);
                scoreboardName.replaceAll("%level%", Integer.toString(hnrchat.levels.getLevel(e.getPlayer().getUniqueId())));
                hnrchat.scoreboard.getTeam(teamName).setPrefix(scoreboardName);
                hnrchat.scoreboard.getTeam(teamName).addEntry(e.getPlayer().getName());

                String tabName = hnrchat.tabFormat;
                tabName.replaceAll("%rank%", teamName);
                tabName.replaceAll("%level%", Integer.toString(hnrchat.levels.getLevel(e.getPlayer().getUniqueId())));
                tabName.replaceAll("%name%", e.getPlayer().getName());
                e.getPlayer().setDisplayName(tabName);
            }
        };
        r.runTaskLater(plugin, 2);
    }

    @EventHandler
    public void onPlayerChat (AsyncPlayerChatEvent e) {

    }

}
