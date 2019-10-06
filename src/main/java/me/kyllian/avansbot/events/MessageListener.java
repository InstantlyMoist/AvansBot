package me.kyllian.avansbot.events;

import me.kyllian.avansbot.AvansBotPlugin;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;

public class MessageListener extends ListenerAdapter {

    private AvansBotPlugin plugin;

    public MessageListener(AvansBotPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String rawContent = event.getMessage().getContentRaw();
        boolean hasAdmin = event.getMember().hasPermission(Permission.ADMINISTRATOR);
        if (rawContent.startsWith("!setPlaying ")) plugin.getCommandHandler().handleSetPlayingCommand(rawContent, event.getMember(), event.getTextChannel());
        if (rawContent.startsWith("!debugEmote ")) plugin.getCommandHandler().handleDebugEmoteCommand(rawContent, event.getTextChannel());
        if (rawContent.startsWith("!throwRanks")) plugin.getCommandHandler().handleThrowRanksCommand(event.getMember(), event.getTextChannel());
    }
}
