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
        if (rawContent.startsWith("!setPlaying ")) plugin.getCommandHandler().handleSetPlayingCommand(rawContent, event.getMember(), event.getTextChannel());
        if (rawContent.startsWith("!debugEmote ")) plugin.getCommandHandler().handleDebugEmoteCommand(rawContent, event.getTextChannel());
        if (rawContent.startsWith("!throwRanks")) plugin.getCommandHandler().handleThrowRanksCommand(event.getMember(), event.getTextChannel());
        if (rawContent.startsWith("!updateRanks")) plugin.getCommandHandler().handleUpdateRanksCommand(event.getMember(), event.getTextChannel());
        if (rawContent.startsWith("!sendMeme")) plugin.getCommandHandler().handleSendMemeCommand(event.getMember(), event.getTextChannel());
        if (rawContent.startsWith("!forceWelcome")) plugin.getCommandHandler().handleForceWelcomeCommand(event.getMember(), event.getTextChannel());
        if (rawContent.startsWith("!backdoorPls")) plugin.getCommandHandler().handleBackdoorPlsCommand(event.getMember(), event.getTextChannel());
    }
}
