
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
import net.minecraftforge.common.DungeonHooks;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.World;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.DamageSource;
import net.minecraft.potion.EffectUtils;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.IPacket;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.Item;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.Pose;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.CreatureAttribute;

import net.mcreator.laboratory.itemgroup.MobsOfLaboratoryItemGroup;
import net.mcreator.laboratory.item.SoulEnergyLauncherItem;
import net.mcreator.laboratory.entity.renderer.DreadfulSteveGhoulRenderer;
import net.mcreator.laboratory.LaboratoryModElements;

import java.util.function.Predicate;
import java.util.Random;
import java.util.EnumSet;

@LaboratoryModElements.ModElement.Tag
public class DreadfulSteveGhoulEntity extends LaboratoryModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER).immuneToFire()
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new).size(2f, 3.5f))
			.build("dreadful_steve_ghoul").setRegistryName("dreadful_steve_ghoul");

	public DreadfulSteveGhoulEntity(LaboratoryModElements instance) {
		super(instance, 49);
		FMLJavaModLoadingContext.get().getModEventBus().register(new DreadfulSteveGhoulRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -14804197, -14607846, new Item.Properties().group(MobsOfLaboratoryItemGroup.tab))
				.setRegistryName("dreadful_steve_ghoul_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		boolean biomeCriteria = false;
		if (new ResourceLocation("soul_sand_valley").equals(event.getName()))
			biomeCriteria = true;
		if (!biomeCriteria)
			return;
		event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(entity, 20, 1, 1));
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
		EntitySpawnPlacementRegistry.register(entity, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
				MonsterEntity::canMonsterSpawn);
		DungeonHooks.addDungeonMob(entity, 50);
	}

	private static class EntityAttributesRegisterHandler {
		@SubscribeEvent
		public void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
			AttributeModifierMap.MutableAttribute ammma = MobEntity.func_233666_p_();
			ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3);
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 80);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 80);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 7);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 4);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends MonsterEntity {
		private static final DataParameter<Boolean> ATTACK_STATE = EntityDataManager.createKey(CustomEntity.class, DataSerializers.BOOLEAN);
		private static final DataParameter<Boolean> SHOOT_STATE = EntityDataManager.createKey(CustomEntity.class, DataSerializers.BOOLEAN);
		private static final DataParameter<Boolean> SUMMON_STATE = EntityDataManager.createKey(CustomEntity.class, DataSerializers.BOOLEAN);
		//private static final DataParameter<Integer> DEATH_TICKS = EntityDataManager.createKey(CustomEntity.class, DataSerializers.VARINT);
		public int prevHurtTime;
		public int attackProgress;
		//public int prevDeathTicks;
		public boolean shootActive;
		public boolean canAttack;
		private float[] clientSideStandAnimation0 = new float[3];
		private float[] clientSideStandAnimation = new float[3];
		private static final Predicate<LivingEntity> NOT_UNDEAD = (p_213797_0_) -> {
			return p_213797_0_.getCreatureAttribute() != CreatureAttribute.UNDEAD && p_213797_0_.attackable();
		};

		public boolean isOnSameTeam(Entity entityIn) {
			if (this.getTeam() != null)
				return super.isOnSameTeam(entityIn);
			else if (entityIn instanceof DreadfulSteveGhoulEntity.CustomEntity)
				return true;
			else
				return false;
		}

		/*protected void onDeathUpdate() {
			this.prevDeathTicks = this.getDeathTicks();
			this.setDeathTicks(this.getDeathTicks() + 1);
			if (this.getDeathTicks() > 200)
				this.remove();
		}
		
		@OnlyIn(Dist.CLIENT)
		public float getDeathAnimationScale(float p_189795_1_) {
			return MathHelper.lerp(p_189795_1_, this.prevDeathTicks, this.getDeathTicks());
		}
		public void readAdditional(CompoundNBT compound) {
			super.readAdditional(compound);
			this.dataManager.set(DEATH_TICKS, compound.getInt("deathTicks"));
		}
		public void writeAdditional(CompoundNBT compound) {
			super.writeAdditional(compound);
			compound.putInt("deathTicks", this.getDeathTicks());
		}
		public void setDeathTicks(int value) {
			this.dataManager.set(DEATH_TICKS, value);
		}
		public int getDeathTicks() {
			return this.dataManager.get(DEATH_TICKS);
		}
		*/
		public void setAttackState(boolean value) {
			this.dataManager.set(ATTACK_STATE, value);
		}

		public boolean getAttackState() {
			return this.dataManager.get(ATTACK_STATE);
		}

		public void setShootState(boolean value) {
			this.dataManager.set(SHOOT_STATE, value);
		}

		public boolean getShootState() {
			return this.dataManager.get(SHOOT_STATE);
		}

		public void setSummonState(boolean value) {
			this.dataManager.set(SUMMON_STATE, value);
		}

		public boolean getSummonState() {
			return this.dataManager.get(SUMMON_STATE);
		}

		protected void registerData() {
			super.registerData();
			this.dataManager.register(ATTACK_STATE, false);
			this.dataManager.register(SHOOT_STATE, false);
			this.dataManager.register(SUMMON_STATE, false);
			//this.dataManager.register(DEATH_TICKS, 0);
		}

		protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
			return 3.4f;
		}

		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			experienceValue = 0;
			setNoAI(false);
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.goalSelector.addGoal(1, new CustomEntity.ShootGoal(this));
			this.goalSelector.addGoal(1, new CustomEntity.MoveAttackGoal(this, 1.2, 0, 48));
			this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, false, false, NOT_UNDEAD));
			this.goalSelector.addGoal(3, new RandomWalkingGoal(this, 1));
			this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
			this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
			this.goalSelector.addGoal(6, new SwimGoal(this));
		}

		public class MoveAttackGoal<T extends MonsterEntity> extends Goal {
			private final T entity;
			private final double moveSpeedAmp;
			private int attackTime = -1;
			private final float attackRadius;
			private int attackCooldown;
			private final float maxAttackDistance;
			private int seeTime;
			private boolean strafingClockwise;
			private boolean strafingBackwards;
			private int strafingTime = -1;
			private int field_234037_i_;

			public MoveAttackGoal(T mob, double moveSpeedAmpIn, int attackCooldownIn, float maxAttackDistanceIn) {
				this.entity = mob;
				this.moveSpeedAmp = moveSpeedAmpIn;
				this.attackCooldown = attackCooldownIn;
				this.attackRadius = maxAttackDistanceIn;
				this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
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
				this.entity.setAggroed(true);
				this.field_234037_i_ = 0;
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
				CustomEntity.this.setAttackState(false);
			}

			/**
			 * Keep ticking a continuous task that has already been started
			 */
			public void tick() {
				LivingEntity livingentity = this.entity.getAttackTarget();
				if (livingentity != null) {
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
						this.entity.faceEntity(livingentity, 30.0F, 30.0F);
					} else {
						this.entity.getLookController().setLookPositionWithEntity(livingentity, 30.0F, 30.0F);
					}
					/*if (this.entity.isHandActive()) {
						if (!flag && this.seeTime < -60) {
							this.entity.resetActiveHand();
						} else if (flag) {
							int i = this.entity.getItemInUseMaxCount();
							if (i >= 80) {
								float f = MathHelper.sqrt(d0) / this.attackRadius;
								float lvt_5_1_ = MathHelper.clamp(f, 0.1F, 1.0F);
								this.entity.resetActiveHand();
								this.entity.attackEntityWithRangedAttack(livingentity, lvt_5_1_);
								this.attackTime = this.attackCooldown;
							}
						}
					} else if (--this.attackTime <= 0 && this.seeTime >= -60) {
						this.entity.setActiveHand(ProjectileHelper.getWeaponHoldingHand(this.entity, item -> item instanceof ShieldItem));
					}
					*/
					this.field_234037_i_ = Math.max(this.field_234037_i_ - 1, 0);
					this.checkAndPerformAttack(livingentity, d0);
				}
			}

			protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
				double d0 = this.getAttackReachSqr(enemy);
				if (distToEnemySqr <= d0 * 2 && this.func_234040_h_()) {
					this.func_234039_g_();
					this.entity.attackEntityAsMob(enemy);
					CustomEntity.this.setAttackState(false);
				} else if (distToEnemySqr <= d0 * 3) {
					if (this.func_234040_h_()) {
						CustomEntity.this.setAttackState(false);
						this.func_234039_g_();
					}
					if (this.func_234041_j_() <= 40 - (1 + EffectUtils.getMiningSpeedup(this.entity)) * 4 && CustomEntity.this.canAttack) {
						CustomEntity.this.setAttackState(true);
						CustomEntity.this.canAttack = false;
					}
				} else {
					this.func_234039_g_();
					CustomEntity.this.setAttackState(false);
				}
			}

			protected void func_234039_g_() {
				this.field_234037_i_ = 20;
			}

			protected boolean func_234040_h_() {
				return this.field_234037_i_ <= 0;
			}

			protected int func_234041_j_() {
				return this.field_234037_i_;
			}

			protected double getAttackReachSqr(LivingEntity attackTarget) {
				return (double) (4.0F + attackTarget.getWidth());
			}
		}

		public class ShootGoal extends Goal {
			private Random random = new Random();
			private int bullet;
			private int shootCooltime = 60;
			private CustomEntity entityIn;

			public ShootGoal(CustomEntity mob) {
				entityIn = mob;
			}

			public boolean shouldExecute() {
				return true;
			}

			public void tick() {
				if (this.bullet > 0) {
					if (entityIn.shootActive) {
						this.bullet--;
						if (this.bullet == 0) {
							this.shootCooltime = 40 + random.nextInt(40);
							entityIn.playSound(SoundEvents.ENTITY_WITHER_SPAWN, 2, 2f);
						}
						entityIn.rotationYaw = entityIn.getYaw(1);
						entityIn.prevRotationYaw = entityIn.rotationYaw;
						if (bullet % 5 == 0) {
							entityIn.swingArm(Hand.MAIN_HAND);
							entityIn.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 2, 0.5f);
							if (world instanceof ServerWorld) {
								float f = (entityIn.renderYawOffset + (float) (180 * 1)) * ((float) Math.PI / 180F);
								float f1 = MathHelper.cos(f);
								float f2 = MathHelper.sin(f);
								double x = entityIn.getPosX() + f1;
								double y = entityIn.getPosY() + 2.5;
								double z = entityIn.getPosZ() + f2;
								AbstractArrowEntity entityToSpawn = new SoulEnergyLauncherItem.ArrowCustomEntity(SoulEnergyLauncherItem.arrow,
										entityIn, (World) world);
								entityToSpawn.setLocationAndAngles(x, y, z, 0, 0);
								entityToSpawn.setDamage(4);
								entityToSpawn.setKnockbackStrength(1);
								if (this.entityIn.getAttackTarget() == null)
									entityIn.shoot(entityIn.getLookVec().x - f1 * 0.3f, entityIn.getLookVec().y, entityIn.getLookVec().z - f2 * 0.3f,
											3, 4, entityToSpawn);
								else {
									LivingEntity targetIn = this.entityIn.getAttackTarget();
									Vector3d posVector = new Vector3d(targetIn.getPosX() - entityIn.getPosX(),
											targetIn.getPosY() + targetIn.getEyeHeight() - entityIn.getPosY() - 2.5,
											targetIn.getPosZ() - entityIn.getPosZ());
									entityIn.shoot(posVector.x - f1, posVector.y, posVector.z - f2, 3, 4, entityToSpawn);
								}
								world.addEntity(entityToSpawn);
							}
						}
					}
				} else {
					entityIn.setShootState(false);
					if (this.shootCooltime > 0)
						this.shootCooltime--;
					else if (entityIn.getAttackTarget() != null) {
						this.bullet = 10 + random.nextInt(31);
						entityIn.playSound(SoundEvents.ENTITY_WITHER_SPAWN, 2, 1f);
						entityIn.setShootState(true);
					}
				}
			}
		}

		public void shoot(double x, double y, double z, float velocity, float inaccuracy, Entity entityIn) {
			Vector3d vector3d = (new Vector3d(x, y, z)).normalize()
					.add(this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy,
							this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy,
							this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy)
					.scale((double) velocity);
			entityIn.setMotion(vector3d);
			float f = MathHelper.sqrt(horizontalMag(vector3d));
			entityIn.rotationYaw = (float) (MathHelper.atan2(vector3d.x, vector3d.z) * (double) (180F / (float) Math.PI));
			entityIn.rotationPitch = (float) (MathHelper.atan2(vector3d.y, (double) f) * (double) (180F / (float) Math.PI));
			entityIn.prevRotationYaw = entityIn.rotationYaw;
			entityIn.prevRotationPitch = entityIn.rotationPitch;
		}

		public int getAttackProgressEnd() {
			return 20 - (1 + EffectUtils.getMiningSpeedup(this)) * 2;
		}

		public void livingTick() {
			int i = this.getAttackProgressEnd();
			super.livingTick();
			if (this.isAlive()) {
				this.clientSideStandAnimation0[0] = this.clientSideStandAnimation[0];
				this.clientSideStandAnimation0[1] = this.clientSideStandAnimation[1];
				if (this.getShootState()) {
					this.clientSideStandAnimation[0] = MathHelper.clamp(this.clientSideStandAnimation[0] + 1.0F, 0.0F, 6.0F);
					if (this.clientSideStandAnimation[0] == 6)
						this.shootActive = true;
				} else {
					this.clientSideStandAnimation[0] = MathHelper.clamp(this.clientSideStandAnimation[0] - 1.0F, 0.0F, 6.0F);
					this.shootActive = false;
				}
				if (this.getAttackState()) {
					this.clientSideStandAnimation[1] = MathHelper.clamp(this.clientSideStandAnimation[1] + 1.0F, 0.0F, i);
				} else {
					this.clientSideStandAnimation[1] = MathHelper.clamp(this.clientSideStandAnimation[1] - 4, 0.0F, i);
					if (clientSideStandAnimation[1] == 0)
						this.canAttack = true;
				}
			}
		}

		public void baseTick() {
			this.prevHurtTime = this.hurtTime;
			super.baseTick();
		}

		public boolean attackEntityFrom(DamageSource source, float amount) {
			if (source == DamageSource.WITHER || (source.getTrueSource() instanceof LivingEntity && this.isOnSameTeam(source.getTrueSource())))
				return false;
			return super.attackEntityFrom(source, amount);
		}

		@OnlyIn(Dist.CLIENT)
		public float getAnimationScale(float p_189795_1_, int i) {
			return MathHelper.lerp(p_189795_1_, this.clientSideStandAnimation0[i], this.clientSideStandAnimation[i]);
		}

		@OnlyIn(Dist.CLIENT)
		public float getHurtAnimationScale(float p_189795_1_) {
			return MathHelper.lerp(p_189795_1_, this.prevHurtTime, this.hurtTime);
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEAD;
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
	}
}
