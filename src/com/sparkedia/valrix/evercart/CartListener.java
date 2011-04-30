package com.sparkedia.valrix.evercart;

import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.event.vehicle.VehicleListener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class CartListener extends VehicleListener {
	protected EverCart plugin;
	private ArrayList<Chunk> old = new ArrayList<Chunk>();

	public CartListener(EverCart plugin) {
		this.plugin = plugin;
	}
	
	public void onVehicleBlockCollision(VehicleBlockCollisionEvent e) {
		Vehicle v = e.getVehicle();
		if (v instanceof StorageMinecart) {
			// Remove all the old chunks in the list upon collide
			if (old.size() > 0) {
				for (int i = 0; i < old.size(); i++) {
					Chunk c = old.get(i);
					c.getWorld().unloadChunkRequest(c.getX(), c.getZ());
				}
				old.clear();
			}
		}
	}

	public void onVehicleMove(VehicleMoveEvent e) {
		Vehicle v = e.getVehicle();
		if (v instanceof StorageMinecart) {
			// Get current chunk
			Chunk current = v.getLocation().getBlock().getChunk();
			// Set range that we want to keep the cart alive at.
			int range = 2;
			// Load in new chunks as we get to them.
			for (int dx = -(range); dx <= range; dx++){
				for (int dz = -(range); dz <= range; dz++){
					Chunk chunk = current.getWorld().getChunkAt(current.getX()+dx, current.getZ()+dz);
					// Only load in chunks that are not already loaded
					chunk.getWorld().loadChunk(chunk);
					if (!old.contains(chunk)) {
						//plugin.log.info("Loading chunk at: ("+chunk.getX()+", "+chunk.getZ()+')');
						old.add(chunk);
					}
				}
			}
			// Now check to see if we need to unload any chunks
			for (int i = 0; i < old.size(); i++) {
				Chunk oc = old.get(i);
				if (oc.getWorld() != current.getWorld() ||
				    oc.getX() > current.getX()+range ||
				    oc.getX() < current.getX()-range ||
				    oc.getZ() > current.getZ()+range ||
				    oc.getZ() < current.getZ()-range) {
					//plugin.log.info("Removing old chunk at: ("+oc.getX()+", "+oc.getZ()+')');
					oc.getWorld().unloadChunkRequest(oc.getX(), oc.getZ());
					old.remove(oc);
				}
			}
		}
	}
}
