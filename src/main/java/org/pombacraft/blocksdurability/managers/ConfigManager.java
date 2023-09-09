package org.pombacraft.blocksdurability.managers;

import java.util.Hashtable;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.pombacraft.blocksdurability.BlocksDurability;

public class ConfigManager {
	
	private static ConfigManager instance = new ConfigManager();
	
	public static ConfigManager getInstance() {
		return instance;
	}
	
	private int _regenerationDelay;
	private Hashtable<Material, Integer> _materialsDurability;
	private String _NBTTag;
	private String _durabilityMessage;
	
	private ConfigManager() {
		YamlConfiguration config = BlocksDurability.getYamlConfiguration();
		
		_regenerationDelay = config.getInt("RegenerationDelay");
		_materialsDurability = new Hashtable<Material, Integer>();
		_materialsDurability.put(Material.END_STONE, config.getInt("Endstone"));
		_materialsDurability.put(Material.OBSIDIAN, config.getInt("Obsidian"));
		_materialsDurability.put(Material.BEDROCK, config.getInt("Bedrock"));
		_NBTTag = config.getString("ItemNBTTag");
		_durabilityMessage = config.getString("DurabilityMessage");
	}
	
	public int getRegenerationDelay() {
		return _regenerationDelay;
	}
	
	public int getDurability(Material type) {
		Integer durability = _materialsDurability.get(type);
		if (durability == null) {
			return -1;
		}
		
		return durability;
	}
	
	public String getNBTTag() {
        return _NBTTag;
    }

    public String getDurabilityMessage() {
        return _durabilityMessage;
    }

}
