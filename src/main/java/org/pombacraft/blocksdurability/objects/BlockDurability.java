package org.pombacraft.blocksdurability.objects;

import org.bukkit.Material;

public class BlockDurability {
	
	private Material _type;
	private int _durability;
	
	public BlockDurability(Material type) {
		_type = type;
		_durability = 0; // TODO
	}
	
	public int getDurability() {
		return _durability;
	}
	
	public int getMaxDurability() {
		return 0; // TODO
	}

}
