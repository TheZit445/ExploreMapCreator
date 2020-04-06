package ru.thezit445.emcreator.utils;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;

import ru.thezit445.emcreator.EMCreatorPlugin;
import ru.thezit445.nbtreflectionapi.nbt.NBTTagCompound;
import ru.thezit445.nbtreflectionapi.util.NBTUtils;

/**
 * <i>Created on 06.04.2020.</i>
 * Simple file manager for plugin.
 * @author Titov Kirill <thezit445@yandex.ru>
 * @version 1.0.1
 */
public final class FileManager {
	
	private FileManager() {}
	
	public static final String mainFolder = Bukkit.getWorldContainer().getPath();
	public static final String mapsFolder = mainFolder+File.separator+"world"+File.separator+"data";
	
	public static final String dot = ".";
	public static final String ymlFileType = "yml";
	
	public static int getMapsCount() {
		File mapsFolder = new File(FileManager.mapsFolder);
		File[] maps = mapsFolder.listFiles((folder, name) -> (name.startsWith("map_") && name.endsWith(".dat")));
		if (maps == null || maps.length == 0) {
			createIdCounts();
			return 0;
		}
		updateIdCounts(maps.length);
		return maps.length;
	}
	
	public static void createIdCounts() {
		File file = new File(mapsFolder+File.separator+"idcounts.dat");
		try {
			FileOutputStream outStream;
			NBTTagCompound idcounts = new NBTTagCompound();
			NBTTagCompound subTag = new NBTTagCompound();
			subTag.setInt("map", 0);
			idcounts.set("data", subTag);
			idcounts.setInt("DataVersion", EMCreatorPlugin.getDataVersion());
			if (!file.exists()) file.createNewFile();
			outStream = new FileOutputStream(file);
			NBTUtils.write(idcounts, outStream);
		} catch (IOException e) {
			Logger.getLogger(EMCreatorPlugin.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	public static void updateIdCounts(int index) {
		try {
			File file = new File(mapsFolder+File.separator+"idcounts.dat");
			if (!file.exists()) createIdCounts();
			FileInputStream inStream = new FileInputStream(file);
			NBTTagCompound idcounts = NBTUtils.read(inStream);
			NBTTagCompound subTag = idcounts.get("data");
			subTag.setInt("map", index);
			idcounts.set("data", subTag);
			FileOutputStream outStream = new FileOutputStream(file);
			NBTUtils.write(idcounts, outStream);
		} catch (IOException e) {
			Logger.getLogger(EMCreatorPlugin.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
}
