package com.weebly.openboxtechnologies.hnrchat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by zhiyuanqi on 26/05/16.
 */

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        new Listener(this);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender e, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("hnrchat")) {
            e.sendMessage("This is the HnR Chat Base Command!");
            return true;
        }
        return false;
    }

}
