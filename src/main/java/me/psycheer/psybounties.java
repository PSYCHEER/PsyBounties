package me.psycheer;

import net.milkbowl.vault.chat.Chat;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class psybounties extends JavaPlugin {

    public static Chat chat;
    public static psybounties instance;
    public static Server server;
    public FileConfiguration config;

    @Override
    public void onEnable() {
        setupChat();
        if (!setupChat() ) {
            getLogger().severe("- Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        InfoLogger();
        instance = this;
        server = this.getServer();
        Bukkit.getPluginManager().registerEvents(new psyevents(), instance);
        saveDefaultConfig();
        config = instance.getConfig();
    }

    @Override
    public void onDisable() {
    }

    public static psybounties getInstance() {
        return instance;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            return false;
        }
        chat = rsp.getProvider();
        return chat != null;
    }

    public static Chat getChat() {
        return chat;
    }

    public void InfoLogger(){
        String chatPl = chat.getName();
        String pluginName = this.getName();
        String version = getServer().getBukkitVersion();
        getLogger().info("Running " + pluginName);
        getLogger().info("Hooked into " + chatPl + "!");
        getLogger().info("Server version: " + version);
    }
}
