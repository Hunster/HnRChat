package com.weebly.openboxtechnologies.hnrchat;

import com.weebly.openboxtechnologies.hnrlevels.LevelChangedEvent;
import com.weebly.openboxtechnologies.hnrperms.RankChangedEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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

    @EventHandler
    public void onLevelChange (LevelChangedEvent e) {
        updatePlayer(plugin.getServer().getPlayer(e.getPlayer()));
    }

    @EventHandler
    public void onRankChange (RankChangedEvent e) {
        updatePlayer(plugin.getServer().getPlayer(e.getPlayer()));
    }

    @EventHandler
    public void onPlayerQuit (PlayerQuitEvent e) {
        hnrchat.scoreboard.getTeam(e.getPlayer().getName()).unregister();
    }

    private void updatePlayer(Player p) {
        BukkitRunnable r = new BukkitRunnable() {
            @Override
            public void run() {
                String highestRank = hnrchat.perms.getHighestRank(p);
                String level = Integer.toString(hnrchat.levels.getLevel(p.getUniqueId()));
                String name = p.getName();

                level = plugin.getConfig().getString("level." + level) + level;
                level = ChatColor.translateAlternateColorCodes('&', level);

                String chatText = hnrchat.chatFormat;
                chatText = chatText.replace("%rank%", highestRank);
                chatText = chatText.replace("%name%", name);
                chatText = chatText.replace("%level%", level);
                playerChatCache.put(p.getUniqueId(), chatText);

                String tabText = hnrchat.rankTabMap.get(highestRank);
                tabText = tabText.replace("%name%", name);
                tabText = tabText.replace("%rank%", highestRank);
                tabText = tabText.replace("%level%", level);
                p.setPlayerListName(tabText);

                String headText = hnrchat.rankHeadMap.get(highestRank);
                headText = headText.replace("%rank%", name);
                headText = headText.replace("%level%", level);

                hnrchat.scoreboard.registerNewTeam(name);
                hnrchat.scoreboard.getTeam(name).setPrefix(headText);
                hnrchat.scoreboard.getTeam(name).addEntry(name);
            }
        };
        r.runTaskLater(plugin, 2);
    }
}
