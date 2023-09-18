package org.pombacraft.blocksdurability.managers;

import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.pombacraft.blocksdurability.BlocksDurability;

public class ConfigManager {
	
	private static ConfigManager instance = new ConfigManager();
	
	public static ConfigManager getInstance() {
		return instance;
	}
	
	private int _regenerationDelay;
	private int _lastBedrockY;
	private Hashtable<Material, Integer> _materialsDurability;
	private String _NBTTag;
	private String _durabilityMessage;
	
	private ConfigManager() {
		YamlConfiguration config = BlocksDurability.getYamlConfiguration();
		
		_regenerationDelay = config.getInt("RegenerationDelay");
		_lastBedrockY = config.getInt("LastBedrockY");
		_materialsDurability = new Hashtable<Material, Integer>();
		populateMaterialsDurability(config);
		_NBTTag = config.getString("ItemNBTTag");
		_durabilityMessage = config.getString("DurabilityMessage");
	}
	
	private void populateMaterialsDurability(YamlConfiguration config) {
		String path = "Blocks.";
		
		List<String> blocksPath = config.getKeys(true).stream().filter(p -> p.startsWith(path) && p.lastIndexOf(".") == path.length() - 1).collect(Collectors.toList());
		for (String blockPath : blocksPath) {
			String name = config.getString(blockPath + ".Name");
			int durability = config.getInt(blockPath + ".Durability");
			
			Material blockType = Material.getMaterial(name);
			
			_materialsDurability.put(blockType, durability);
		}
		
	}
	
	public int getRegenerationDelay() {
		return _regenerationDelay;
	}
	
	public int getLastBedrockY() {
		return _lastBedrockY;
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
