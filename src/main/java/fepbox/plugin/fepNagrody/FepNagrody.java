package fepbox.plugin.fepNagrody;

import fepbox.plugin.fepNagrody.listeners.DiscordListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class FepNagrody extends JavaPlugin {
    private JDA jda;
    @Override
    public void onEnable() {
        saveDefaultConfig();

        int pluginId = 30898;
        Metrics metrics = new Metrics(this, pluginId);

        PlayerDataManger playerDataManger = new PlayerDataManger(this);
        playerDataManger.setup();

        String token = getConfig().getString("bot-token");
        String channelid = getConfig().getString("discord-channel-id");

        if (token == null || token.isBlank() || "exampletoken".equals(token)
                || channelid == null || channelid.isBlank() || "exampleid".equals(channelid)) {
            getLogger().severe("Token or Channel Id not set");
            getServer().getPluginManager().disablePlugin(this);
        } else {
            try {
                jda = JDABuilder.createDefault(token)
                        .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                        .addEventListeners(new DiscordListener(this, channelid, playerDataManger))
                        .build();
            } catch (Exception e) {
                getLogger().severe("Nie udalo sie uruchomic bota Discord: " + e.getMessage());
                e.printStackTrace();
                getServer().getPluginManager().disablePlugin(this);
            }
        }
    }

    @Override
    public void onDisable() {
        if (jda != null) {
            jda.shutdown();
        }
        getLogger().info("FepNagrody disabled");
    }

    public JDA getJDA() {
        return jda;
    }
}
