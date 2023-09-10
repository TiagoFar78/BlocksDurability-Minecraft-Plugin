package org.pombacraft.blocksdurability.objects;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitTask;
import org.pombacraft.blocksdurability.BlocksDurability;
import org.pombacraft.blocksdurability.managers.BlocksDurabilityManager;
import org.pombacraft.blocksdurability.managers.ConfigManager;

public class BlockDurability {
	
	private static final int TICKS_PER_SECOND = 20;
	
	private String _id;
	private Material _type;
	private int _durability;
	private BukkitTask _task;
	
	public BlockDurability(String id, Material type, boolean isFullDurability) {
		_id = id;
		_type = type;
		_durability = isFullDurability ? getMaxDurability() : 1;
		
		regenerate();
	}
	
	public int getDurability() {
		return _durability;
	}
	
	public int getMaxDurability() {
		return ConfigManager.getInstance().getDurability(_type);
	}
	
	public boolean exploded() {
		_durability--;		
		return _durability == 0;
	}
	
	private void regenerate() {
		ConfigManager configManager = ConfigManager.getInstance();
		int delay = configManager.getRegenerationDelay();
		
		_task = Bukkit.getScheduler().runTaskLater(BlocksDurability.getBlocksDurability(), new Runnable() {
			
			@Override
			public void run() {
				_durability++;
				
				if (_durability == getMaxDurability()) {
					delete();
				}
				else {
					regenerate();
				}
			}
		}, delay*TICKS_PER_SECOND);
	}
	
	public void delete() {
		if (_task != null) {
			_task.cancel();
		}
		
		BlocksDurabilityManager.remove(_id);
	}

}
