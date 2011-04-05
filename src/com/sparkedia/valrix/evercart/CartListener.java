package com.sparkedia.valrix.evercart;

import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleListener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class CartListener extends VehicleListener {
	protected EverCart plugin;
	private ArrayList<Chunk> old = new ArrayList<Chunk>();
	
	public CartListener(EverCart plugin) {
		this.plugin = plugin;
	}
	
	public void onVehicleMove(VehicleMoveEvent e) {
		Vehicle v = e.getVehicle();
		if (v instanceof StorageMinecart) {
			Chunk current = v.getLocation().getBlock().getChunk();
			if (!old.contains(current)) {
				old.add(current);
			}
			int range = 1;
			for (int dx = -(range); dx <= range; dx++){
				for (int dz = -(range); dz <= range; dz++){
					Chunk chunk = current.getWorld().getChunkAt(current.getX()+dx, current.getZ()+dz);
					if (!old.contains(chunk)) {
						old.add(chunk);
					}
					if (!current.getWorld().isChunkLoaded(chunk)) {
						plugin.log.info("Loading chunk at: ("+chunk.getX()+", "+chunk.getZ()+')');
						current.getWorld().loadChunk(chunk);
					}
				}
			}
			for (int i = 0; i < old.size(); i++) {
				// If the old chunks are greater than 2 chunks away, unload them
				if (old.get(i).getX() > current.getX()+range || old.get(i).getX() < current.getX()-range) {
					plugin.log.info("Removing old chunk at: ("+old.get(i).getX()+", "+old.get(i).getZ()+')');
					current.getWorld().unloadChunkRequest(old.get(i).getX(), old.get(i).getZ());
					old.remove(i);
				}
				if (old.get(i).getZ() > current.getZ()+range || old.get(i).getZ() < current.getZ()-range) {
					plugin.log.info("Removing old chunk at: ("+old.get(i).getX()+", "+old.get(i).getZ()+')');
					current.getWorld().unloadChunkRequest(old.get(i).getX(), old.get(i).getZ());
					old.remove(i);
				}
			}
		}
	}
}
