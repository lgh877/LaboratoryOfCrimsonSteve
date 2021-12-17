package net.mcreator.laboratory.procedures;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.IWorld;

import net.mcreator.laboratory.particle.DumbFaceParticle;
import net.mcreator.laboratory.LaboratoryMod;

import java.util.Map;

public class DumbFaceParticleSpawnProcedure {

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				LaboratoryMod.LOGGER.warn("Failed to load dependency world for procedure DumbFaceParticleSpawn!");
			return;
		}
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				LaboratoryMod.LOGGER.warn("Failed to load dependency x for procedure DumbFaceParticleSpawn!");
			return;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				LaboratoryMod.LOGGER.warn("Failed to load dependency y for procedure DumbFaceParticleSpawn!");
			return;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				LaboratoryMod.LOGGER.warn("Failed to load dependency z for procedure DumbFaceParticleSpawn!");
			return;
		}
		IWorld world = (IWorld) dependencies.get("world");
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		if (world instanceof ServerWorld) {
			((ServerWorld) world).spawnParticle(DumbFaceParticle.particle, x, y, z, (int) 1, 0, 0, 0, 0.1);
		}
	}
}
