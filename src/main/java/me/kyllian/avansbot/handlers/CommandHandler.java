package me.kyllian.avansbot.handlers;

import me.kyllian.avansbot.AvansBotPlugin;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channel;

public class CommandHandler {

    private AvansBotPlugin plugin;
    private JDA bot;
    private URL url;

    public CommandHandler(AvansBotPlugin plugin, JDA bot) {
        this.plugin = plugin;
        this.bot = bot;
        try {
            url = new URL("https://discord-mem.es/api/action/get_meme");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
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

    public void handleUpdateRanksCommand(Member member, TextChannel textChannel) {
        if (!checkAndHandleAdmin(member, textChannel)) return;
        textChannel.getIterableHistory().forEach(message -> {
            if (!message.getId().equals("630114888299053067")) return;
            plugin.getBotHandler().getRoleEmotes().keySet().forEach(emoji -> {
                message.addReaction(emoji).queue();
            });
        });
    }

    public void handleSendMemeCommand(Member member, TextChannel textChannel) {
        new BukkitRunnable() {
            public void run() {
                try {
                    URLConnection connection = url.openConnection();
                    connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    JSONObject object = (JSONObject) new JSONParser().parse(readAll(bufferedReader));
                    String currentTitle = (String) object.get("title");

                    URL newURL = new URL((String) object.get("url"));
                    URLConnection connection1 = newURL.openConnection();
                    connection1.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                    BufferedImage image = ImageIO.read(connection1.getInputStream());
                    ImageIO.write(image, "png", new File(plugin.getDataFolder().getPath() + "\\meme.png"));
                    textChannel.sendMessage(currentTitle).addFile(new File(plugin.getDataFolder().getPath() + "\\meme.png")).queue();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
