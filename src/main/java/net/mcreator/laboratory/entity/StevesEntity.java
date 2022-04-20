
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
import net.minecraft.world.IServerWorld;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.DamageSource;
import net.minecraft.network.IPacket;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.Item;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.CreatureAttribute;

import net.mcreator.laboratory.itemgroup.MobsOfLaboratoryItemGroup;
import net.mcreator.laboratory.entity.renderer.StevesRenderer;
import net.mcreator.laboratory.LaboratoryModElements;

import javax.annotation.Nullable;

import java.util.UUID;

@LaboratoryModElements.ModElement.Tag
public class StevesEntity extends LaboratoryModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(0.6f, 1.8f)).build("steves").setRegistryName("steves");

	public StevesEntity(LaboratoryModElements instance) {
		super(instance, 64);
		FMLJavaModLoadingContext.get().getModEventBus().register(new StevesRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -16737895, -10066432, new Item.Properties().group(MobsOfLaboratoryItemGroup.tab))
				.setRegistryName("steves_spawn_egg"));
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
			ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.6);
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 50);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 64);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 3);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 3);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends MonsterEntity {
		private LivingEntity caster;
		private UUID casterUuid;

		/*
		protected float radius;
		protected float angleYaw;
		protected float offsetY;
		private float parentYaw = 0;
		*/
		public void readAdditional(CompoundNBT compound) {
			super.readAdditional(compound);
			if (compound.hasUniqueId("Owner")) {
				this.casterUuid = compound.getUniqueId("Owner");
			}
			//this.angleYaw = compound.getFloat("PartAngle");
			//this.radius = compound.getFloat("PartRadius");
		}

		public void writeAdditional(CompoundNBT compound) {
			super.writeAdditional(compound);
			if (this.casterUuid != null) {
				compound.putUniqueId("Owner", this.casterUuid);
			}
			//compound.putFloat("PartAngle", angleYaw);
			//compound.putFloat("PartRadius", radius);
		}

		public void setCaster(@Nullable LivingEntity p_190549_1_) {
			this.caster = p_190549_1_;
			this.casterUuid = p_190549_1_ == null ? null : p_190549_1_.getUniqueID();
		}

		@Nullable
		public LivingEntity getCaster() {
			if (this.caster == null && this.casterUuid != null && this.world instanceof ServerWorld) {
				Entity entity = ((ServerWorld) this.world).getEntityByUuid(this.casterUuid);
				if (entity instanceof LivingEntity) {
					this.caster = (LivingEntity) entity;
				}
			}
			return this.caster;
		}

		protected void collideWithEntity(Entity entityIn) {
			if (entityIn instanceof LivingEntity && !this.isOnSameTeam(entityIn) && !this.isSwingInProgress) {
				this.swingArm(Hand.MAIN_HAND);
				this.attackEntityAsMob(entityIn);
			}
			super.collideWithEntity(entityIn);
		}

		/*
				protected void collideWithNearbyEntities() {
				}
		*/
		public boolean isOnSameTeam(Entity entityIn) {
			if (this.getTeam() != null)
				return super.isOnSameTeam(entityIn);
			else if (entityIn instanceof CustomEntity)
				return true;
			else
				return false;
		}

		public boolean onLivingFall(float distance, float b) {
			return false;
		}

		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			setNoAI(false);
		}

		protected float getJumpFactor() {
			return 4;
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, LivingEntity.class, false, false));
			this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2, false));
			this.goalSelector.addGoal(3, new RandomWalkingGoal(this, 1));
			this.targetSelector.addGoal(4, new HurtByTargetGoal(this));
			this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
			this.goalSelector.addGoal(6, new SwimGoal(this));
		}

		public boolean isInvulnerableTo(DamageSource source) {
			return source == DamageSource.CRAMMING;
		}

		protected void updateMovementGoalFlags() {
			boolean flag = !(this.getControllingPassenger() instanceof MobEntity) || this.getCaster() == null;
			boolean flag1 = !(this.getRidingEntity() instanceof BoatEntity);
			this.goalSelector.setFlag(Goal.Flag.MOVE, flag);
			this.goalSelector.setFlag(Goal.Flag.JUMP, flag && flag1);
			this.goalSelector.setFlag(Goal.Flag.LOOK, flag);
			this.goalSelector.setFlag(Goal.Flag.TARGET, flag);
		}

		public void tick() {
			super.tick();
			LivingEntity owner = this.getCaster();
			//recalculateSize();
			if (owner != null && owner.isAlive()) {
				LivingEntity ownerTarget = ((MobEntity) owner).getAttackTarget();
				/*
				double x = (owner.prevPosX + this.prevPosX * 10) / 11;
				double y = (owner.prevPosY + this.prevPosY * 10) / 11;
				double z = (owner.prevPosZ + this.prevPosZ * 10) / 11;
				this.moveForced(x, y, z);
				*/
				/*
				double x = owner.prevPosX;
				double y = owner.prevPosY;
				double z = owner.prevPosZ;
				*/
				double x = owner.getPosX();
				double y = owner.getPosY();
				double z = owner.getPosZ();
				Vector3d posVector = new Vector3d(x - this.getPosX(), y - this.getPosY(), z - this.getPosZ());
				this.setMotion(posVector.scale(0.4 * MathHelper.clamp((this.getDistanceSq(owner)) * 0.3f - 1, 0, 1)));
				//this.markVelocityChanged();
				this.rotationYawHead = owner.prevRotationYawHead;
				this.rotationYaw = owner.prevRotationYaw;
				this.rotationPitch = owner.prevRotationPitch;
				if (ownerTarget != null)
					this.setAttackTarget(ownerTarget);
			}
			/*
			super.tick();
			Entity parent = getCaster();
			recalculateSize();
			if (parent != null && !world.isRemote && parent.isAlive()) {
				float f = this.getDistance(parent);
				this.setNoGravity(true);
				this.faceEntity(parent, 1, 1);
				this.parentYaw = this.limitAngle(this.parentYaw, parent.prevRotationYaw, 5.0F);
				double yD1 = (parent.getPosY() - this.getPosY()) / (double) f;
				double ySet = parent.prevPosY;
				if (!world.getBlockState(new BlockPos(this.getPosX(), ySet - 0.1, this.getPosZ())).isSolid()) {
					ySet = parent.prevPosY - 0.2F;
				}
				if (this.isEntityInsideOpaqueBlock() || world.getBlockState(new BlockPos(this.getPosX(), ySet, this.getPosZ())).isSolid()) {
					ySet = parent.prevPosY + 0.2F;
				}
				double yaw = parentYaw;
				double x = parent.prevPosX + this.radius * Math.cos(yaw * (Math.PI / 180.0F) + this.angleYaw);
				double z = parent.prevPosZ + this.radius * Math.sin(yaw * (Math.PI / 180.0F) + this.angleYaw);
				//this.moveForced(x, ySet, z);
				this.setPosition(x, ySet, z);
				double d0 = parent.getPosX() - this.getPosX();
				double d1 = parent.getPosY() - this.getPosY();
				double d2 = parent.getPosZ() - this.getPosZ();
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				float f2 = -((float) (MathHelper.atan2(d1, MathHelper.sqrt(d0 * d0 + d2 * d2)) * (double) (180F / (float) Math.PI)));
				this.rotationPitch = this.limitAngle(this.rotationPitch, f2, 5.0F);
				this.markVelocityChanged();
				this.rotationYaw = parentYaw;
				this.rotationYawHead = this.rotationYaw;
				this.renderYawOffset = this.prevRotationYaw;
			} else {
				this.setNoGravity(false);
			}
			*/
		}

		/*
				protected float limitAngle(float sourceAngle, float targetAngle, float maximumChange) {
					float f = MathHelper.wrapDegrees(targetAngle - sourceAngle);
					if (f > maximumChange) {
						f = maximumChange;
					}
					if (f < -maximumChange) {
						f = -maximumChange;
					}
					float f1 = sourceAngle + f;
					if (f1 < 0.0F) {
						f1 += 360.0F;
					} else if (f1 > 360.0F) {
						f1 -= 360.0F;
					}
					return f1;
				}
		*/
		public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason,
				@Nullable ILivingEntityData livingdata, @Nullable CompoundNBT tag) {
			ILivingEntityData retval = super.onInitialSpawn(world, difficulty, reason, livingdata, tag);
			if (this.rand.nextInt(20) > 0 && world instanceof ServerWorld) {
				StevesEntity.CustomEntity entityToSpawn = new StevesEntity.CustomEntity(StevesEntity.entity, (World) world);
				entityToSpawn.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), (float) 0, (float) 0);
				entityToSpawn.setCaster(this);
				entityToSpawn.onInitialSpawn((ServerWorld) world, world.getDifficultyForLocation(entityToSpawn.getPosition()),
						SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
				world.addEntity(entityToSpawn);
			}
			return retval;
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
