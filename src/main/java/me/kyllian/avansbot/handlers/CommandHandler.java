package me.kyllian.avansbot.handlers;

import me.kyllian.avansbot.AvansBotPlugin;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;

import java.nio.channels.Channel;

public class CommandHandler {

    private AvansBotPlugin plugin;
    private JDA bot;

    public CommandHandler(AvansBotPlugin plugin, JDA bot) {
        this.plugin = plugin;
        this.bot = bot;
    }

    public void handleSetPlayingCommand(String fullCommand, Member member, TextChannel channel) {
        if (!checkAndHandleAdmin(member, channel)) return;
        String status = ditchPrefix("!setPlaying ", fullCommand);
        plugin.getConfig().set("Status", status);
        plugin.saveConfig();
        bot.getPresence().setActivity(Activity.playing(status));
    }

    public void handleDebugEmoteCommand(String fullCommand, TextChannel channel) {
        String emote = ditchPrefix("!debugEmote ", fullCommand);
        channel.sendMessage("> Raw text: " + emote);
    }

    public boolean checkAndHandleAdmin(Member member, TextChannel channel) {
        boolean hasAdmin = member.hasPermission(Permission.ADMINISTRATOR);
        if (!hasAdmin) channel.sendMessage("> You don't have permissions for this!").queue();
        return hasAdmin;
    }

    public String ditchPrefix(String prefix, String fullCommand) {
        return fullCommand.replace(prefix, "");
    }

    public void handleThrowRanksCommand(Member member, TextChannel textChannel) {
        if (!checkAndHandleAdmin(member, textChannel)) return;
        StringBuilder builder = new StringBuilder();
        textChannel.getGuild().getRoles().forEach(role -> {
            builder.append("> " + role.getName().replace("@", "") + " ID:" + role.getId()).append("\n");
        });
        textChannel.sendMessage(builder.toString().trim()).queue();
    }
}
