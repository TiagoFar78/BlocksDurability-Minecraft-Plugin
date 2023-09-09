package org.pombacraft.blocksdurability.managers;

import java.util.Hashtable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.pombacraft.blocksdurability.objects.BlockDurability;

public class BlocksDurabilityManager {
	
	private static Hashtable<String, BlockDurability> blocks = new Hashtable<String, BlockDurability>();
	
	public static int getDurability(Location loc) {
		BlockDurability block = blocks.get(getId(loc));
		if (block == null) {
			return -1;
		}
		
		return block.getDurability();
	}
	
	public static void detectedExplosion(Block worldBlock) {
		Location loc = worldBlock.getLocation();
		Material type = worldBlock.getType();
		
		ConfigManager configManager = ConfigManager.getInstance();
		if (configManager.getDurability(type) == -1) {
			return;
		}
		
		String id = getId(loc);		
		
		BlockDurability block = blocks.get(id);
		if (block == null) {
			block = new BlockDurability(id, type);
			blocks.put(id, block);
		}
		
		if (block.exploded()) {
			block.deleteBlockDurability();
			worldBlock.setType(Material.AIR);
		}
	}
	
	public static void add(Location loc, Material type) {
		String id = getId(loc);
		blocks.put(id, new BlockDurability(id, type));
	}
	
	public static void remove(String id) {
		blocks.remove(id);
	}
	
	private static String getId(Location loc) {
		return loc.getWorld().getName() + loc.getBlockX() + loc.getBlockY() + loc.getBlockZ();
	}

}
