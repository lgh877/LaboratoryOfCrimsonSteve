
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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.Direction;
import net.minecraft.util.DamageSource;
import net.minecraft.network.IPacket;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Item;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
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
import net.minecraft.block.BlockState;

import net.mcreator.laboratory.entity.renderer.ReturnedSpectriteSteveRenderer;
import net.mcreator.laboratory.LaboratoryModElements;

import java.util.Random;
import java.util.EnumSet;

@LaboratoryModElements.ModElement.Tag
public class ReturnedSpectriteSteveEntity extends LaboratoryModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(0.6f, 1.8f)).build("returned_spectrite_steve").setRegistryName("returned_spectrite_steve");
	public ReturnedSpectriteSteveEntity(LaboratoryModElements instance) {
		super(instance, 21);
		FMLJavaModLoadingContext.get().getModEventBus().register(new ReturnedSpectriteSteveRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -10053376, -52429, new Item.Properties().group(ItemGroup.MISC))
				.setRegistryName("returned_spectrite_steve_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(entity, 20, 1, 1));
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
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 200);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 150);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 10);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 5);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 1);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_SPEED, 10);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends MonsterEntity {
		private Entity attackedTarget;
		private int attackCount;
		public float deathTicks;
		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			experienceValue = 0;
			stepHeight = 40;
			jumpMovementFactor = 0.3f;
			setNoAI(false);
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		public float getJumpFactor() {
			return 3;
		}

		protected void onDeathUpdate() {
			double x = this.getPosXRandom(1.5);
			double y = this.getPosYRandom();
			double z = this.getPosZRandom(1.5);
			double x1 = this.getPosX();
			double y1 = this.getPosY();
			double z1 = this.getPosZ();
			int level = 6;
			this.deathTicks++;
			this.setPositionAndUpdate(x, y, z);
			if (this.deathTicks % 2.0F == 0.0F)
				this.playSound(this.getDeathSound(), 3.0F, 0.5F + this.deathTicks / 133.0F);
			if (this.deathTicks > 200.0F) {
				if (this.world instanceof World && !((World) world).isRemote)
					((World) world).createExplosion(null, (int) x1, (int) y1, (int) z1, level, Explosion.Mode.BREAK);
				this.remove();
			}
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.goalSelector.addGoal(4, new CustomEntity.Teleport());
			this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, LivingEntity.class, false, false));
			this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false));
			this.goalSelector.addGoal(2, new RandomWalkingGoal(this, 1));
			this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
			this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
			this.goalSelector.addGoal(5, new SwimGoal(this));
		}
		class Teleport extends Goal {
			public Teleport() {
				this.setMutexFlags(EnumSet.of(Goal.Flag.LOOK));
			}

			public boolean shouldExecute() {
				LivingEntity livingentity = CustomEntity.this.getAttackTarget();
				if (livingentity != null && livingentity.isAlive()) {
					return true;
				} else {
					return false;
				}
			}

			public void tick() {
				LivingEntity livingentity = CustomEntity.this.getAttackTarget();
				Entity entity = CustomEntity.this;
				double x = entity.getPosX();
				double y = entity.getPosY();
				double z = entity.getPosZ();
				double tx = livingentity.getPosX();
				double ty = livingentity.getPosY();
				double tz = livingentity.getPosZ();
				CustomEntity.this.getLookController().setLookPositionWithEntity(livingentity, 2F, 2F);
				if (CustomEntity.this.swingProgress > 0 && !attackedTarget.isAlive() && attackedTarget != null) {
					CustomEntity.this.setPositionAndUpdate(tx, ty, tz);
					CustomEntity.this.swing(Hand.MAIN_HAND, true);
					CustomEntity.this.attackEntityAsMob(livingentity);
				}
				if (CustomEntity.this.rand.nextInt(10) == 0) {
					CustomEntity.this.teleportToEntity(livingentity);
				}
				super.tick();
			}
		}
		private boolean teleportTo(double x, double y, double z) {
			BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(x, y, z);
			while (blockpos$mutable.getY() > 0 && !this.world.getBlockState(blockpos$mutable).getMaterial().blocksMovement()) {
				blockpos$mutable.move(Direction.DOWN);
			}
			BlockState blockstate = this.world.getBlockState(blockpos$mutable);
			boolean flag = blockstate.getMaterial().blocksMovement();
			if (flag) {
				net.minecraftforge.event.entity.living.EntityTeleportEvent.EnderEntity event = net.minecraftforge.event.ForgeEventFactory
						.onEnderTeleport(this, x, y, z);
				if (event.isCanceled())
					return false;
				boolean flag2 = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
				if (flag2 && !this.isSilent()) {
					this.world.playSound((PlayerEntity) null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT,
							this.getSoundCategory(), 1.0F, 1.0F);
					this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
				}
				return flag2;
			} else {
				return false;
			}
		}

		private boolean teleportToEntity(Entity p_70816_1_) {
			Vector3d vector3d = new Vector3d(this.getPosX() - p_70816_1_.getPosX(), this.getPosYHeight(0.5D) - p_70816_1_.getPosYEye(),
					this.getPosZ() - p_70816_1_.getPosZ());
			vector3d = vector3d.normalize();
			double d0 = 16.0D;
			double d1 = this.getPosX() + (this.rand.nextDouble() - 0.5D) * 20.0D - vector3d.x * 1.0D;
			double d2 = this.getPosY() + (double) (this.rand.nextInt(20) - 10) - vector3d.y * 1.0D;
			double d3 = this.getPosZ() + (this.rand.nextDouble() - 0.5D) * 20.0D - vector3d.z * 1.0D;
			return this.teleportTo(d1, d2, d3);
		}

		public void livingTick() {
			super.livingTick();
			double x = this.getPosX();
			double y = this.getPosY();
			double z = this.getPosZ();
			Random random = this.rand;
			Entity entity = this;
			if (this.isInWater() || this.isInLava()) {
				float verticalSpeed;
				float surfaceIndicator = CustomEntity.this.world.getHeight(Heightmap.Type.OCEAN_FLOOR, (int) CustomEntity.this.getPosX(),
						(int) CustomEntity.this.getPosZ());
				if (CustomEntity.this.getPosY() - 6 < surfaceIndicator)
					verticalSpeed = 0.4f;
				else
					verticalSpeed = -0.4f;
				float speed = (float) ((LivingEntity) entity).getAttributeValue(Attributes.MOVEMENT_SPEED);
				if (((MobEntity) entity).isAggressive())
					entity.setMotion(entity.getMotion().add((speed * Math.sin(((-1) * Math.toRadians((entity.rotationYaw)))) / 4),
							(speed * (Math.sin(((-1) * Math.toRadians((entity.rotationPitch)))) / 1.5f + 0.1f)),
							(speed * Math.cos(((-1) * Math.toRadians((entity.rotationYaw)))) / 4)));
				else
					entity.setMotion(entity.getMotion().add((speed * Math.sin(((-1) * Math.toRadians((entity.rotationYaw)))) / 6),
							(speed * (Math.sin(((-1) * Math.toRadians((entity.rotationPitch)))) / 2 + verticalSpeed)),
							(speed * Math.cos(((-1) * Math.toRadians((entity.rotationYaw)))) / 6)));
				entity.rotationYaw = entity.getYaw(1);
			}
			if (attackCount > 0 && attackedTarget.isAlive()) {
				attackCount--;
				attackedTarget.hurtResistantTime = 0;
				attackedTarget.attackEntityFrom(DamageSource.causeMobDamage(this), 3);
			}
		}

		public boolean attackEntityAsMob(Entity entityIn) {
			entityIn.hurtResistantTime = 0;
			attackCount = 5;
			attackedTarget = entityIn;
			return super.attackEntityAsMob(entityIn);
		}

		public boolean attackEntityFrom(DamageSource source, float amount) {
			if (source == DamageSource.FALL || source == DamageSource.CRAMMING || source == DamageSource.IN_WALL || source == DamageSource.IN_FIRE
					|| source == DamageSource.LAVA)
				return false;
			return super.attackEntityFrom(source, amount);
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEFINED;
		}

		@Override
		public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("laboratory:spectrite_steve_hurt"));
		}

		@Override
		public net.minecraft.util.SoundEvent getDeathSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("laboratory:spectrite_steve_hurt"));
		}
	}
}
