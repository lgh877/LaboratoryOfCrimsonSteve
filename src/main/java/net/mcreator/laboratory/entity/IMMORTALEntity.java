
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
import net.minecraft.world.Explosion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.DamageSource;
import net.minecraft.network.IPacket;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.Item;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.CreatureAttribute;

import net.mcreator.laboratory.itemgroup.MobsOfLaboratoryItemGroup;
import net.mcreator.laboratory.entity.renderer.IMMORTALRenderer;
import net.mcreator.laboratory.LaboratoryModElements;

@LaboratoryModElements.ModElement.Tag
public class IMMORTALEntity extends LaboratoryModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(0.6f, 1.8f)).build("immortal").setRegistryName("immortal");

	public IMMORTALEntity(LaboratoryModElements instance) {
		super(instance, 46);
		FMLJavaModLoadingContext.get().getModEventBus().register(new IMMORTALRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -10737946, -12730946, new Item.Properties().group(MobsOfLaboratoryItemGroup.tab))
				.setRegistryName("immortal_spawn_egg"));
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
			ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.7);
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 1);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 64);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 5);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 2);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends MonsterEntity {
		public float deathTicks;

		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			stepHeight = 50;
			setNoAI(false);
			enablePersistence();
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		public boolean isOnSameTeam(Entity entityIn) {
			if (entityIn == null) {
				return false;
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

		public void onKillCommand() {
			if (this.world instanceof World && !((World) world).isRemote)
				remove();
			super.onKillCommand();
		}

		public boolean onLivingFall(float distance, float damageMultiplier) {
			return false;
		}

		protected void onDeathUpdate() {
			double x1 = this.getPosX();
			double y1 = this.getPosY();
			double z1 = this.getPosZ();
			int level = 6;
			this.deathTicks++;
			this.deathTime = (int) (this.deathTicks / 20);
			if (this.deathTicks % 2.0F == 0.0F)
				this.playSound(this.getLaughSound(), 3.0F, 0.5F + this.deathTicks / 150.0F);
			if (this.deathTicks > 300.0F) {
				if (this.world instanceof World && !((World) world).isRemote)
					((World) world).createExplosion(this, (int) x1, (int) y1, (int) z1, level, Explosion.Mode.BREAK);
				this.remove();
			}
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, MobEntity.class, false, false));
			this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2, false));
			this.goalSelector.addGoal(3, new RandomWalkingGoal(this, 1));
			this.targetSelector.addGoal(4, new HurtByTargetGoal(this));
			this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
			this.goalSelector.addGoal(6, new SwimGoal(this));
		}

		public void livingTick() {
			super.livingTick();
			if (this.deathTime > 5) {
				this.setSprinting(!this.isSprinting());
			} else
				this.setSprinting(false);
			if (this.deathTicks < 200 && this.rand.nextInt(10) == 0)
				setHealth(1);
			setRotation((float) Math.random() * 360, (float) Math.random() * 360);
			setRotationYawHead((float) Math.random() * 360);
		}

		public boolean attackEntityFrom(DamageSource source, float amount) {
			boolean flag = super.attackEntityFrom(source, amount);
			return flag;
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.ILLAGER;
		}

		@Override
		public boolean canDespawn(double distanceToClosestPlayer) {
			return false;
		}

		@Override
		public net.minecraft.util.SoundEvent getAmbientSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("laboratory:stupid_idle"));
		}

		public net.minecraft.util.SoundEvent getLaughSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("laboratory:stupid_laugh"));
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
