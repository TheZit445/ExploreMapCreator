package ru.thezit445.emcreator.core;

import java.util.UUID;

/**
 * <i>Created on 06.04.2020.</i>
 * Marker class for minecraft map;
 * @author Titov Kirill <thezit445@yandex.ru>
 * @version 1.0.1
 */
public class MapMarker {
	
	private MapMarkerType type;
	private int x, z, rotation;
	private String id;
	
	public MapMarker(MapMarkerType type, int x, int z, int rotation) {
		this.id = UUID.randomUUID().toString();
		this.rotation = rotation;
		this.x = x;
		this.z = z;
		this.type = type;
	}
	
	public int getX() {
		return x;
	}
	
	public int getZ() {
		return z;
	}
	
	public int getRotation() {
		return rotation;
	}
	
	public String getId() {
		return id;
	}
	
	public byte getMarketType() {
		return type.getId();
	}
	
}
