package org.pombacraft.blocksdurability;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

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
    	
    }

}
