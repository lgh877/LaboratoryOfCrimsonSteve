package net.mcreator.laboratory.procedures;

import net.minecraft.world.IWorld;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Entity;

import net.mcreator.laboratory.LaboratoryMod;

import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Map;
import java.util.List;
import java.util.Comparator;

public class HomingProjectileLauncherWhileProjectileFlyingTickProcedure {

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				LaboratoryMod.LOGGER.warn("Failed to load dependency world for procedure HomingProjectileLauncherWhileProjectileFlyingTick!");
			return;
		}
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				LaboratoryMod.LOGGER.warn("Failed to load dependency x for procedure HomingProjectileLauncherWhileProjectileFlyingTick!");
			return;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				LaboratoryMod.LOGGER.warn("Failed to load dependency y for procedure HomingProjectileLauncherWhileProjectileFlyingTick!");
			return;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				LaboratoryMod.LOGGER.warn("Failed to load dependency z for procedure HomingProjectileLauncherWhileProjectileFlyingTick!");
			return;
		}
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				LaboratoryMod.LOGGER.warn("Failed to load dependency entity for procedure HomingProjectileLauncherWhileProjectileFlyingTick!");
			return;
		}
		if (dependencies.get("imediatesourceentity") == null) {
			if (!dependencies.containsKey("imediatesourceentity"))
				LaboratoryMod.LOGGER
						.warn("Failed to load dependency imediatesourceentity for procedure HomingProjectileLauncherWhileProjectileFlyingTick!");
			return;
		}
		IWorld world = (IWorld) dependencies.get("world");
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		Entity entity = (Entity) dependencies.get("entity");
		Entity imediatesourceentity = (Entity) dependencies.get("imediatesourceentity");
		if (entity instanceof ServerPlayerEntity || entity instanceof PlayerEntity) {
			if (((Entity) world
					.getEntitiesWithinAABB(MonsterEntity.class,
							new AxisAlignedBB(x - (10 / 2d), y - (10 / 2d), z - (10 / 2d), x + (10 / 2d), y + (10 / 2d), z + (10 / 2d)), null)
					.stream().sorted(new Object() {
						Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
							return Comparator.comparing((Function<Entity, Double>) (_entcnd -> _entcnd.getDistanceSq(_x, _y, _z)));
						}
					}.compareDistOf(x, y, z)).findFirst().orElse(null)) != null) {
				imediatesourceentity.setMotion((((Entity) world
						.getEntitiesWithinAABB(MonsterEntity.class,
								new AxisAlignedBB(x - (10 / 2d), y - (10 / 2d), z - (10 / 2d), x + (10 / 2d), y + (10 / 2d), z + (10 / 2d)), null)
						.stream().sorted(new Object() {
							Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
								return Comparator.comparing((Function<Entity, Double>) (_entcnd -> _entcnd.getDistanceSq(_x, _y, _z)));
							}
						}.compareDistOf(x, y, z)).findFirst().orElse(null)).getPosX() - imediatesourceentity.getPosX()),
						(((Entity) world.getEntitiesWithinAABB(MonsterEntity.class,
								new AxisAlignedBB(x - (10 / 2d), y - (10 / 2d), z - (10 / 2d), x + (10 / 2d), y + (10 / 2d), z + (10 / 2d)), null)
								.stream().sorted(new Object() {
									Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
										return Comparator.comparing((Function<Entity, Double>) (_entcnd -> _entcnd.getDistanceSq(_x, _y, _z)));
									}
								}.compareDistOf(x, y, z)).findFirst().orElse(null)).getPosY() - imediatesourceentity.getPosY()),
						(((Entity) world.getEntitiesWithinAABB(MonsterEntity.class,
								new AxisAlignedBB(x - (10 / 2d), y - (10 / 2d), z - (10 / 2d), x + (10 / 2d), y + (10 / 2d), z + (10 / 2d)), null)
								.stream().sorted(new Object() {
									Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
										return Comparator.comparing((Function<Entity, Double>) (_entcnd -> _entcnd.getDistanceSq(_x, _y, _z)));
									}
								}.compareDistOf(x, y, z)).findFirst().orElse(null)).getPosZ() - imediatesourceentity.getPosZ()));
			}
		} else {
			{
				List<Entity> _entfound = world
						.getEntitiesWithinAABB(Entity.class,
								new AxisAlignedBB(x - (10 / 2d), y - (10 / 2d), z - (10 / 2d), x + (10 / 2d), y + (10 / 2d), z + (10 / 2d)), null)
						.stream().sorted(new Object() {
							Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
								return Comparator.comparing((Function<Entity, Double>) (_entcnd -> _entcnd.getDistanceSq(_x, _y, _z)));
							}
						}.compareDistOf(x, y, z)).collect(Collectors.toList());
				for (Entity entityiterator : _entfound) {
					if (((entity instanceof MobEntity) ? ((MobEntity) entity).getAttackTarget() : null) == entityiterator) {
						imediatesourceentity.setMotion(
								(((entity instanceof MobEntity) ? ((MobEntity) entity).getAttackTarget() : null).getPosX()
										- imediatesourceentity.getPosX()),
								(((entity instanceof MobEntity) ? ((MobEntity) entity).getAttackTarget() : null).getPosY()
										- imediatesourceentity.getPosY()),
								(((entity instanceof MobEntity) ? ((MobEntity) entity).getAttackTarget() : null).getPosZ()
										- imediatesourceentity.getPosZ()));
					}
				}
			}
		}
	}
}
