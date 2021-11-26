package net.mcreator.laboratory.procedures;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.Entity;

import net.mcreator.laboratory.LaboratoryMod;

import java.util.Map;
import java.util.Collections;

public class TeleportingToolWhileBulletFlyingTickProcedure {
	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				LaboratoryMod.LOGGER.warn("Failed to load dependency entity for procedure TeleportingToolWhileBulletFlyingTick!");
			return;
		}
		if (dependencies.get("imediatesourceentity") == null) {
			if (!dependencies.containsKey("imediatesourceentity"))
				LaboratoryMod.LOGGER.warn("Failed to load dependency imediatesourceentity for procedure TeleportingToolWhileBulletFlyingTick!");
			return;
		}
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				LaboratoryMod.LOGGER.warn("Failed to load dependency x for procedure TeleportingToolWhileBulletFlyingTick!");
			return;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				LaboratoryMod.LOGGER.warn("Failed to load dependency y for procedure TeleportingToolWhileBulletFlyingTick!");
			return;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				LaboratoryMod.LOGGER.warn("Failed to load dependency z for procedure TeleportingToolWhileBulletFlyingTick!");
			return;
		}
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				LaboratoryMod.LOGGER.warn("Failed to load dependency world for procedure TeleportingToolWhileBulletFlyingTick!");
			return;
		}
		Entity entity = (Entity) dependencies.get("entity");
		Entity imediatesourceentity = (Entity) dependencies.get("imediatesourceentity");
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		IWorld world = (IWorld) dependencies.get("world");
		if (world instanceof ServerWorld) {
			((ServerWorld) world).spawnParticle(ParticleTypes.PORTAL, x, y, z, (int) 5, 0.5, 0.5, 0.5, 1);
		}
		Entity _ent = entity;
		_ent.setPositionAndUpdate(x, y, z);
		if (_ent instanceof ServerPlayerEntity) {
			((ServerPlayerEntity) _ent).connection.setPlayerLocation(x, y, z, _ent.rotationYaw, _ent.rotationPitch, Collections.emptySet());
		}
		entity.setInvisible(true);
		entity.setInvulnerable(true);
	}
}
