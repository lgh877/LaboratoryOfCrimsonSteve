
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
import net.minecraft.util.DamageSource;
import net.minecraft.network.IPacket;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.Item;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.Pose;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.CreatureAttribute;

import net.mcreator.laboratory.itemgroup.MobsOfLaboratoryItemGroup;
import net.mcreator.laboratory.entity.renderer.StevokerRenderer;
import net.mcreator.laboratory.LaboratoryModElements;

@LaboratoryModElements.ModElement.Tag
public class StevokerEntity extends LaboratoryModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(0.6f, 1.8f)).build("stevoker").setRegistryName("stevoker");

	public StevokerEntity(LaboratoryModElements instance) {
		super(instance, 43);
		FMLJavaModLoadingContext.get().getModEventBus().register(new StevokerRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -1, -1, new Item.Properties().group(MobsOfLaboratoryItemGroup.tab))
				.setRegistryName("stevoker_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(entity, 20, 4, 4));
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
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 120);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 64);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 3);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends EvokerEntity {
		public int celebrateTime;

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

		public boolean isOnSameTeam(Entity entityIn) {
			if (entityIn == null) {
				return false;
			} else if (entityIn == this) {
				return true;
			} else if (super.isOnSameTeam(entityIn)) {
				return true;
			} else if (entityIn instanceof VexEntity) {
				return this.isOnSameTeam(((VexEntity) entityIn).getOwner());
			} else if (entityIn instanceof LivingEntity && ((LivingEntity) entityIn).getCreatureAttribute() == CreatureAttribute.ILLAGER) {
				return this.getTeam() == null && entityIn.getTeam() == null;
			} else {
				return false;
			}
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 0.5, false));
			this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, LivingEntity.class, false, false));
		}

		public void livingTick() {
			super.livingTick();
			if (this.celebrateTime > 0) {
				this.celebrateTime--;
			}
			if (this.isSpellcasting()) {
				if (this.rand.nextInt(2) == 0)
					this.setPose(Pose.FALL_FLYING);
				else
					this.setPose(Pose.STANDING);
			} else {
				this.setPose(Pose.STANDING);
				this.setSneaking(false);
			}
			if (this.getCelebrating() && this.onGround) {
				this.jump();
				this.startSpinAttack(3);
			}
		}

		public void awardKillScore(Entity killed, int scoreValue, DamageSource damageSource) {
			super.awardKillScore(killed, scoreValue, damageSource);
			this.celebrateTime = 60;
		}

		public boolean getCelebrating() {
			if (this.celebrateTime > 0)
				return true;
			else
				return super.getCelebrating();
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.ILLAGER;
		}

		@Override
		public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.hurt"));
		}

		@Override
		public net.minecraft.util.SoundEvent getDeathSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.death"));
		}
	}
}
