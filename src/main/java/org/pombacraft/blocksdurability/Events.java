package org.pombacraft.blocksdurability;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pombacraft.blocksdurability.managers.BlocksDurabilityManager;
import org.pombacraft.blocksdurability.managers.ConfigManager;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;


public class Events implements Listener {
	
	private static final int EXPLOSION_RADIUS = 3;
	private static final String INFINITE_SYMBOL = "∞";
	
	@EventHandler
    public void onBlockExplode(BlockExplodeEvent e) {
        handleExplosion(e.getBlock().getLocation(), e.blockList());
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        handleExplosion(e.getEntity().getLocation(), e.blockList());
    }

	private void handleExplosion(Location detonatorLoc, List<Block> blocks) {
    	for (Block block : getAffectedBlocks(detonatorLoc)) {
    		if (BlocksDurabilityManager.detectedExplosion(block) == 0) {
    			blocks.remove(block);
    		}
    		
    	}
    }
	
	private List<Block> getAffectedBlocks(Location detonatorLoc) {
		int lastBedrockY = ConfigManager.getInstance().getLastBedrockY();
		
		List<Block> blocks = new ArrayList<Block>();
		
		World world = detonatorLoc.getWorld();
		
		for (int x = -EXPLOSION_RADIUS; x <= EXPLOSION_RADIUS; x++) {
			for (int y = -EXPLOSION_RADIUS; y <= EXPLOSION_RADIUS; y++) {
				if (detonatorLoc.getY() + y <= lastBedrockY) {
					continue;
				}
				
				for (int z = -EXPLOSION_RADIUS; z <= EXPLOSION_RADIUS; z++) {	
					Location targetLoc = new Location(world, detonatorLoc.getX() + x, 
							detonatorLoc.getY() + y, detonatorLoc.getZ() + z);

					if (detonatorLoc.distance(targetLoc) <= EXPLOSION_RADIUS) {
						Block block = world.getBlockAt(targetLoc);
						if (block.getType() != Material.AIR) {
							blocks.add(block);
						}
					}
				}
			}
		}
		
		return blocks;
	}
	
	
	@EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Material type = block.getType();
        
        if (ConfigManager.getInstance().getDurability(type) != -1) {
        	BlocksDurabilityManager.add(block.getLocation(), type);
        }
	}
	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		ConfigManager configManager = ConfigManager.getInstance();
		
		Block block = e.getClickedBlock();
		if (block == null) {
			return;
		}
		
		int blockMaxDurability = configManager.getDurability(block.getType());
		if (blockMaxDurability == -1) {
			return;
		}
		
		Player player = e.getPlayer();
		ItemStack item = player.getItemInHand();
		if (item == null) {
			return;
		}
		
		ItemMeta itemMeta = item.getItemMeta();
		if (itemMeta == null) {
			return;
		}
		
		if (!itemMeta.getAsString().contains(configManager.getNBTTag())) {
			return;
		}
		
		String message = configManager.getDurabilityMessage().replace("&", "§");
		if (block.getLocation().getBlockY() <= configManager.getLastBedrockY()) {
			message = message.replace("{CURRENT}", INFINITE_SYMBOL).replace("{MAX}", INFINITE_SYMBOL);
		}
		else {
			int currentDurability = BlocksDurabilityManager.getDurability(block);
			
			message = message
					.replace("{CURRENT}", Integer.toString(currentDurability))
					.replace("{MAX}", Integer.toString(blockMaxDurability));
		}
		
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
	}
	
}
