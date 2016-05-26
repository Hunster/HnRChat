package com.weebly.openboxtechnologies.hnrchat;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by zhiyuanqi on 26/05/16.
 */

public class Listener implements org.bukkit.event.Listener {

    private JavaPlugin plugin;

    public Listener(JavaPlugin plugin) {

        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }
}
