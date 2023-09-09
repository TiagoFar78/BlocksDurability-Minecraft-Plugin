package org.pombacraft.blocksdurability;

import java.util.List;

import org.bukkit.Material;
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

public class Events implements Listener {
	
	@EventHandler
    public void onBlockExplode(BlockExplodeEvent e) {
        handleExplosion(e.blockList());
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        handleExplosion(e.blockList());
    }

	private void handleExplosion(List<Block> blocks) {
    	for (Block block : blocks) {
    		BlocksDurabilityManager.detectedExplosion(block);
    	}
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
		
		if (itemMeta.getAsString().contains(configManager.getNBTTag())) {
			int currentDurability = BlocksDurabilityManager.getDurability(block.getLocation());
					
			String message = configManager.getDurabilityMessage()
					.replace("&", "§")
					.replace("{CURRENT}", Integer.toString(currentDurability))
					.replace("{MAX}", Integer.toString(blockMaxDurability));
			
			player.sendMessage(message);
		}
	}
	
}
