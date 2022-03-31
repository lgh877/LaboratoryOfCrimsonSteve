
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.DamageSource;
import net.minecraft.network.IPacket;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.ItemGroup;
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
		elements.items.add(() -> new SpawnEggItem(entity, -16737895, -10066432, new Item.Properties().group(ItemGroup.MISC))
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

		public void readAdditional(CompoundNBT compound) {
			super.readAdditional(compound);
			if (compound.hasUniqueId("Owner")) {
				this.casterUuid = compound.getUniqueId("Owner");
			}
		}

		public void writeAdditional(CompoundNBT compound) {
			super.writeAdditional(compound);
			if (this.casterUuid != null) {
				compound.putUniqueId("Owner", this.casterUuid);
			}
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

		public void applyEntityCollision(Entity entityIn) {
		}

		protected void collideWithEntity(Entity entityIn) {
			if (entityIn instanceof LivingEntity && !this.isOnSameTeam(entityIn) && !this.isSwingInProgress) {
				this.swingArm(Hand.MAIN_HAND);
				this.attackEntityAsMob(entityIn);
			}
		}

		protected void collideWithNearbyEntities() {
		}

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
			experienceValue = 2;
			setNoAI(false);
			this.stepHeight = 10;
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
			this.goalSelector.addGoal(3, new RandomWalkingGoal(this, 1, 1));
			this.targetSelector.addGoal(4, new HurtByTargetGoal(this));
			this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
			this.goalSelector.addGoal(6, new SwimGoal(this));
		}

		protected void updateMovementGoalFlags() {
			boolean flag = !(this.getControllingPassenger() instanceof MobEntity) || this.getCaster() == null;
			boolean flag1 = !(this.getRidingEntity() instanceof BoatEntity);
			this.goalSelector.setFlag(Goal.Flag.MOVE, flag);
			this.goalSelector.setFlag(Goal.Flag.JUMP, flag && flag1);
			this.goalSelector.setFlag(Goal.Flag.LOOK, flag);
			this.goalSelector.setFlag(Goal.Flag.TARGET, flag);
		}

		public void livingTick() {
			super.livingTick();
			LivingEntity owner = this.getCaster();
			if (owner != null && owner.isAlive()) {
				LivingEntity ownerTarget = ((MobEntity) owner).getAttackTarget();
				if (!this.world.isRemote()) {
					this.setPositionAndUpdate(owner.prevPosX, owner.prevPosY, owner.prevPosZ);
					this.rotationYawHead = owner.prevRotationYawHead;
					this.rotationYaw = owner.prevRotationYaw;
					this.rotationPitch = owner.prevRotationPitch;
				}
				if (ownerTarget != null)
					this.setAttackTarget(ownerTarget);
			}
		}

		public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason,
				@Nullable ILivingEntityData livingdata, @Nullable CompoundNBT tag) {
			ILivingEntityData retval = super.onInitialSpawn(world, difficulty, reason, livingdata, tag);
			if (this.rand.nextInt(6) > 0 && world instanceof ServerWorld) {
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
