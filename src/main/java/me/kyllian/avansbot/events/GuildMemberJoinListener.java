package me.kyllian.avansbot.events;

import me.kyllian.avansbot.AvansBotPlugin;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMemberJoinListener extends ListenerAdapter {

    private AvansBotPlugin plugin;

    public GuildMemberJoinListener(AvansBotPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        event.getGuild().getTextChannelById("630105079298654261")
                .sendMessage("Welkom <@" + event.getMember().getId() + "> vergeet geen rank te kiezen in <#630113106982469645>").queue();

    }
}
