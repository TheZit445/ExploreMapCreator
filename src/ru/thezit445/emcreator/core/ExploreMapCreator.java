package ru.thezit445.emcreator.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import ru.thezit445.emcreator.EMCreatorPlugin;
import ru.thezit445.emcreator.utils.FileManager;
import ru.thezit445.nbtreflectionapi.nbt.NBTTagCompound;
import ru.thezit445.nbtreflectionapi.nbt.NBTTagList;
import ru.thezit445.nbtreflectionapi.nms.NMSItem;
import ru.thezit445.nbtreflectionapi.util.NBTUtils;

/**
 * <i>Created on 06.04.2020.</i>
 * Minecraft explore map builder.
 * @author Titov Kirill <thezit445@yandex.ru>
 * @version 1.0.1
 */
public class ExploreMapCreator {
	
	private int xCenter, zCenter;
	
	private boolean locked = false,
					unlimitedTracking = true,
					trakingPosition = true;
	
	private List<MapMarker> markers = new ArrayList<>();
	private int mapColor;
	
	private byte[] colors = new byte[16384];
	
	private static final byte dimension = 0,
							 scale = 0;
	private static final int size = 128,
							 y = 85;
	
	public ExploreMapCreator(int xCenter, int zCenter) {
		this.xCenter = xCenter;
		this.zCenter = zCenter;
	}
	
	public int getXCenter() {
		return xCenter;
	}
	
	public int getZCenter() {
		return zCenter;
	}
	
	public ExploreMapCreator setLocked(boolean value) {
		this.locked = value;
		return this;
	}
	
	public ExploreMapCreator setUnlimitedTracking(boolean value) {
		this.unlimitedTracking = value;
		return this;
	}
	
	public ExploreMapCreator setTrakingPosition(boolean value) {
		this.trakingPosition = value;
		return this;
	}
	
	public ExploreMapCreator setMapColor(int red, int green, int blue) {
		int color = (red<<16) | (green<<8) | blue;
		mapColor = color;
		return this;
	}
	
	public ExploreMapCreator addMarker(MapMarker marker) {
		markers.add(marker);
		return this;
	}
	
	public ItemStack create() {
		int mapsCount = FileManager.getMapsCount();
		
		World world = Bukkit.getWorld("world");
		int x = (xCenter - size/2 + 1);
		int z = (zCenter - size/2 + 1);
		
		Block[] blocks = new Block[16384];
		
		for (int i = z, ic = 0; i<z+size; i++, ic++) {
			for (int j = x, jc = 0; j<x+size; j++, jc++) {
				Block block = world.getBlockAt(j,y,i);
				int offsetY = 1;
				while (block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR) {
					block = world.getBlockAt(j, y-offsetY, i);
					offsetY++;
				}
				blocks[ic*size+jc] = block;
			}
		}
		
		for (int i = 0, offset = 0; i<size; i++, offset+=3) {
			if (offset >= 10) offset = 0;
			for (int j = 0; j<size; j++) {
				Block block = blocks[i*size+j];
				Block positiveX;
				Block negativeX;
				Block positiveZ;
				Block negativeZ;
				if (j < (size-1)) {
					positiveX = blocks[i*size+(j+1)];
				} else {
					positiveX = world.getBlockAt(block.getX()+1, y, block.getZ());
					int offsetY = 1;
					while (positiveX.getType() == Material.AIR || positiveX.getType() == Material.CAVE_AIR) {
						positiveX = world.getBlockAt(block.getX()+1, y-offsetY, block.getZ());
						offsetY++;
					}
				}
				if (j > 0) {
					negativeX = blocks[i*size+(j-1)];
				} else {
					negativeX = world.getBlockAt(block.getX()-1, y, block.getZ());
					int offsetY = 1;
					while (negativeX.getType() == Material.AIR || negativeX.getType() == Material.CAVE_AIR) {
						negativeX = world.getBlockAt(block.getX()-1, y-offsetY, block.getZ());
						offsetY++;
					}
				}
				if (i < (size-1)) {
					positiveZ = blocks[(i+1)*size+j];
				} else {
					positiveZ = world.getBlockAt(block.getX(), y, block.getZ()+1);
					int offsetY = 1;
					while (positiveZ.getType() == Material.AIR || positiveZ.getType() == Material.CAVE_AIR) {
						positiveZ = world.getBlockAt(block.getX()-1, y-offsetY, block.getZ());
						offsetY++;
					}
				}
				if (i > 0) {
					negativeZ = blocks[(i-1)*size+j];
				} else {
					negativeZ = world.getBlockAt(block.getX(), y, block.getZ()-1);
					int offsetY = 1;
					while (negativeZ.getType() == Material.AIR || negativeZ.getType() == Material.CAVE_AIR) {
						negativeZ = world.getBlockAt(block.getX()-1, y-offsetY, block.getZ());
						offsetY++;
					}
				}
				
				int color = TerrainChecker.getColor(block, negativeX, positiveX, negativeZ, positiveZ, offset);
				colors[i*size+j] = (byte) color;
			}
		}

		NBTTagCompound mapMainTag = new NBTTagCompound();
		NBTTagCompound mapDataTag = new NBTTagCompound();
		NBTTagList emptyListTag = new NBTTagList();

		mapDataTag.set("frames", emptyListTag);
		mapDataTag.set("banners", emptyListTag);
		mapDataTag.setByte("scale", scale);
		mapDataTag.setInt("dimension", dimension);
		mapDataTag.setByte("trackingPosition", (byte) (trakingPosition ? 1 : 0));
		mapDataTag.setByte("unlimitedTracking", (byte) (unlimitedTracking ? 1 : 0));
		mapDataTag.setByte("locked", (byte) (locked ? 1 : 0));
		mapDataTag.setInt("xCenter", xCenter);
		mapDataTag.setInt("zCenter", zCenter);
		mapDataTag.setByteArray("colors", colors);
		mapMainTag.setInt("DataVersion", EMCreatorPlugin.getDataVersion());
		mapMainTag.set("data", mapDataTag);

		File file = new File(FileManager.mapsFolder+File.separator+"map_"+mapsCount+".dat");
		if (!file.exists()) {
			try {
				file.createNewFile();
				FileOutputStream stream = new FileOutputStream(file);
				NBTUtils.write(mapMainTag, stream);
			} catch (IOException e) {
				Logger.getLogger(EMCreatorPlugin.class.getName()).log(Level.SEVERE, "error: ", e);
			}
		}

		NBTTagCompound mainTag = new NBTTagCompound();
		NBTTagCompound itemTag = new NBTTagCompound();
		NBTTagCompound displayTag = new NBTTagCompound();

		ItemStack result;

		itemTag.setInt("map", mapsCount);

		NBTTagList listTag = new NBTTagList();
		for (MapMarker marker : markers) {
			NBTTagCompound markerTag = new NBTTagCompound();
			markerTag.setString("id", marker.getId());
			markerTag.setByte("type", marker.getMarketType());
			markerTag.setDouble("x", marker.getX());
			markerTag.setDouble("z", marker.getZ());
			markerTag.setDouble("rot", marker.getRotation());
			listTag.add(markerTag);
		}

		itemTag.set("Decorations", listTag);
		displayTag.setInt("MapColor", mapColor);
		itemTag.set("display", displayTag);
		mainTag.setString("id", "filled_map");
		mainTag.setInt("Count", 1);
		mainTag.set("tag", itemTag);

		NMSItem nmsItem = new NMSItem(mainTag);
		result = nmsItem.asBukkit();
		
		return result;
	}
	
}
