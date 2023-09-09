package org.pombacraft.blocksdurability;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class BlocksDurability extends JavaPlugin {
	
	@Override
	public void onEnable() {		
		if (!new File(getDataFolder(), "config.yml").exists()) {
			saveDefaultConfig();
		}
		
		getServer().getPluginManager().registerEvents(new Events(), this);
	}
	
	public static YamlConfiguration getYamlConfiguration() {
		return YamlConfiguration.loadConfiguration(configFile());
	}
	
	private static File configFile() {
		return new File(getBlocksDurability().getDataFolder(), "config.yml");
	}
	
	public static BlocksDurability getBlocksDurability() {
		return (BlocksDurability)Bukkit.getServer().getPluginManager().getPlugin("TF_BlocksDurability");
	}
	
	public static void saveConfiguration(YamlConfiguration config) {
		File configFile = configFile();
		
		try {
			config.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	
	
}
