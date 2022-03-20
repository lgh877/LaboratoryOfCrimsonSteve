package net.mcreator.laboratory.procedures;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Hand;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;

import net.mcreator.laboratory.LaboratoryMod;

import java.util.Map;

public class MagicianOnEntityTickUpdateProcedure {

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				LaboratoryMod.LOGGER.warn("Failed to load dependency world for procedure MagicianOnEntityTickUpdate!");
			return;
		}
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				LaboratoryMod.LOGGER.warn("Failed to load dependency x for procedure MagicianOnEntityTickUpdate!");
			return;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				LaboratoryMod.LOGGER.warn("Failed to load dependency y for procedure MagicianOnEntityTickUpdate!");
			return;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				LaboratoryMod.LOGGER.warn("Failed to load dependency z for procedure MagicianOnEntityTickUpdate!");
			return;
		}
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				LaboratoryMod.LOGGER.warn("Failed to load dependency entity for procedure MagicianOnEntityTickUpdate!");
			return;
		}
		IWorld world = (IWorld) dependencies.get("world");
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		Entity entity = (Entity) dependencies.get("entity");
		double i = 0;
		if (((entity instanceof MobEntity) ? ((MobEntity) entity).getAttackTarget() : null) instanceof LivingEntity) {
			if (Math.random() < 0.1 && !entity.getPersistentData().getBoolean("FangAttack")) {
				entity.getPersistentData().putBoolean("FangAttack", (true));
				entity.getPersistentData().putDouble("FangAttackTicks", 0);
				entity.getPersistentData().putDouble("FangAttackDirection", Math.random());
				if (entity.getPersistentData().getDouble("FangAttackDirection") < 0.25) {
					for (int index0 = 0; index0 < (int) (20); index0++) {
						i = (i + 1);
						new Object() {
							private int ticks = 0;
							private float waitTicks;
							private IWorld world;

							public void start(IWorld world, int waitTicks) {
								this.waitTicks = waitTicks;
								MinecraftForge.EVENT_BUS.register(this);
								this.world = world;
							}

							@SubscribeEvent
							public void tick(TickEvent.ServerTickEvent event) {
								if (event.phase == TickEvent.Phase.END) {
									this.ticks += 1;
									if (this.ticks >= this.waitTicks)
										run();
								}
							}

							private void run() {
								entity.getPersistentData().putDouble("FangAttackX", (x + entity.getPersistentData().getDouble("FangAttackTicks")));
								entity.getPersistentData().putDouble("FangAttackY", y);
								entity.getPersistentData().putDouble("FangAttackZ", z);
								while (world.getBlockState(new BlockPos((int) (entity.getPersistentData().getDouble("FangAttackX")),
										(int) (entity.getPersistentData().getDouble("FangAttackY")),
										(int) (entity.getPersistentData().getDouble("FangAttackZ")))).isSolid()) {
									entity.getPersistentData().putDouble("FangAttackY", (entity.getPersistentData().getDouble("FangAttackY") + 1));
								}
								if (world instanceof ServerWorld) {
									EvokerFangsEntity entityToSpawn = new EvokerFangsEntity((World) world,
											(entity.getPersistentData().getDouble("FangAttackX")),
											(entity.getPersistentData().getDouble("FangAttackY")),
											(entity.getPersistentData().getDouble("FangAttackZ")), 0, 0, (LivingEntity) entity);
									world.addEntity(entityToSpawn);
								}
								entity.getPersistentData().putDouble("FangAttackTicks",
										(entity.getPersistentData().getDouble("FangAttackTicks") + 1));
								if (entity.getPersistentData().getDouble("FangAttackTicks") == 20) {
									entity.getPersistentData().putBoolean("FangAttack", (false));
								}
								MinecraftForge.EVENT_BUS.unregister(this);
							}
						}.start(world, (int) i);
					}
				} else if (entity.getPersistentData().getDouble("FangAttackDirection") < 0.5) {
					for (int index2 = 0; index2 < (int) (20); index2++) {
						i = (i + 1);
						new Object() {
							private int ticks = 0;
							private float waitTicks;
							private IWorld world;

							public void start(IWorld world, int waitTicks) {
								this.waitTicks = waitTicks;
								MinecraftForge.EVENT_BUS.register(this);
								this.world = world;
							}

							@SubscribeEvent
							public void tick(TickEvent.ServerTickEvent event) {
								if (event.phase == TickEvent.Phase.END) {
									this.ticks += 1;
									if (this.ticks >= this.waitTicks)
										run();
								}
							}

							private void run() {
								entity.getPersistentData().putDouble("FangAttackX", (x - entity.getPersistentData().getDouble("FangAttackTicks")));
								entity.getPersistentData().putDouble("FangAttackY", y);
								entity.getPersistentData().putDouble("FangAttackZ", z);
								while (world.getBlockState(new BlockPos((int) (entity.getPersistentData().getDouble("FangAttackX")),
										(int) (entity.getPersistentData().getDouble("FangAttackY")),
										(int) (entity.getPersistentData().getDouble("FangAttackZ")))).isSolid()) {
									entity.getPersistentData().putDouble("FangAttackY", (entity.getPersistentData().getDouble("FangAttackY") + 1));
								}
								if (world instanceof ServerWorld) {
									EvokerFangsEntity entityToSpawn = new EvokerFangsEntity((World) world,
											(entity.getPersistentData().getDouble("FangAttackX")),
											(entity.getPersistentData().getDouble("FangAttackY")),
											(entity.getPersistentData().getDouble("FangAttackZ")), 0, 0, (LivingEntity) entity);
									world.addEntity(entityToSpawn);
								}
								entity.getPersistentData().putDouble("FangAttackTicks",
										(entity.getPersistentData().getDouble("FangAttackTicks") + 1));
								if (entity.getPersistentData().getDouble("FangAttackTicks") == 20) {
									entity.getPersistentData().putBoolean("FangAttack", (false));
								}
								MinecraftForge.EVENT_BUS.unregister(this);
							}
						}.start(world, (int) i);
					}
				} else if (entity.getPersistentData().getDouble("FangAttackDirection") < 0.75) {
					for (int index4 = 0; index4 < (int) (20); index4++) {
						i = (i + 1);
						new Object() {
							private int ticks = 0;
							private float waitTicks;
							private IWorld world;

							public void start(IWorld world, int waitTicks) {
								this.waitTicks = waitTicks;
								MinecraftForge.EVENT_BUS.register(this);
								this.world = world;
							}

							@SubscribeEvent
							public void tick(TickEvent.ServerTickEvent event) {
								if (event.phase == TickEvent.Phase.END) {
									this.ticks += 1;
									if (this.ticks >= this.waitTicks)
										run();
								}
							}

							private void run() {
								entity.getPersistentData().putDouble("FangAttackX", x);
								entity.getPersistentData().putDouble("FangAttackY", y);
								entity.getPersistentData().putDouble("FangAttackZ", (z + entity.getPersistentData().getDouble("FangAttackTicks")));
								while (world.getBlockState(new BlockPos((int) (entity.getPersistentData().getDouble("FangAttackX")),
										(int) (entity.getPersistentData().getDouble("FangAttackY")),
										(int) (entity.getPersistentData().getDouble("FangAttackZ")))).isSolid()) {
									entity.getPersistentData().putDouble("FangAttackY", (entity.getPersistentData().getDouble("FangAttackY") + 1));
								}
								if (world instanceof ServerWorld) {
									EvokerFangsEntity entityToSpawn = new EvokerFangsEntity((World) world,
											(entity.getPersistentData().getDouble("FangAttackX")),
											(entity.getPersistentData().getDouble("FangAttackY")),
											(entity.getPersistentData().getDouble("FangAttackZ")), 0, 0, (LivingEntity) entity);
									world.addEntity(entityToSpawn);
								}
								entity.getPersistentData().putDouble("FangAttackTicks",
										(entity.getPersistentData().getDouble("FangAttackTicks") + 1));
								if (entity.getPersistentData().getDouble("FangAttackTicks") == 20) {
									entity.getPersistentData().putBoolean("FangAttack", (false));
								}
								MinecraftForge.EVENT_BUS.unregister(this);
							}
						}.start(world, (int) i);
					}
				} else {
					for (int index6 = 0; index6 < (int) (20); index6++) {
						i = (i + 1);
						new Object() {
							private int ticks = 0;
							private float waitTicks;
							private IWorld world;

							public void start(IWorld world, int waitTicks) {
								this.waitTicks = waitTicks;
								MinecraftForge.EVENT_BUS.register(this);
								this.world = world;
							}

							@SubscribeEvent
							public void tick(TickEvent.ServerTickEvent event) {
								if (event.phase == TickEvent.Phase.END) {
									this.ticks += 1;
									if (this.ticks >= this.waitTicks)
										run();
								}
							}

							private void run() {
								entity.getPersistentData().putDouble("FangAttackX", x);
								entity.getPersistentData().putDouble("FangAttackY", y);
								entity.getPersistentData().putDouble("FangAttackZ", (z - entity.getPersistentData().getDouble("FangAttackTicks")));
								while (world.getBlockState(new BlockPos((int) (entity.getPersistentData().getDouble("FangAttackX")),
										(int) (entity.getPersistentData().getDouble("FangAttackY")),
										(int) (entity.getPersistentData().getDouble("FangAttackZ")))).isSolid()) {
									entity.getPersistentData().putDouble("FangAttackY", (entity.getPersistentData().getDouble("FangAttackY") + 1));
								}
								if (world instanceof ServerWorld) {
									EvokerFangsEntity entityToSpawn = new EvokerFangsEntity((World) world,
											(entity.getPersistentData().getDouble("FangAttackX")),
											(entity.getPersistentData().getDouble("FangAttackY")),
											(entity.getPersistentData().getDouble("FangAttackZ")), 0, 0, (LivingEntity) entity);
									world.addEntity(entityToSpawn);
								}
								entity.getPersistentData().putDouble("FangAttackTicks",
										(entity.getPersistentData().getDouble("FangAttackTicks") + 1));
								if (entity.getPersistentData().getDouble("FangAttackTicks") == 20) {
									entity.getPersistentData().putBoolean("FangAttack", (false));
								}
								MinecraftForge.EVENT_BUS.unregister(this);
							}
						}.start(world, (int) i);
					}
				}
			} else if (Math.random() < 0.07) {
				if (entity instanceof LivingEntity) {
					((LivingEntity) entity).swing(Hand.OFF_HAND, true);
				}
			}
		}
	}
}
