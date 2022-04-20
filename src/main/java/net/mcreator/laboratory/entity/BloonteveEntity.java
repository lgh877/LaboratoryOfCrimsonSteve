
package net.mcreator.laboratory.entity;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.DamageSource;
import net.minecraft.network.IPacket;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Item;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.CreatureAttribute;

import net.mcreator.laboratory.entity.renderer.BloonteveRenderer;
import net.mcreator.laboratory.LaboratoryModElements;

import javax.annotation.Nullable;

import java.util.UUID;
import java.util.EnumSet;

@LaboratoryModElements.ModElement.Tag
public class BloonteveEntity extends LaboratoryModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(0.6f, 1.8f)).build("bloonteve").setRegistryName("bloonteve");

	public BloonteveEntity(LaboratoryModElements instance) {
		super(instance, 61);
		FMLJavaModLoadingContext.get().getModEventBus().register(new BloonteveRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -11513776, -11513776, new Item.Properties().group(ItemGroup.MISC))
				.setRegistryName("bloonteve_spawn_egg"));
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
	}

	private static class EntityAttributesRegisterHandler {
		@SubscribeEvent
		public void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
			AttributeModifierMap.MutableAttribute ammma = MobEntity.func_233666_p_();
			ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3);
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 20);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 3);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends CreatureEntity {
		private MobEntity caster;
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

		public void setCaster(@Nullable MobEntity p_190549_1_) {
			this.caster = p_190549_1_;
			this.casterUuid = p_190549_1_ == null ? null : p_190549_1_.getUniqueID();
		}

		@Nullable
		public MobEntity getCaster() {
			if (this.caster == null && this.casterUuid != null && this.world instanceof ServerWorld) {
				Entity entity = ((ServerWorld) this.world).getEntityByUuid(this.casterUuid);
				if (entity instanceof MobEntity) {
					this.caster = (MobEntity) entity;
				}
			}
			return this.caster;
		}

		class setAttackTargetSameAsSummoner extends Goal {
			private MobEntity summoner;
			private final CustomEntity mob;

			public setAttackTargetSameAsSummoner(CustomEntity mobIn) {
				this.mob = mobIn;
				this.setMutexFlags(EnumSet.of(Goal.Flag.TARGET));
			}

			public boolean shouldExecute() {
				if (this.mob.getCaster() != null && this.mob.getCaster().isAlive()) {
					return this.summoner.getAttackTarget() != null;
				}
				return false;
			}

			public void startExecuting() {
				this.summoner = this.mob.getCaster();
			}

			public boolean shouldContinueExecuting() {
				return this.mob.getAttackTarget() != this.summoner;
			}

			public void tick() {
				this.mob.setAttackTarget(summoner.getAttackTarget());
			}
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

		public void livingTick() {
			super.livingTick();
			this.setMotion(this.getMotion().add(0, 0.1, 0));
			if (this.getCaster() != null && this.getCaster().isAlive()) {
				float f = (float) MathHelper.atan2(this.getCaster().getPosZ() - this.getPosZ(), this.getCaster().getPosX() - this.getPosX());
				if (this.getDistanceSq(this.getCaster()) < 64 || this.getPosY() < this.getCaster().getPosY())
					this.setMotion(this.getMotion().add((Math.random() - 0.5) * 0.03, 0, (Math.random() - 0.5) * 0.03));
				else {
					this.setMotion(this.getMotion().add((double) MathHelper.cos(f) * this.getDistanceSq(this.getCaster()) * 0.003,
							-0.003 * this.getDistanceSq(this.getCaster()),
							(double) MathHelper.sin(f) * this.getDistanceSq(this.getCaster()) * 0.003));
					this.getCaster()
							.setMotion(this.getCaster().getMotion().add(-(double) MathHelper.cos(f) * this.getDistanceSq(this.getCaster()) * 0.0008,
									0.0018 * Math.random() * this.getDistanceSq(this.getCaster()),
									-(double) MathHelper.sin(f) * this.getDistanceSq(this.getCaster()) * 0.0008));
					if (this.getCaster().fallDistance > 0)
						this.getCaster().fallDistance--;
				}
			} else {
				this.attackEntityFrom(DamageSource.OUT_OF_WORLD, 3);
			}
		}

		public boolean onLivingFall(float a, float b) {
			return false;
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEFINED;
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
