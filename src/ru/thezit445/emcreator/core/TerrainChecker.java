package ru.thezit445.emcreator.core;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * <i>Created on 06.04.2020.</i>
 * Class with static method for get color of block;
 * @author Titov Kirill <thezit445@yandex.ru>
 * @version 1.0.1
 */
public final class TerrainChecker {

	private TerrainChecker() {}
	
	private static final int[] waterColors = {60,61,62,61,60};
	private static final int[] shoalColors = {62,61,61,60,60,105};
	private static final int[] deepShoalColors = {105,61,61,61,0,0};
	
	public static int getColor(Block block, Block negativeX, Block positiveX, Block negativeZ, Block positiveZ, int waterOffset) {
		
		if (isWater(block)) {
			if (isShoal(negativeX, negativeZ, positiveX, positiveZ)) {
				return shoalColors[new Random().nextInt(6)];
			} else if (isDeepShoal(negativeX, negativeZ, positiveX, positiveZ)) {
				return deepShoalColors[new Random().nextInt(6)];
			}
			if (block.getZ()%2==0) {
				return 0;
			} else {
				int ox = Math.abs((block.getX()+waterOffset)%40);
				return waterColors[ox/8];
			}
		} else {
			if(isBeach(negativeX, negativeZ, positiveX, positiveZ)) {
				return 107;
			}
			return 0;
		}
	}
	
	public static boolean isWater(Block block) {
		return block.getType() == Material.WATER || block.getType() == Material.KELP_PLANT || block.getType() == Material.KELP;
	}
	
	public static boolean isShoal(Block negativeX, Block negativeZ, Block positiveX, Block positiveZ) { 
		return (!isWater(negativeX) || !isWater(positiveX) || !isWater(positiveZ) || !isWater(negativeZ));
	}
	
	public static boolean isDeepShoal(Block negativeX, Block negativeZ, Block positiveX, Block positiveZ) {
		return (!isWater(negativeX.getRelative(-1, 0, 0)) || !isWater(positiveX.getRelative(1, 0, 0)) || !isWater(positiveZ.getRelative(0, 0, 1)) || !isWater(negativeZ.getRelative(0, 0, -1)));
	}
	
	public static boolean isBeach(Block negativeX, Block negativeZ, Block positiveX, Block positiveZ) {
		return (isWater(negativeX) || isWater(positiveX) || isWater(positiveZ) || isWater(negativeZ));
	}
}
