package ru.thezit445.emcreator.core;

/**
 * <i>Created on 06.04.2020.</i>
 * Enum with map marker types.
 * @author Titov Kirill <thezit445@yandex.ru>
 * @version 1.0.1
 */
public enum MapMarkerType {
	
	PLAYER((byte) 0),
	FRAME((byte) 1),
	RED_MARKER((byte) 2),
	BLUE_MARKER((byte) 3),
	TARGET_X((byte) 4),
	TARGET_POINT((byte) 5),
	PLAYER_OFF_MAP((byte) 6),
	PLAYER_OFF_LIMITS((byte) 7),
	MANSION((byte) 8),
	MONUMENT((byte) 9),
	BANNER_WHITE((byte) 10),
	BANNER_ORANGE((byte) 11),
	BANNER_MAGENTA((byte) 12),
	BANNER_CYAN((byte) 13),
	BANNER_YELLOW((byte) 14),
	BANNER_LIME((byte) 15),
	BANNER_PINK((byte) 16),
	BANNER_DARK_GRAY((byte) 17),
	BANNER_GRAY((byte) 18),
	BANNER_LIGHT_BLUE((byte) 19),
	BANNER_PURPLE((byte) 20),
	BANNER_BLUE((byte) 21),
	BANNER_BROWN((byte) 22),
	BANNER_GREEN((byte) 23),
	BANNER_RED((byte) 24),
	BANNER_BLACK((byte) 25),
	RED_X((byte) 26);
	
	private byte id;
	
	MapMarkerType(byte id) {
		this.id = id;
	}
	
	public byte getId() {
		return id;
	}
	
}
