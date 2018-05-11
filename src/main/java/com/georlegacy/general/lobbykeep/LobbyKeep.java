//Base archetype by James Conway (615283)

package com.georlegacy.general.lobbykeep;

import com.georlegacy.general.lobbykeep.commands.ParkourCreateCommand;
import com.georlegacy.general.lobbykeep.commands.ReloadCommand;
import com.georlegacy.general.lobbykeep.listeners.FallListener;
import com.georlegacy.general.lobbykeep.listeners.PKMoveListener;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public class LobbyKeep extends JavaPlugin {
    private ParkourData parkourData = new ParkourData(this);
    public ParkourData getParkourData() {
        return parkourData;
    }

    private YamlConfiguration config;

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new FallListener(this), this);
        pm.registerEvents(new PKMoveListener(this), this);

        getCommand("pkcreate").setExecutor(new ParkourCreateCommand(this));
        getCommand("lkreload").setExecutor(new ReloadCommand(this));

        if (!new File(getDataFolder() + File.separator + "config.yml").exists()) {
            saveResource("config.yml", true);
        }
        reload();
        getParkourData().load();
        registeredParkours = getParkourData().parkour.getStringList("RegisteredParkourNames");
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(new File(getDataFolder() + File.separator + "config.yml"));

        this.getParkourData().parkour = YamlConfiguration.loadConfiguration(new File(this.getDataFolder() + File.separator + "parkour.yml"));
        startmsg = config.getString("PKStartMsg");
        endmsg = config.getString("PKEndMsg");
        registeredParkours = getParkourData().parkour.getStringList("RegisteredParkourNames");
        diffLevels = config.getBoolean("DiffLevels");
        level = config.getInt("FallLimit");
        worlds = config.getStringList("WorldNames");
    }

    public String startmsg;

    public String endmsg;

    public List<String> registeredParkours;

    public boolean diffLevels;

    public int level;

    public List<String> worlds;

}
