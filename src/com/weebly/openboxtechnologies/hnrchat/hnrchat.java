package com.weebly.openboxtechnologies.hnrchat;

import com.weebly.openboxtechnologies.hnrlevels.hnrlevels;
import com.weebly.openboxtechnologies.hnrperms.hnrperms;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class hnrchat extends JavaPlugin {

    static hnrlevels levels;
    static hnrperms perms;

    static String chatFormat;
    static HashMap<String, String> rankTabMap = new HashMap<>();
    static HashMap<String, String> rankHeadMap = new HashMap<>();

    static Scoreboard scoreboard;

    @Override
    public void onEnable() {
        new Listener(this);

        levels = (hnrlevels) getServer().getPluginManager().getPlugin("HnRLevels");
        perms = (hnrperms) getServer().getPluginManager().getPlugin("HnRPerms");

        if (levels == null | perms == null) {
            getLogger().severe("A dependency could not be loaded! Please check your other plugins.");
            getLogger().severe("Disabling HnRChat ...");
            getServer().getPluginManager().disablePlugin(this);
        }

        scoreboard = getServer().getScoreboardManager().getMainScoreboard();
        loadConfig();
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
            e.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHnRPerms&7&l> &9To use this feature, you must be rank &8(&cADMIN&8)&9!"));
            return true;
        }

        if (args.length == 0) {
            e.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHnRChat&7&l> &9This is the HnR Chat main command!"));
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                loadConfig();
                e.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHnRChat&7&l> &9The config file has been reloaded!"));
                return true;
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
                int amountOfLevels = levels.getAmountOfLevels();
                for (int i = 0; i < amountOfLevels; i++) {
                    getConfig().set("levels." + i, "&5");
                }

                ArrayList<String> ranks = new ArrayList<>();
                ranks.addAll(perms.getRanksList());

                for (String i : ranks) {
                    getConfig().set("ranks." + i + ".tab", "%rank% &4%name% &5%level%");
                    getConfig().set("ranks." + i + ".head", "%rank% &5%level%");
                }
                getConfig().save(file);
            } else {
                getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        chatFormat = getConfig().getString("globalChatFormat");

        ArrayList<String> ranks = new ArrayList<>();
        ranks.addAll(perms.getRanksList());

        for (String i : ranks) {
            String tabTemp = getConfig().getString("ranks." + i + ".tab");
            tabTemp = tabTemp.replace("%rank%", i);
            tabTemp = ChatColor.translateAlternateColorCodes('&', tabTemp);
            rankTabMap.put(i, tabTemp);
            String headTemp = getConfig().getString("ranks." + i + ".head");
            headTemp = headTemp.replace("%rank%", i);
            headTemp = headTemp.replace('&', '§');
            rankHeadMap.put(i, headTemp);
        }
    }
}
