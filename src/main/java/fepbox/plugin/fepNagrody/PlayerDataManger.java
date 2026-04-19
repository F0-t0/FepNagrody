package fepbox.plugin.fepNagrody;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerDataManger {
    private final JavaPlugin plugin;
    private File file;
    private FileConfiguration config;

    public PlayerDataManger(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        file = new File(plugin.getDataFolder(), "Players.yml");

        if (!file.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);

        if (!config.contains("players")) {
            config.set("players", new ArrayList<String>());
            save();
        }
    }

    public List<String> getPlayers() {
        return config.getStringList("players");
    }

    public void addPlayer(String nick) {
        List<String> players = config.getStringList("players");
        if (!players.contains(nick)) {
            players.add(nick);
            config.set("players", players);
            save();
        }
    }

    public boolean containsPlayer(String nick) {
        return config.getStringList("players").contains(nick);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
