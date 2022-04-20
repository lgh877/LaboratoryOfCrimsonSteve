
package net.mcreator.laboratory.entity;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.World;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.DamageSource;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.network.IPacket;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.Item;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.CreatureAttribute;

import net.mcreator.laboratory.itemgroup.MobsOfLaboratoryItemGroup;
import net.mcreator.laboratory.item.SoulSmokeProjectileItem;
import net.mcreator.laboratory.entity.renderer.GhostSteveRenderer;
import net.mcreator.laboratory.LaboratoryWatchTargetGoal;
import net.mcreator.laboratory.LaboratoryModElements;

import java.util.Random;
import java.util.EnumSet;

@LaboratoryModElements.ModElement.Tag
public class GhostSteveEntity extends LaboratoryModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER).immuneToFire()
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(100).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(0.6f, 1.8f)).build("ghost_steve").setRegistryName("ghost_steve");

	public GhostSteveEntity(LaboratoryModElements instance) {
		super(instance, 35);
		FMLJavaModLoadingContext.get().getModEventBus().register(new GhostSteveRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -1, -1, new Item.Properties().group(MobsOfLaboratoryItemGroup.tab))
				.setRegistryName("ghost_steve_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(entity, 1, 1, 1));
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
		EntitySpawnPlacementRegistry.register(entity, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
				MonsterEntity::canMonsterSpawn);
	}

	private static class EntityAttributesRegisterHandler {
		@SubscribeEvent
		public void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
			AttributeModifierMap.MutableAttribute ammma = MobEntity.func_233666_p_();
			ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3);
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 60);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 120);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 3);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends MonsterEntity {
		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			experienceValue = 60;
			setNoAI(false);
			this.moveController = new GhostSteveEntity.CustomEntity.MoveHelperController(this);
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false));
			this.goalSelector.addGoal(1, new CustomEntity.RandomFlyGoal(this));
			this.goalSelector.addGoal(4, new LaboratoryWatchTargetGoal(this));
			this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 5, false, false, (p_234199_0_) -> {
				return !(p_234199_0_ instanceof GhostSteveEntity.CustomEntity);
			}));
			this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
			this.goalSelector.addGoal(5, new CustomEntity.LookAroundGoal(this));
		}

		private static class MoveHelperController extends MovementController {
			private final GhostSteveEntity.CustomEntity parentEntity;
			private int courseChangeCooldown;

			public MoveHelperController(GhostSteveEntity.CustomEntity ghast) {
				super(ghast);
				this.parentEntity = ghast;
			}

			public void tick() {
				if (this.action == MovementController.Action.MOVE_TO) {
					if (this.courseChangeCooldown-- <= 0) {
						this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
						Vector3d vector3d = new Vector3d(this.posX - this.parentEntity.getPosX(), this.posY - this.parentEntity.getPosY(),
								this.posZ - this.parentEntity.getPosZ());
						double d0 = vector3d.length();
						vector3d = vector3d.normalize();
						if (this.func_220673_a(vector3d, MathHelper.ceil(d0))) {
							if (!this.parentEntity.isElytraFlying())
								this.parentEntity.setMotion(this.parentEntity.getMotion().add(vector3d.scale(0.2D)));
						} else {
							this.action = MovementController.Action.WAIT;
						}
					}
				}
			}

			private boolean func_220673_a(Vector3d p_220673_1_, int p_220673_2_) {
				AxisAlignedBB axisalignedbb = this.parentEntity.getBoundingBox();
				for (int i = 1; i < p_220673_2_; ++i) {
					axisalignedbb = axisalignedbb.offset(p_220673_1_);
					if (!this.parentEntity.world.hasNoCollisions(this.parentEntity, axisalignedbb)) {
						return false;
					}
				}
				return true;
			}
		}

		private static class RandomFlyGoal extends Goal {
			private final GhostSteveEntity.CustomEntity parentEntity;

			public RandomFlyGoal(GhostSteveEntity.CustomEntity ghast) {
				this.parentEntity = ghast;
				this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
			}

			/**
			 * Returns whether execution should begin. You can also read and cache any state
			 * necessary for execution in this method as well.
			 */
			public boolean shouldExecute() {
				MovementController movementcontroller = this.parentEntity.getMoveHelper();
				if (!movementcontroller.isUpdating()) {
					return true;
				} else {
					double d0 = movementcontroller.getX() - this.parentEntity.getPosX();
					double d1 = movementcontroller.getY() - this.parentEntity.getPosY();
					double d2 = movementcontroller.getZ() - this.parentEntity.getPosZ();
					double d3 = d0 * d0 + d1 * d1 + d2 * d2;
					return d3 < 1.0D || d3 > 3000.0D;
				}
			}

			/**
			 * Returns whether an in-progress EntityAIBase should continue executing
			 */
			public boolean shouldContinueExecuting() {
				return false;
			}

			/**
			 * Execute a one shot task or start executing a continuous task
			 */
			public void startExecuting() {
				Random random = this.parentEntity.getRNG();
				double d0 = this.parentEntity.getPosX() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 8.0F);
				double d1 = this.parentEntity.getPosY() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 8.0F);
				double d2 = this.parentEntity.getPosZ() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 8.0F);
				this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.2D);
			}
		}

		private static class LookAroundGoal extends Goal {
			private final GhostSteveEntity.CustomEntity parentEntity;

			public LookAroundGoal(GhostSteveEntity.CustomEntity ghast) {
				this.parentEntity = ghast;
				this.setMutexFlags(EnumSet.of(Goal.Flag.LOOK));
			}

			/**
			 * Returns whether execution should begin. You can also read and cache any state
			 * necessary for execution in this method as well.
			 */
			public boolean shouldExecute() {
				return true;
			}

			/**
			 * Keep ticking a continuous task that has already been started
			 */
			public void tick() {
				if (this.parentEntity.getAttackTarget() == null) {
					Vector3d vector3d = this.parentEntity.getMotion();
					this.parentEntity.rotationYaw = -((float) MathHelper.atan2(vector3d.x, vector3d.z)) * (180F / (float) Math.PI);
					this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
				} else {
					/*
					 * LivingEntity livingentity = this.parentEntity.getAttackTarget(); double d0 =
					 * 64.0D; if (livingentity.getDistanceSq(this.parentEntity) < 4096.0D) { double
					 * d1 = livingentity.getPosX() - this.parentEntity.getPosX(); double d2 =
					 * livingentity.getPosZ() - this.parentEntity.getPosZ();
					 * this.parentEntity.rotationYaw = -((float) MathHelper.atan2(d1, d2)) * (180F /
					 * (float) Math.PI); this.parentEntity.renderYawOffset =
					 * this.parentEntity.rotationYaw;
					 * 
					 * }
					 */
				}
			}
		}

		public boolean attackEntityFrom(DamageSource source, float amount) {
			if (source == DamageSource.FALL || source == DamageSource.DROWN || source.getTrueSource() instanceof GhostSteveEntity.CustomEntity)
				return false;
			if (source == DamageSource.IN_WALL) {
				if (world instanceof ServerWorld) {
					((ServerWorld) world).spawnParticle(ParticleTypes.SOUL, this.getPosXRandom(0.8), this.getPosYRandom(), this.getPosZRandom(0.8),
							(int) 5, 0, 0, 0, 0);
				}
				return false;
			}
			return super.attackEntityFrom(source, amount);
		}

		@Override
		public void baseTick() {
			super.baseTick();
			double surface = (world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (int) this.getPosX(), (int) this.getPosZ()));
			if (this.getPosY() <= surface + 1) {
				this.setMotion(this.getMotion().add(0, 0.1, 0));
			}
			this.setNoGravity(true);
			this.noClip = true;
			if (this.getAttackTarget() != null) {
				this.setAggroed(false);
				if (this.rand.nextInt(15) == 0) {
					if (this.rand.nextInt(5) == 0) {
						int error = rand.nextInt(4);
						for (int i = 0; i < 2 + error; i++)
							SoulSmokeProjectileItem.shoot(this, this.getAttackTarget(), 0.6f + error * 0.2f);
						this.startSpinAttack(10);
						this.swing(Hand.OFF_HAND, true);
					} else {
						Hand s_hand = this.rand.nextInt(2) == 0 ? Hand.MAIN_HAND : Hand.OFF_HAND;
						SoulSmokeProjectileItem.shoot(this, this.getAttackTarget(), 0.3f);
						this.swing(s_hand, true);
					}
				}
			}
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEFINED;
		}

		@Override
		public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("laboratory:steve_hurt"));
		}

		@Override
		public net.minecraft.util.SoundEvent getDeathSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("laboratory:steve_hurt"));
		}
	}
}
