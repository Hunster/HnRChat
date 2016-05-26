package com.weebly.openboxtechnologies.hnrchat;

import com.weebly.openboxtechnologies.hnrlevels.hnrlevels;
import com.weebly.openboxtechnologies.hnrperms.hnrperms;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.ScoreboardCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.util.Set;

public class hnrchat extends JavaPlugin {

    static hnrlevels levels;
    static hnrperms perms;

    static String chatFormat;
    static String tabFormat;
    static String headFormat;

    static Scoreboard scoreboard;

    @Override
    public void onEnable() {
        new Listener(this);

        levels = (hnrlevels) getServer().getPluginManager().getPlugin("HnRLevels");
        perms = (hnrperms) getServer().getPluginManager().getPlugin("HnRPerms");

        loadConfig();
        scoreboard = getServer().getScoreboardManager().getMainScoreboard();
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender e, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("hnrchat")) {
            return false;
        }

        if (!e.hasPermission("chat.admin")) {
            ChatColor.translateAlternateColorCodes('&', "&e&lHnRPerms&7&l> &9To use this feature, you must be rank &8(&cADMIN&8)&9!");
        }

        if (args.length == 0) {
            e.sendMessage("&e&HnRChat&7&l> &9This is the HnR Chat main command!");
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                loadConfig();
                e.sendMessage("&e&HnRChat&7&l> &9The config file has been reloaded!");
            }
        }
        return true;
    }

    private void loadConfig() {
        try {
            if (!getDataFolder().exists()) {
                if (!getDataFolder().mkdirs()) {
                    getLogger().severe("A problem occurred when creating the config file.");
                }
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info("Config.yml not found, creating!");
                saveDefaultConfig();
            } else {
                getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        chatFormat = getConfig().getString("globalChatFormat");
        tabFormat = getConfig().getString("globalTabFormat");
        headFormat = getConfig().getString("globalHeadFormat");
    }
}
