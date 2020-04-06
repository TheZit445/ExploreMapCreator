package ru.thezit445.emcreator;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import ru.thezit445.emcreator.command.EMCommandExecutor;
import ru.thezit445.emcreator.config.Config;
import ru.thezit445.emcreator.config.Message;
import ru.thezit445.emcreator.utils.ResourceLoader;

/**
 * <i>Created on 06.04.2020.</i>
 * Main plugin class.
 * @author Titov Kirill <thezit445@yandex.ru>
 * @version 1.0.1
 */
public class EMCreatorPlugin extends JavaPlugin {
	
	private static EMCreatorPlugin instance;
	private static ResourceLoader resourceLoader;
	private static int dataVersion;

	@Override
	public void onLoad() {	
	}
	
	@Override
	public void onEnable() {
		instance = this;
		resourceLoader = new ResourceLoader();
		dataVersion = checkVersion().getData();
		Config.initialize();
		Message.initialize(Config.MESSAGE_FILE.getConfig());
		Bukkit.getPluginCommand("emcreate").setExecutor(new EMCommandExecutor());
	}
	
	@Override
	public void onDisable() {
	}
	
	public static EMCreatorPlugin getInstance() {
		return instance;
	}
	
	public static ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	public static MapVersion checkVersion() {
		String serverVersion = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
		MapVersion version = MapVersion.valueOf(serverVersion);
		return version;
	}

	public static int getDataVersion() {
		return dataVersion;
	}

}
