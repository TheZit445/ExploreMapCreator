package ru.thezit445.emcreator.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.thezit445.emcreator.EMCreatorPlugin;
import ru.thezit445.emcreator.utils.FileManager;

/**
 * <i>Created on 06.04.2020.</i>
 * Enum with plugin config files.
 * @author Titov Kirill <thezit445@yandex.ru>
 * @version 1.0.1
 */
public enum Config {
	
	MAIN_CONFIG("config"),
	MESSAGE_FILE("%lang%_messages");
	
	private static final Config[] skipedConfigs = {Config.MESSAGE_FILE};
	private static final List<Config> skipedConfigsList = Arrays.asList(skipedConfigs);
	
	private String configName;
	private PluginConfiguration config;

	Config(String configName){
		this.configName = configName;
	}
	
	public String getConfigName() {
		return configName;
	}
	
	public PluginConfiguration getConfig() {
		return config;
	}
	
	public static void initialize() {
		
		for (Config cfg : Config.values()) {
			if (skipedConfigsList.contains(cfg)) continue;
			String fileName = cfg.configName+FileManager.dot+FileManager.ymlFileType;
			String folderPath = EMCreatorPlugin.getInstance().getDataFolder().toString()+File.separator+"configs";
			String fullPath = folderPath+File.separator+fileName;
			File file = new File(fullPath);
			if (file.exists()) {
				cfg.config = new PluginConfiguration(file);
			} else {
				try {
					EMCreatorPlugin.getResourceLoader().uploadFile(fileName, fullPath);
					cfg.config = new PluginConfiguration(file);
				} catch (FileNotFoundException e) {
					Logger.getLogger(EMCreatorPlugin.class.getName()).log(Level.SEVERE, "Resource load error: ", e);
				}
			}
		}
		
		messageInit();
		
	}
	
	private static void messageInit() {
		String lang = Config.MAIN_CONFIG.getConfig().getString("lang").toLowerCase();
		String fileName = Config.MESSAGE_FILE.getConfigName()+FileManager.dot+FileManager.ymlFileType;
		fileName = fileName.replace("%lang%", lang);
		String folderPath = EMCreatorPlugin.getInstance().getDataFolder().toString()+File.separator+"configs";
		String fullPath = folderPath+File.separator+fileName;
		File file = new File(fullPath);
		if (file.exists()) {
			Config.MESSAGE_FILE.config = new PluginConfiguration(file);
		} else {
			switch (lang) {
				case "en":
				case "ru": {
					try {
						EMCreatorPlugin.getResourceLoader().uploadFile(fileName, fullPath);
						Config.MESSAGE_FILE.config = new PluginConfiguration(file);
					} catch (FileNotFoundException e) {
						Logger.getLogger(EMCreatorPlugin.class.getName()).log(Level.SEVERE, "Error: ", e);
					}
					break;
				}
				default: {
					lang = "en";
					fileName = Config.MESSAGE_FILE.getConfigName()+FileManager.dot+FileManager.ymlFileType;
					fileName = fileName.replace("%lang%", lang);
					folderPath = EMCreatorPlugin.getInstance().getDataFolder().toString()+File.separator+"configs";
					fullPath = folderPath+File.separator+fileName;
					file = new File(fullPath);
					if (file.exists()) {
						Config.MESSAGE_FILE.config = new PluginConfiguration(file);
					} else {
						try {
							EMCreatorPlugin.getResourceLoader().uploadFile(fileName, fullPath);
							Config.MESSAGE_FILE.config = new PluginConfiguration(file);
						} catch (FileNotFoundException e) {
							Logger.getLogger(EMCreatorPlugin.class.getName()).log(Level.SEVERE, "Error: ", e);
						}
					}
					break;
				}
			}
		}
	}
	
}
