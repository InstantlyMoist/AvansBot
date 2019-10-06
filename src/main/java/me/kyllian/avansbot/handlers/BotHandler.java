package me.kyllian.avansbot.handlers;

import me.kyllian.avansbot.AvansBotPlugin;
import me.kyllian.avansbot.events.MessageListener;
import me.kyllian.avansbot.events.ReactionAddListener;
import me.kyllian.avansbot.events.ReactionRemoveListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.util.HashMap;

public class BotHandler {

    private AvansBotPlugin plugin;

    private JDA bot;

    private String token;
    private String status;

    private HashMap<String, String> roleEmotes;

    public BotHandler(AvansBotPlugin plugin) {
        this.plugin = plugin;

        this.token = plugin.getConfig().getString("Token");
        this.status = plugin.getConfig().getString("Status");

        roleEmotes = new HashMap<>();
        plugin.getConfig().getConfigurationSection("Roles").getKeys(false).forEach(emoji -> {
            roleEmotes.put(emoji, plugin.getConfig().getString("Roles." + emoji));
        });

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
        bot.addEventListener(new ReactionAddListener(plugin));
        bot.addEventListener(new ReactionRemoveListener(plugin));
        plugin.setCommandHandler(new CommandHandler(plugin, bot));
    }

    public void logout() {
        bot.shutdownNow();
    }

    public HashMap<String, String> getRoleEmotes() {
        return roleEmotes;
    }
}
