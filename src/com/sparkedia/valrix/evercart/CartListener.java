package com.sparkedia.valrix.evercart;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleListener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class CartListener extends VehicleListener {
	private EverCart plugin;
	
	public CartListener(EverCart plugin) {
		this.plugin = plugin;
	}
	
	public void onVehicleMove(VehicleMoveEvent e) {
		Vehicle v = e.getVehicle();
		if (v instanceof StorageMinecart) {
			World w = v.getWorld();
			Location from = e.getFrom();
			Location to = e.getTo();
			Chunk c = w.getChunkAt(v.getLocation());
			if (to.getBlockX() > from.getBlockX()) {
				// Moving across X-axis positively, load up next positive X and both Z-axis chunks
				plugin.log.info("Moving across positive X");
				int x = c.getX();
				int z = c.getZ();
				// Get the chunks that we want to check on
				Chunk X = w.getChunkAt((x+1), z);
				Chunk Z0 = w.getChunkAt(x, (z-1));
				Chunk Z1 = w.getChunkAt(x, (z+1));
				// Check if they're already loaded, if not then load it
				if (!w.isChunkLoaded(X)) {
					w.loadChunk(X);
					plugin.log.info("Loading chunk at: ("+X.getX()+", "+X.getZ()+')');
				}
				if (!w.isChunkLoaded(Z0)) {
					w.loadChunk(Z0);
					plugin.log.info("Loading chunk at: ("+Z0.getX()+", "+Z0.getZ()+')');
				}
				if (!w.isChunkLoaded(Z1)) {
					w.loadChunk(Z1);
					plugin.log.info("Loading chunk at: ("+Z1.getX()+", "+Z1.getZ()+')');
				}
				// Request to unload the previous chunk
				if (w.isChunkLoaded(x-1, z)) {
					w.unloadChunkRequest((x-1), z);
					plugin.log.info("Un-loading chunk at: ("+(x-1)+", "+z+')');
				}
			} else if (to.getBlockX() < from.getBlockX()) {
				plugin.log.info("Moving across negative X");
				// Moving across X-axis negatively, load up next negative X and both Z-axis chunks
				int x = c.getX();
				int z = c.getZ();
				// Get the chunks that we want to check on
				Chunk X = w.getChunkAt((x-1), z);
				Chunk Z0 = w.getChunkAt(x, (z-1));
				Chunk Z1 = w.getChunkAt(x, (z+1));
				// Check if they're already loaded, if not then load it
				if (!w.isChunkLoaded(X)) {
					w.loadChunk(X);
					plugin.log.info("Loading chunk at: ("+X.getX()+", "+X.getZ()+')');
				}
				if (!w.isChunkLoaded(Z0)) {
					w.loadChunk(Z0);
					plugin.log.info("Loading chunk at: ("+Z0.getX()+", "+Z0.getZ()+')');
				}
				if (!w.isChunkLoaded(Z1)) {
					w.loadChunk(Z1);
					plugin.log.info("Loading chunk at: ("+Z1.getX()+", "+Z1.getZ()+')');
				}
				// Request to unload the previous chunk
				if (w.isChunkLoaded((x+1), z)) {
					w.unloadChunkRequest((x+1), z);
					plugin.log.info("Un-loading chunk at: ("+(x+1)+", "+z+')');
				}
			} else if (to.getBlockZ() > from.getBlockZ()) {
				// Moving across Z-axis positively, load up next positive Z and both X-axis chunks
				int x = c.getX();
				int z = c.getZ();
				// LOG
				plugin.log.info("Moving across positive Z, currently at: ("+x+", "+z+')');
				// Get the chunks that we want to check on
				Chunk X0 = w.getChunkAt((x-1), z);
				Chunk X1 = w.getChunkAt((x+1), z);
				// Check if they're already loaded, if not then load it
				if (!w.isChunkLoaded(w.getChunkAt(to))) {
					w.loadChunk(w.getChunkAt(to));
					plugin.log.info("Loading current chunk.");
				}
				if (!w.isChunkLoaded(w.getChunkAt(x, (z+1)))) {
					w.loadChunk(w.getChunkAt(x, (z+1)));
					plugin.log.info("Loading next chunk");
				}
				if (!w.isChunkLoaded(X0)) {
					w.loadChunk(X0);
					plugin.log.info("Loading chunk at: ("+X0.getX()+", "+X0.getZ()+')');
				}
				if (!w.isChunkLoaded(X1)) {
					w.loadChunk(X1);
					plugin.log.info("Loading chunk at: ("+X1.getX()+", "+X1.getZ()+')');
				}
				w.loadChunk(x, z);
				w.refreshChunk(x, z);
				// Request to unload the previous chunk
				//if (w.isChunkLoaded(x, (z-1))) {
					//w.unloadChunkRequest(x, (z-1));
					//w.refreshChunk(x, (z-1));
					//plugin.log.info("Un-loading chunk at: ("+x+", "+(z-1)+')');
				//}
			} else if (to.getBlockZ() < from.getBlockZ()) {
				plugin.log.info("Moving across negative Z");
				// Moving across Z-axis negatively, load up next negative Z and both X-axis chunks
				int x = c.getX();
				int z = c.getZ();
				// Get the chunks that we want to check on
				Chunk Z = w.getChunkAt(x, (z-1));
				Chunk X0 = w.getChunkAt((x-1), z);
				Chunk X1 = w.getChunkAt((x+1), z);
				// Check if they're already loaded, if not then load it
				if (!w.isChunkLoaded(Z)) {
					w.loadChunk(Z);
					plugin.log.info("Loading chunk at: ("+Z.getX()+", "+Z.getZ()+')');
				}
				if (!w.isChunkLoaded(X0)) {
					w.loadChunk(X0);
					plugin.log.info("Loading chunk at: ("+X0.getX()+", "+X0.getZ()+')');
				}
				if (!w.isChunkLoaded(X1)) {
					w.loadChunk(X1);
					plugin.log.info("Loading chunk at: ("+X1.getX()+", "+X1.getZ()+')');
				}
				// Request to unload the previous chunk
				if (w.isChunkLoaded(x, (z+1))) {
					w.unloadChunkRequest(x, (z+1));
					plugin.log.info("Un-loading chunk at: ("+x+", "+(z+1)+')');
				}
			}
		}
	}
}
