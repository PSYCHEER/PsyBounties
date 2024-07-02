package me.psycheer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class psyevents implements Listener{


    private static final Logger log = LogManager.getLogger(psyevents.class);
    Plugin main = psybounties.instance;

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        FileConfiguration config = main.getConfig();
        String alert = config.getString("alerts.join-message");
        //String alert = hex(message);
        if(config.getBoolean("alerts.enabled", true)){
            if(config.getStringList("enabled-worlds").contains(player.getLocation().getWorld().getName())) {
                if(alert != null) {
                    sendAlert(player, alert, event, false);
                }else{
                    log.error("Disable alerts or fill out join-message!");
                }
            }
        }
    }

    public void sendAlert(Player player, String alert, PlayerJoinEvent event, boolean toPlayer){
        alert = hex(alert);
        alert = alert.replace("%PLAYER%", player.getName());
        //Component joinMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(alert);
        //psybounties.audience.sendMessage(joinMessage);
        for(Player others : Bukkit.getOnlinePlayers()){
            if(!others.getUniqueId().equals(player.getUniqueId()))
                others.sendMessage(alert);
        }
        if(toPlayer)
            player.sendMessage(alert);
    }

    public String hex(String message) {
        Pattern pattern = Pattern.compile("<(#[a-fA-F0-9]{6})>");
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String removeArrows = hexCode.replaceAll("(<|>)", "");
            String replaceSharp = removeArrows.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();

            for (char c : ch) {
                builder.append("&").append(c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
