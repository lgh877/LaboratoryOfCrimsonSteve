package net.mcreator.laboratory.procedures;

import net.minecraft.world.World;
import net.minecraft.world.IWorld;

import net.mcreator.laboratory.LaboratoryMod;

import java.util.Map;

public class OnlySpawnInOverWorldProcedure {

	public static boolean executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				LaboratoryMod.LOGGER.warn("Failed to load dependency world for procedure OnlySpawnInOverWorld!");
			return false;
		}
		IWorld world = (IWorld) dependencies.get("world");
		return (world instanceof World ? (((World) world).getDimensionKey()) : World.OVERWORLD) == (World.OVERWORLD);
	}
}
