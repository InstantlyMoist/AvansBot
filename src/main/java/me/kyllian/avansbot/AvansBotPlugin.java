package me.kyllian.avansbot;

import me.kyllian.avansbot.handlers.BotHandler;
import me.kyllian.avansbot.handlers.CommandHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class AvansBotPlugin extends JavaPlugin {

    private BotHandler botHandler;
    private CommandHandler commandHandler;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        botHandler = new BotHandler(this);
    }


    @Override
    public void onDisable() {
        botHandler.logout();
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public void setCommandHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }
}
