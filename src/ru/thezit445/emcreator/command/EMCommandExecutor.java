package ru.thezit445.emcreator.command;

import java.util.ArrayList;
import java.util.List;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import ru.thezit445.emcreator.EMCreatorPlugin;
import ru.thezit445.emcreator.config.Message;
import ru.thezit445.emcreator.core.ExploreMapCreator;
import ru.thezit445.emcreator.core.MapMarker;
import ru.thezit445.emcreator.core.MapMarkerType;

/**
 * <i>Created on 06.04.2020.</i>
 * Command executor class for plugin.
 * @author Titov Kirill <thezit445@yandex.ru>
 * @version 1.0.1
 */
public class EMCommandExecutor implements CommandExecutor, TabExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(Message.CMD_ONLY_PLAYER.getMessage());
			return true;
		}
		
		Player player = (Player) sender;
		
		if (!player.hasPermission("emcreator.admin")) {
			player.sendMessage(Message.PERMISSION_DENY.getMessage());
			return true;
		}
		
		if (args.length == 0) {
			Sub.HELP.invoke(player);
			return true;
		}
		
		if (args.length == 1) {
			for (Sub sub : Sub.values()) {
				if (sub.name.equals(args[0])) {
					sub.invoke(player);
					return true;
				}
			}
		}
		
		if (args.length>1 && args.length<6) {
			player.sendMessage(Message.CMD_ILLEGAL_ARGUMENTS.getMessage());
			return true;
		}
		
		if (args.length >= 6) {
			BukkitRunnable runnable = new BukkitRunnable() {
				@Override
				public void run() {
					generate(player, args);
				}
			};
			runnable.runTaskAsynchronously(EMCreatorPlugin.getInstance());
		} else {
			player.sendMessage(Message.CMD_ILLEGAL_ARGUMENTS.getMessage());
		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> list = new ArrayList<>();
		if (args.length == 1) {
			if (!args[0].equals("")) {
				for (Sub sub : Sub.values()) {
					if (sub.name.startsWith(args[0])) {
						list.add(sub.name);
					}
				}
			} else {
				for (Sub sub : Sub.values()) list.add(sub.name);
			}
		}
		return list;
	}
	
	private static final String intMatch = "[-+]?\\d+";
	private static final String trueStr = "true", falseStr = "false";
	private static void generate(Player player, String[] args) {
		player.sendMessage(Message.MAP_LOADING.getMessage());
		
		String xStr = args[0];
		String zStr = args[1];
		String lockedStr = args[2];
		String trackingStr = args[3];
		String unlimitedStr = args[4];
		
		int x = 0, z = 0;
		boolean locked = false, tracking = true, unlimited = true;
		
		boolean argsIsCorrrect = digitParse(xStr, player) &&
								 digitParse(zStr, player) &&
								 booleanParse(lockedStr, player) &&
								 booleanParse(trackingStr, player) &&
								 booleanParse(unlimitedStr, player);
		
		if (argsIsCorrrect) {
			x = Integer.parseInt(xStr);
			z = Integer.parseInt(zStr);
			locked = Boolean.parseBoolean(lockedStr);
			tracking = Boolean.parseBoolean(trackingStr);
			unlimited = Boolean.parseBoolean(unlimitedStr);
		} else {
			player.sendMessage(Message.CMD_ILLEGAL_ARGUMENTS.getMessage());
			return;
		}
		
		ExploreMapCreator creator = new ExploreMapCreator(x, z);
		creator.setLocked(locked).setTrakingPosition(tracking).setUnlimitedTracking(unlimited);
		
		int colorIndex[] = new int[3];
		String color = args[5].replaceAll("[{}]", "");
		String[] strDigits = color.split(",");
		for (int j = 0; j<colorIndex.length; j++) {
			if (!digitParse(strDigits[j], player)) {
				player.sendMessage(Message.MAP_LOAD_FAIL.getMessage());
				return;
			}
			colorIndex[j] = Integer.parseInt(strDigits[j]);
		}
		creator.setMapColor(colorIndex[0], colorIndex[1], colorIndex[2]);
		
		if (args.length-6>0) {
			MapMarkerType type = null;
			int dx = 0, dz = 0, rot = 0;
			for (int i = 6; i<args.length; i++) {
				boolean correct = true;
				
				String decorate = args[i].replaceAll("[{}]", "");
				type = MapMarkerType.MANSION;
				dx = 0; dz = 0; rot = 0;
				if (!decorate.contains(",")) {
					player.sendMessage(Message.MAP_LOAD_FAIL.getMessage());
					return;
				}
				String[] tags = decorate.split(",");
				if (tags.length != 4) {
					player.sendMessage(Message.MAP_LOAD_FAIL.getMessage());
					return;
				}
				for (String tag : tags) {
					String[] value = tag.split(":");
					if (value.length != 2) {
						player.sendMessage(Message.MAP_LOAD_FAIL.getMessage());
						return;
					}
					String tagName = value[0];
					switch (tagName) {
						case "type": {
							correct = correct && mapMarkerTypeParse(value[1], player);
							if (!correct) {
								player.sendMessage(Message.MAP_LOAD_FAIL.getMessage());
								return;
							}
							type = MapMarkerType.valueOf(value[1].toUpperCase());
							break;
						}
						case "x": {
							correct = correct && digitParse(value[1], player);
							if (!correct) {
								player.sendMessage(Message.MAP_LOAD_FAIL.getMessage());
								return;
							}
							dx = Integer.parseInt(value[1]);
							break;
						}
						case "z": {
							correct = correct && digitParse(value[1], player);
							if (!correct) {
								player.sendMessage(Message.MAP_LOAD_FAIL.getMessage());
								return;
							}
							dz = Integer.parseInt(value[1]);
							break;
						}
						case "rot":{
							correct = correct && digitParse(value[1], player);
							if (!correct) {
								player.sendMessage(Message.MAP_LOAD_FAIL.getMessage());
								return;
							}
							rot = Integer.parseInt(value[1]);
							break;
						}
						default: player.sendMessage(Message.MAP_LOAD_FAIL.getMessage()); return;
					}
					if (!correct) {
						player.sendMessage(Message.MAP_LOAD_FAIL.getMessage());
						return;
					}
				}
				creator.addMarker(new MapMarker(type, dx, dz, rot));
			}
		}
		
		ItemStack map = creator.create();
		player.getInventory().addItem(map);
		player.sendMessage(Message.MAP_LOAD_SUCCESS.getMessage());
	}
	
	private static boolean digitParse(String str, Player player) {
		if (str.matches(intMatch)) {
			return true;
		} else {
			return false;
		}
	}
	
	private static boolean booleanParse(String str, Player player) {
		if (str.equals(trueStr) || str.equals(falseStr)) {
			return true;
		} else {
			return false;
		}
	}
	
	private static boolean mapMarkerTypeParse(String str, Player player) {
		for (MapMarkerType type : MapMarkerType.values()) {
			if (str.toUpperCase().equalsIgnoreCase(type.name())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Enum with sub commands for executor.
	 */
	private enum Sub {
		
		HELP("help"){
			@Override
			void invoke(Player player) {
				String messageName = "CMD_HELP_";
				for (int i = 0; i<8; i++) {
					String name = messageName+i;
					player.sendMessage(Message.valueOf(name.toUpperCase()).getMessage());
				}
			}
		},
		TYPES("types"){
			@Override
			void invoke(Player player) {
				String messageName = "CMD_DECOR_TYPES_";
				for (int i = 0; i<13; i++) {
					String name = messageName+i;
					player.sendMessage(Message.valueOf(name.toUpperCase()).getMessage());
				}
			}
		};
		
		private String name;
		
		Sub(String name){
			this.name = name;
		}
		
		void invoke(Player player) {};
		
	}
	
}
