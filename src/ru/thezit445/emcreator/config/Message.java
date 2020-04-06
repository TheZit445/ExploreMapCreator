package ru.thezit445.emcreator.config;

import java.util.Map;

import org.bukkit.ChatColor;

/**
 * <i>Created on 06.04.2020.</i>
 * Enum with plugin messages.
 * @author Titov Kirill <thezit445@yandex.ru>
 * @version 1.0.1
 */
public enum Message {
	
	PERMISSION_DENY("permission-deny"),
	MAP_LOADING("map-loading"),
	MAP_LOAD_SUCCESS("map-load-success"),
	MAP_LOAD_FAIL("map-load-fail"),
	CMD_HELP_0("cmd-help-0"),
	CMD_HELP_1("cmd-help-1"),
	CMD_HELP_2("cmd-help-2"),
	CMD_HELP_3("cmd-help-3"),
	CMD_HELP_4("cmd-help-4"),
	CMD_HELP_5("cmd-help-5"),
	CMD_HELP_6("cmd-help-6"),
	CMD_HELP_7("cmd-help-7"),
	CMD_HELP_8("cmd-help-8"),
	CMD_ONLY_PLAYER("cmd-only-player"),
	CMD_DECOR_TYPES_0("cmd-decoration-type-list-0"),
	CMD_DECOR_TYPES_1("cmd-decoration-type-list-1"),
	CMD_DECOR_TYPES_2("cmd-decoration-type-list-2"),
	CMD_DECOR_TYPES_3("cmd-decoration-type-list-3"),
	CMD_DECOR_TYPES_4("cmd-decoration-type-list-4"),
	CMD_DECOR_TYPES_5("cmd-decoration-type-list-5"),
	CMD_DECOR_TYPES_6("cmd-decoration-type-list-6"),
	CMD_DECOR_TYPES_7("cmd-decoration-type-list-7"),
	CMD_DECOR_TYPES_8("cmd-decoration-type-list-8"),
	CMD_DECOR_TYPES_9("cmd-decoration-type-list-9"),
	CMD_DECOR_TYPES_10("cmd-decoration-type-list-10"),
	CMD_DECOR_TYPES_11("cmd-decoration-type-list-11"),
	CMD_DECOR_TYPES_12("cmd-decoration-type-list-12"),
	CMD_ILLEGAL_ARGUMENTS("cmd-illegal-arguments");
	
	private String section;
	private String message = ChatColor.DARK_RED+"Message error.";
	
	Message(String section) {
		this.section = section;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getMessage(Map<String, String> replacements) {
		String msg = message;
		for (String tag : replacements.keySet()) {
			if (msg.contains(tag)) {
				msg = msg.replace(tag, replacements.get(tag));
			}
		}
		return msg;
	}
	
	public String getMessage(String tag, String replacement) {
		String msg = message;
		if (message.contains(tag)) {
			msg = message.replace(tag, replacement);
		}
		return msg;
	}
	
	public static void initialize(PluginConfiguration config) {
		for (Message msg : Message.values()) {
			String text = config.getString(msg.section);
			msg.message = convertToColorText(text);
		}
	}
	
	public static String convertToColorText(String text) {
		if (text.contains("&")) {
			text = ChatColor.translateAlternateColorCodes('&', text);
		}
		return text;
	}
	
}
