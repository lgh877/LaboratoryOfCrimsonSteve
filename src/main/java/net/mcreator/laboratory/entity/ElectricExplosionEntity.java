
package net.mcreator.laboratory.entity;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.DamageSource;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.IPacket;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.CreatureAttribute;

import net.mcreator.laboratory.entity.renderer.ElectricExplosionRenderer;
import net.mcreator.laboratory.LaboratoryModElements;
import net.mcreator.laboratory.CustomMathHelper;

import javax.annotation.Nullable;

import java.util.UUID;

@LaboratoryModElements.ModElement.Tag
public class ElectricExplosionEntity extends LaboratoryModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MISC)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(128).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new).immuneToFire()
			.size(2f, 2f)).build("electric_explosion").setRegistryName("electric_explosion");

	public ElectricExplosionEntity(LaboratoryModElements instance) {
		super(instance, 59);
		FMLJavaModLoadingContext.get().getModEventBus().register(new ElectricExplosionRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
	}

	private static class EntityAttributesRegisterHandler {
		@SubscribeEvent
		public void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
			AttributeModifierMap.MutableAttribute ammma = MobEntity.func_233666_p_();
			ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3);
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 10);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 3);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends CreatureEntity {
		public int lifeTime;
		public int prevLifeTime;
		private static final DataParameter<Integer> DELAY = EntityDataManager.createKey(CustomEntity.class, DataSerializers.VARINT);
		private LivingEntity caster;
		private UUID casterUuid;

		protected void registerData() {
			super.registerData();
			this.dataManager.register(DELAY, 0);
		}

		public void readAdditional(CompoundNBT compound) {
			super.readAdditional(compound);
			this.lifeTime = compound.getInt("lifeTime");
			this.prevLifeTime = compound.getInt("prevLifeTime");
			this.dataManager.set(DELAY, compound.getInt("delayTime"));
			if (compound.hasUniqueId("Owner")) {
				this.casterUuid = compound.getUniqueId("Owner");
			}
		}

		public void writeAdditional(CompoundNBT compound) {
			super.writeAdditional(compound);
			compound.putInt("lifeTime", this.lifeTime);
			compound.putInt("prevLifeTime", this.prevLifeTime);
			compound.putInt("delayTime", this.getDelayTime());
			if (this.casterUuid != null) {
				compound.putUniqueId("Owner", this.casterUuid);
			}
		}

		public int getDelayTime() {
			return this.dataManager.get(DELAY);
		}

		public void setDelayTime(int value) {
			this.dataManager.set(DELAY, value);
		}

		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		@OnlyIn(Dist.CLIENT)
		public float getAnimationScale(float p_189795_1_) {
			return MathHelper.lerp(p_189795_1_, this.prevLifeTime, this.lifeTime);
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			experienceValue = 0;
			setNoAI(true);
			setNoGravity(true);
			setInvulnerable(true);
			enablePersistence();
		}

		protected void collideWithEntity(Entity entityIn) {
			return;
		}

		public void applyEntityCollision(Entity entityIn) {
			return;
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEFINED;
		}

		public void livingTick() {
			super.livingTick();
			if (this.getDelayTime() > 0)
				this.setDelayTime(this.getDelayTime() - 1);
			else {
				LivingEntity entity = this.getCaster();
				if (entity == null)
					entity = this;
				this.prevLifeTime = this.lifeTime;
				this.lifeTime++;
				if (this.lifeTime > 11)
					this.remove();
				else if (this.lifeTime == 1) {
					AxisAlignedBB attackRange = CustomMathHelper.makeAttackRange(this.getPosX(), this.getPosY(), this.getPosZ(), 0, 3, 3, 3);
					for (LivingEntity livingentity : this.world.getEntitiesWithinAABB(LivingEntity.class, attackRange)) {
						if (!entity.isOnSameTeam(livingentity) && livingentity != entity) {
							livingentity.hurtResistantTime = 0;
							livingentity.attackEntityFrom(DamageSource.causeMobDamage(entity), 4);
						}
					}
				}
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

		public boolean attackEntityFrom(DamageSource source, float a) {
			return false;
		}

		@Override
		public boolean canDespawn(double distanceToClosestPlayer) {
			return false;
		}

		@Override
		public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(""));
		}

		@Override
		public net.minecraft.util.SoundEvent getDeathSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(""));
		}
	}
}
