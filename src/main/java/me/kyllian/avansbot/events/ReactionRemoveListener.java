package me.kyllian.avansbot.events;

import me.kyllian.avansbot.AvansBotPlugin;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReactionRemoveListener extends ListenerAdapter {

    private AvansBotPlugin plugin;

    public ReactionRemoveListener(AvansBotPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        if (event.getMember().getUser().isBot()) return;
        if (!event.getMessageId().equalsIgnoreCase("630114888299053067")) return;
        String foundRank = plugin.getBotHandler().getRoleEmotes().get(event.getReactionEmote().getAsCodepoints());
        if (foundRank == null) return;
        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(foundRank)).queue();
    }
}
