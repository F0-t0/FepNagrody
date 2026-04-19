package fepbox.plugin.fepNagrody.listeners;

import fepbox.plugin.fepNagrody.FepNagrody;
import fepbox.plugin.fepNagrody.PlayerDataManger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DiscordListener extends ListenerAdapter {
    private final FepNagrody plugin;
    private final PlayerDataManger playerDataManger;
    private final String channelid;

    public DiscordListener(FepNagrody plugin, String channelid, PlayerDataManger playerDataManger) {
        this.plugin = plugin;
        this.channelid = channelid;
        this.playerDataManger = playerDataManger;
    }

    private String c(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {return;}
        if (event.isWebhookMessage()) {return;}
        if (!event.isFromGuild()) {return;}

        String channelid = event.getChannel().getId();
        if (!channelid.equals(this.channelid)) {return;}

        String content = event.getMessage().getContentRaw();
        List<String> commands = plugin.getConfig().getStringList("commands");
        TextChannel channel = plugin.getJDA().getTextChannelById(channelid);

        RewardResult result;
        try {
            result = Bukkit.getScheduler().callSyncMethod(plugin, () -> handleOnMainThread(content, commands)).get();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            plugin.getLogger().warning("Przerwano obsluge wiadomosci Discord.");
            return;
        } catch (ExecutionException exception) {
            plugin.getLogger().severe("Nie udalo sie obsluzyc wiadomosci Discord: " + exception.getCause());
            exception.getCause().printStackTrace();
            return;
        }

        if (channel == null) {
            return;
        }

        switch (result) {
            case ALREADY_RECEIVED -> channel.sendMessageEmbeds(buildReceivedEmbed()).queue();
            case SUCCESS -> channel.sendMessageEmbeds(buildSuccessEmbed()).queue();
            case NOT_ONLINE -> channel.sendMessageEmbeds(buildNotOnlineEmbed()).queue();
        }
    }

    private RewardResult handleOnMainThread(String content, List<String> commands) {
        Player matchedPlayer = null;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getName().equalsIgnoreCase(content)) {
                matchedPlayer = onlinePlayer;
                break;
            }
        }

        if (matchedPlayer == null) {
            return RewardResult.NOT_ONLINE;
        }

        if (playerDataManger.containsPlayer(matchedPlayer.getName())) {
            return RewardResult.ALREADY_RECEIVED;
        }

        for (String cmd : commands) {
            String parsedCommand = cmd.replace("%player%", matchedPlayer.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parsedCommand);
        }

        playerDataManger.addPlayer(matchedPlayer.getName());

        String broadcast = plugin.getConfig().getString("broadcast-message");
        if (broadcast != null) {
            Bukkit.broadcast(Component.text(c(broadcast.replace("%player%", matchedPlayer.getName()))));
        }

        return RewardResult.SUCCESS;
    }

    private MessageEmbed buildReceivedEmbed() {
        String embedTitle = plugin.getConfig().getString("embed-recieved.title", "Otrzymales juz nagrode!");
        String embedColorS = plugin.getConfig().getString("embed-recieved.color", "#0DD939");
        List<String> descriptionList = plugin.getConfig().getStringList("embed-recieved.description");
        if (descriptionList.isEmpty()) {
            descriptionList = List.of("Wlasnie odebrales nagrode za discord!", "Za chwile powinienes otrzymac nagrode w Minecraft!");
        }

        return buildEmbed(
                embedTitle,
                String.join("\n", descriptionList),
                parseColor(embedColorS, "#0DD939"),
                plugin.getConfig().getString("embed-recieved.thumbnail")
        );
    }

    private MessageEmbed buildSuccessEmbed() {
        String embedTitle = plugin.getConfig().getString("embed-Success.title", "Otrzymales juz nagrode!");
        String embedColorS = plugin.getConfig().getString("embed-Success.color", "#E31E1E");
        List<String> descriptionList = plugin.getConfig().getStringList("embed-Success.description");
        if (descriptionList.isEmpty()) {
            descriptionList = List.of("Otrzymales juz nagrode!", "Nie mozesz odebrac jej kilka razy.");
        }

        return buildEmbed(
                embedTitle,
                String.join("\n", descriptionList),
                parseColor(embedColorS, "#E31E1E"),
                plugin.getConfig().getString("embed-Success.thumbnail")
        );
    }

    private MessageEmbed buildNotOnlineEmbed() {
        String embedTitle = plugin.getConfig().getString("embed-notOnline.title", "Otrzymales juz nagrode!");
        String embedColorS = plugin.getConfig().getString("embed-notOnline.color", "#E31E1E");
        List<String> descriptionList = plugin.getConfig().getStringList("embed-notOnline.description");
        if (descriptionList.isEmpty()) {
            descriptionList = List.of("Nie ma takiego gracza na serwerze!", "Upewnij sie, ze dobrze wpisales nick!");
        }

        return buildEmbed(
                embedTitle,
                String.join("\n", descriptionList),
                parseColor(embedColorS, "#E31E1E"),
                plugin.getConfig().getString("embed-notOnline.thumbnail")
        );
    }

    private MessageEmbed buildEmbed(String title, String description, Color color, String thumbnail) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(title);
        eb.setDescription(description);
        eb.setColor(color);
        if (thumbnail != null) {
            eb.setThumbnail(thumbnail);
        }
        return eb.build();
    }

    private Color parseColor(String rawColor, String fallbackColor) {
        String color = rawColor == null || rawColor.isBlank() ? fallbackColor : rawColor.trim();

        if (!color.startsWith("#") && !color.startsWith("0x") && color.matches("[0-9a-fA-F]{6}")) {
            color = "#" + color;
        }

        try {
            return Color.decode(color);
        } catch (NumberFormatException exception) {
            plugin.getLogger().warning("Niepoprawny kolor w configu: " + rawColor + ". Uzywam " + fallbackColor);
            return Color.decode(fallbackColor);
        }
    }

    private enum RewardResult {
        ALREADY_RECEIVED,
        SUCCESS,
        NOT_ONLINE
    }
}
