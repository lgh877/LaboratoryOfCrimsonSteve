
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

import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.DamageSource;
import net.minecraft.network.IPacket;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Item;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.CreatureAttribute;

import net.mcreator.laboratory.procedures.MagicianOnEntityTickUpdateProcedure;
import net.mcreator.laboratory.item.PuchLaserBeamLauncherItem;
import net.mcreator.laboratory.entity.renderer.MagicianRenderer;
import net.mcreator.laboratory.LaboratoryModElements;

import java.util.stream.Stream;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumSet;
import java.util.AbstractMap;

@LaboratoryModElements.ModElement.Tag
public class MagicianEntity extends LaboratoryModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(0.6f, 1.8f)).build("magician").setRegistryName("magician");

	public MagicianEntity(LaboratoryModElements instance) {
		super(instance, 57);
		FMLJavaModLoadingContext.get().getModEventBus().register(new MagicianRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -10079488, -10092340, new Item.Properties().group(ItemGroup.MISC))
				.setRegistryName("magician_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(entity, 20, 1, 4));
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
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 500);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 100);
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
			experienceValue = 500;
			setNoAI(false);
			stepHeight = 40;
			this.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(PuchLaserBeamLauncherItem.block));
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.goalSelector.addGoal(1, new CustomEntity.MoveAttackGoal(this, 1.2, 0));
			this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, LivingEntity.class, false, false));
			//this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2, false));
			this.goalSelector.addGoal(3, new RandomWalkingGoal(this, 1));
			this.targetSelector.addGoal(4, new HurtByTargetGoal(this));
			this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
			this.goalSelector.addGoal(6, new SwimGoal(this));
		}

		public class MoveAttackGoal<T extends MonsterEntity> extends Goal {
			private final T entity;
			private final double moveSpeedAmp;
			private int attackTime = -1;
			private float attackRadius;
			private int attackCooldown;
			private float maxAttackDistance;
			private int seeTime;
			private boolean strafingClockwise;
			private boolean strafingBackwards;
			private int strafingTime = -1;

			public MoveAttackGoal(T mob, double moveSpeedAmpIn, int attackCooldownIn) {
				this.entity = mob;
				this.moveSpeedAmp = moveSpeedAmpIn;
				this.attackCooldown = attackCooldownIn;
				this.setMutexFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
			}

			public void setAttackCooldown(int attackCooldownIn) {
				this.attackCooldown = attackCooldownIn;
			}

			/**
			 * Returns whether execution should begin. You can also read and cache any state
			 * necessary for execution in this method as well.
			 */
			public boolean shouldExecute() {
				return this.entity.getAttackTarget() == null ? false : true;
			}

			/**
			 * Returns whether an in-progress EntityAIBase should continue executing
			 */
			public boolean shouldContinueExecuting() {
				return (this.shouldExecute() || !this.entity.getNavigator().noPath());
			}

			/**
			 * Execute a one shot task or start executing a continuous task
			 */
			public void startExecuting() {
				super.startExecuting();
				Random rand = new Random();
				this.attackRadius = rand.nextInt(30) + 10;
				this.maxAttackDistance = attackRadius * attackRadius;
				this.entity.setAggroed(true);
			}

			/**
			 * Reset the task's internal state. Called when this task is interrupted by
			 * another one
			 */
			public void resetTask() {
				super.resetTask();
				this.entity.setAggroed(true);
				this.seeTime = 0;
				this.attackTime = -1;
			}

			/**
			 * Keep ticking a continuous task that has already been started
			 */
			public void tick() {
				LivingEntity livingentity = this.entity.getAttackTarget();
				if (livingentity != null) {
					Random rand = new Random();
					if (rand.nextInt(70) == 0) {
						this.attackRadius = rand.nextInt(30) + 10;
						this.maxAttackDistance = attackRadius * attackRadius;
					}
					double d0 = this.entity.getDistanceSq(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());
					boolean flag = this.entity.getEntitySenses().canSee(livingentity);
					boolean flag1 = this.seeTime > 0;
					if (flag != flag1) {
						this.seeTime = 0;
					}
					if (flag) {
						++this.seeTime;
					} else {
						--this.seeTime;
					}
					if (!(d0 > (double) this.maxAttackDistance) && this.seeTime >= 20) {
						this.entity.getNavigator().clearPath();
						++this.strafingTime;
					} else {
						this.entity.getNavigator().tryMoveToEntityLiving(livingentity, this.moveSpeedAmp);
						this.strafingTime = -1;
					}
					if (this.strafingTime >= 20) {
						if ((double) this.entity.getRNG().nextFloat() < 0.3D) {
							this.strafingClockwise = !this.strafingClockwise;
						}
						if ((double) this.entity.getRNG().nextFloat() < 0.3D) {
							this.strafingBackwards = !this.strafingBackwards;
						}
						this.strafingTime = 0;
					}
					if (this.strafingTime > -1) {
						if (d0 > (double) (this.maxAttackDistance * 0.75F)) {
							this.strafingBackwards = false;
						} else if (d0 < (double) (this.maxAttackDistance * 0.25F)) {
							this.strafingBackwards = true;
						}
						//this.entity.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
						this.entity.setMoveForward(this.strafingBackwards ? -1F : 1F);
						this.entity.setMoveStrafing(this.strafingClockwise ? 0.5F : -0.5F);
					}
					//this is not needed. I put this only for making the mob use melee attacks.
					this.checkAndPerformAttack(livingentity, d0);
				}
			}

			protected double getAttackReachSqr(LivingEntity attackTarget) {
				return (double) (this.entity.getWidth() + attackTarget.getWidth() + 2);
			}

			//this is not needed. I put this only for making the mob use melee attacks.
			protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
				double d0 = this.getAttackReachSqr(enemy);
				this.entity.getLookController().setLookPositionWithEntity(enemy, 180.0F, 180.0F);
				this.entity.faceEntity(enemy, 180.0F, 180.0F);
				if (distToEnemySqr <= d0 && !this.entity.isSwingInProgress) {
					this.entity.swingArm(Hand.MAIN_HAND);
					this.entity.attackEntityAsMob(enemy);
				}
			}
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEFINED;
		}

		@Override
		public net.minecraft.util.SoundEvent getAmbientSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("laboratory:stupid_idle"));
		}

		@Override
		public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("laboratory:stupid_hurt"));
		}

		@Override
		public net.minecraft.util.SoundEvent getDeathSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("laboratory:stupid_death"));
		}

		@Override
		public void baseTick() {
			super.baseTick();
			double x = this.getPosX();
			double y = this.getPosY();
			double z = this.getPosZ();
			Entity entity = this;
			MagicianOnEntityTickUpdateProcedure.executeProcedure(Stream
					.of(new AbstractMap.SimpleEntry<>("world", world), new AbstractMap.SimpleEntry<>("x", x), new AbstractMap.SimpleEntry<>("y", y),
							new AbstractMap.SimpleEntry<>("z", z), new AbstractMap.SimpleEntry<>("entity", entity))
					.collect(HashMap::new, (_m, _e) -> _m.put(_e.getKey(), _e.getValue()), Map::putAll));
		}
	}
}
