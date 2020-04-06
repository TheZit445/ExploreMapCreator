package ru.thezit445.emcreator.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.thezit445.emcreator.EMCreatorPlugin;

/**
 * <i>Created on 06.04.2020.</i>
 * Simple resource loader for plugin.
 * @author Titov Kirill <thezit445@yandex.ru>
 * @version 1.0.1
 */
public class ResourceLoader {
	
	final private static String resourcesFolder = "resources/";
	
	public ResourceLoader() {
	}
	
	public void uploadFile(String fileName, String targetFilePath) throws FileNotFoundException {
		InputStream uploadedFile = EMCreatorPlugin.getInstance().getResource(resourcesFolder+fileName);
		File file = new File(targetFilePath);
		if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
		if (!file.exists()) {
			try {
				Files.copy(uploadedFile, file.getAbsoluteFile().toPath());
			} catch (IOException e) {
				Logger.getLogger(EMCreatorPlugin.class.getName()).log(Level.SEVERE, "Resource load error: ", e);
			}
		}
	}
	
}
