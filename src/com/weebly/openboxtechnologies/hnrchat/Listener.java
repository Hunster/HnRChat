package com.weebly.openboxtechnologies.hnrchat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

class Listener implements org.bukkit.event.Listener {

    private JavaPlugin plugin;
    private HashMap<UUID, String> playerChatCache = new HashMap<>();

    Listener(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent e) {
        updatePlayer(e.getPlayer());
    }

    @EventHandler
    public void onPlayerChat (AsyncPlayerChatEvent e) {
        String message = playerChatCache.get(e.getPlayer().getUniqueId());
        message = message.replaceAll("%message%", e.getMessage());
        e.setFormat(ChatColor.translateAlternateColorCodes('&', message));
    }

    private void updatePlayer(Player p) {
        BukkitRunnable r = new BukkitRunnable() {
            @Override
            public void run() {
                String teamName = hnrchat.perms.getHighestRank(p);
                try {
                    hnrchat.scoreboard.registerNewTeam(teamName);
                } catch (IllegalArgumentException a) {

                }
                String scoreboardName = hnrchat.headFormat;
                scoreboardName = scoreboardName.replace("%rank%", teamName);
                scoreboardName = scoreboardName.replace("%level%", Integer.toString(hnrchat.levels.getLevel(p.getUniqueId())));
                hnrchat.scoreboard.getTeam(teamName).setPrefix(ChatColor.translateAlternateColorCodes('§', scoreboardName));
                hnrchat.scoreboard.getTeam(teamName).addEntry(p.getName());

                String tabName = hnrchat.tabFormat;
                tabName = tabName.replace("%rank%", teamName);
                tabName = tabName.replace("%level%", Integer.toString(hnrchat.levels.getLevel(p.getUniqueId())));
                tabName = tabName.replace("%name%", p.getName());
                p.setPlayerListName(ChatColor.translateAlternateColorCodes('&', tabName));

                String chatFormat = hnrchat.chatFormat;
                chatFormat = chatFormat.replace("%rank%", teamName);
                chatFormat = chatFormat.replace("%level%", Integer.toString(hnrchat.levels.getLevel(p.getUniqueId())));
                chatFormat = chatFormat.replace("%name%", p.getName());
                playerChatCache.put(p.getUniqueId(), chatFormat);
            }
        };
        r.runTaskLater(plugin, 2);
    }
}
