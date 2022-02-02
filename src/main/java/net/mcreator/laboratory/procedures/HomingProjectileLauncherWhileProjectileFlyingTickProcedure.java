package net.mcreator.laboratory.procedures;

import net.minecraft.world.IWorld;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.LivingEntity;
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
		double range = 0;
		if (entity instanceof ServerPlayerEntity || entity instanceof PlayerEntity) {
			range = 40;
			if (((Entity) world.getEntitiesWithinAABB(MonsterEntity.class,
					new AxisAlignedBB(x - (range / 2d), y - (range / 2d), z - (range / 2d), x + (range / 2d), y + (range / 2d), z + (range / 2d)),
					null).stream().sorted(new Object() {
						Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
							return Comparator.comparing((Function<Entity, Double>) (_entcnd -> _entcnd.getDistanceSq(_x, _y, _z)));
						}
					}.compareDistOf(x, y, z)).findFirst().orElse(null)) != null) {
				imediatesourceentity
						.setMotion(
								((((Entity) world
										.getEntitiesWithinAABB(MonsterEntity.class, new AxisAlignedBB(x - (range / 2d), y - (range / 2d),
												z - (range / 2d), x + (range / 2d), y + (range / 2d), z + (range / 2d)), null)
										.stream().sorted(new Object() {
											Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
												return Comparator
														.comparing((Function<Entity, Double>) (_entcnd -> _entcnd.getDistanceSq(_x, _y, _z)));
											}
										}.compareDistOf(x, y, z)).findFirst().orElse(null)).getPosX() - imediatesourceentity.getPosX()) / 10),
								(((((Entity) world
										.getEntitiesWithinAABB(MonsterEntity.class, new AxisAlignedBB(x - (range / 2d), y - (range / 2d),
												z - (range / 2d), x + (range / 2d), y + (range / 2d), z + (range / 2d)), null)
										.stream().sorted(new Object() {
											Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
												return Comparator
														.comparing((Function<Entity, Double>) (_entcnd -> _entcnd.getDistanceSq(_x, _y, _z)));
											}
										}.compareDistOf(x, y, z)).findFirst().orElse(null)).getPosY() + ((Entity) world
												.getEntitiesWithinAABB(MonsterEntity.class, new AxisAlignedBB(x - (range / 2d), y - (range / 2d),
														z - (range / 2d), x + (range / 2d), y + (range / 2d), z + (range / 2d)), null)
												.stream().sorted(new Object() {
													Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
														return Comparator
																.comparing((Function<Entity, Double>) (_entcnd -> _entcnd.getDistanceSq(_x, _y, _z)));
													}
												}.compareDistOf(x, y, z)).findFirst().orElse(null)).getHeight() * 0.3)
										- imediatesourceentity.getPosY()) / 10),
								((((Entity) world
										.getEntitiesWithinAABB(MonsterEntity.class, new AxisAlignedBB(x - (range / 2d), y - (range / 2d),
												z - (range / 2d), x + (range / 2d), y + (range / 2d), z + (range / 2d)), null)
										.stream().sorted(new Object() {
											Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
												return Comparator
														.comparing((Function<Entity, Double>) (_entcnd -> _entcnd.getDistanceSq(_x, _y, _z)));
											}
										}.compareDistOf(x, y, z)).findFirst().orElse(null)).getPosZ() - imediatesourceentity.getPosZ()) / 10));
			}
		} else {
			{
				List<Entity> _entfound = world
						.getEntitiesWithinAABB(LivingEntity.class,
								new AxisAlignedBB(x - (20 / 2d), y - (20 / 2d), z - (20 / 2d), x + (20 / 2d), y + (20 / 2d), z + (20 / 2d)), null)
						.stream().sorted(new Object() {
							Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
								return Comparator.comparing((Function<Entity, Double>) (_entcnd -> _entcnd.getDistanceSq(_x, _y, _z)));
							}
						}.compareDistOf(x, y, z)).collect(Collectors.toList());
				for (Entity entityiterator : _entfound) {
					//((entity instanceof MobEntity) ? ((MobEntity) entity).getAttackTarget() : null) == entityiterator
					if (!entity.isOnSameTeam(entityiterator)) {
						imediatesourceentity.setMotion(
								(imediatesourceentity.getMotion().getX() + (entityiterator.getPosX() - imediatesourceentity.getPosX()) / 50),
								((imediatesourceentity.getMotion().getY() + entityiterator.getPosY() + entityiterator.getHeight() * 0.3
										- imediatesourceentity.getPosY()) / 50),
								(imediatesourceentity.getMotion().getZ() + (entityiterator.getPosZ() - imediatesourceentity.getPosZ()) / 50));
					}
				}
			}
		}
	}
}
