package me.kyllian.avansbot.handlers;

import me.kyllian.avansbot.AvansBotPlugin;
import me.kyllian.avansbot.events.MessageListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class BotHandler {

    private AvansBotPlugin plugin;

    private JDA bot;

    private String token;
    private String status;

    public BotHandler(AvansBotPlugin plugin) {
        this.plugin = plugin;

        this.token = plugin.getConfig().getString("Token");
        this.status = plugin.getConfig().getString("Status");

        login();
    }

    public void login() {
        try {
            bot = new JDABuilder(token).build();
            bot.awaitReady();
        } catch (LoginException exception) {
            exception.printStackTrace();
            plugin.getPluginLoader().disablePlugin(plugin);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
            plugin.getPluginLoader().disablePlugin(plugin);
        }
        bot.getPresence().setActivity(Activity.playing(status));
        bot.addEventListener(new MessageListener(plugin));
        plugin.setCommandHandler(new CommandHandler(plugin, bot));
    }

    public void logout() {
        bot.shutdownNow();
    }
}
