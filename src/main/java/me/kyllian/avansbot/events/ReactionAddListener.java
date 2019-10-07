package me.kyllian.avansbot.events;

import me.kyllian.avansbot.AvansBotPlugin;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;

public class ReactionAddListener extends ListenerAdapter {

    private AvansBotPlugin plugin;

    public ReactionAddListener(AvansBotPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (!event.getMessageId().equalsIgnoreCase("630114888299053067")) return;
        String foundRank = plugin.getBotHandler().getRoleEmotes().get(event.getReactionEmote().getAsCodepoints());
        Bukkit.getLogger().info(event.getReactionEmote().getAsCodepoints() + " emote found");
        if (foundRank == null) return;
        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(foundRank)).queue();
    }
}
