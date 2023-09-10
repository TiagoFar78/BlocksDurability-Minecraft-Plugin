package org.pombacraft.blocksdurability.managers;

import java.util.Hashtable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.pombacraft.blocksdurability.objects.BlockDurability;

public class BlocksDurabilityManager {
	
	private static Hashtable<String, BlockDurability> blocks = new Hashtable<String, BlockDurability>();
	
	public static int getDurability(Block block) {
		BlockDurability blockDurability = blocks.get(getId(block.getLocation()));
		if (blockDurability == null) {
			return ConfigManager.getInstance().getDurability(block.getType());
		}
		
		return blockDurability.getDurability();
	}
	
	public static int detectedExplosion(Block worldBlock) {
		Location loc = worldBlock.getLocation();
		Material type = worldBlock.getType();
		
		ConfigManager configManager = ConfigManager.getInstance();
		if (configManager.getDurability(type) == -1) {
			return -1;
		}
		
		String id = getId(loc);		
		
		BlockDurability block = blocks.get(id);
		if (block == null) {
			block = new BlockDurability(id, type, true);
			blocks.put(id, block);
		}
		
		if (block.exploded()) {
			block.delete();
			worldBlock.setType(Material.AIR);
		}
		
		return 0;
	}
	
	public static void add(Location loc, Material type) {
		String id = getId(loc);
		blocks.put(id, new BlockDurability(id, type, false));
	}
	
	public static void remove(String id) {
		blocks.remove(id);
	}
	
	private static String getId(Location loc) {
		return loc.getWorld().getName() + loc.getBlockX() + loc.getBlockY() + loc.getBlockZ();
	}

}
