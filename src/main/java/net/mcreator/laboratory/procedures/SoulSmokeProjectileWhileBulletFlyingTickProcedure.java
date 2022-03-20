package net.mcreator.laboratory.procedures;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.IWorld;

import net.mcreator.laboratory.particle.SoulSmokeParticleParticle;

import java.util.Map;

public class SoulSmokeProjectileWhileBulletFlyingTickProcedure {
	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			return;
		}
		if (dependencies.get("x") == null) {
			return;
		}
		if (dependencies.get("y") == null) {
			return;
		}
		if (dependencies.get("z") == null) {
			return;
		}
		IWorld world = (IWorld) dependencies.get("world");
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		if (world instanceof ServerWorld) {
			((ServerWorld) world).spawnParticle(SoulSmokeParticleParticle.particle, x, y, z, (int) 1, 0, 0, 0, 0);
		}
	}
}
