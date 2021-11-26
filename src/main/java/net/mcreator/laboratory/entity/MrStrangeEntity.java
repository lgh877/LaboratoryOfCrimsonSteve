
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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.DamageSource;
import net.minecraft.network.IPacket;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Item;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
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
import net.minecraft.block.BlockState;
import net.minecraft.block.Block;

import net.mcreator.laboratory.entity.renderer.MrStrangeRenderer;
import net.mcreator.laboratory.LaboratoryModElements;

@LaboratoryModElements.ModElement.Tag
public class MrStrangeEntity extends LaboratoryModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(0.6f, 1.8f)).build("mr_strange").setRegistryName("mr_strange");
	public MrStrangeEntity(LaboratoryModElements instance) {
		super(instance, 40);
		FMLJavaModLoadingContext.get().getModEventBus().register(new MrStrangeRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(
				() -> new SpawnEggItem(entity, -1, -13421773, new Item.Properties().group(ItemGroup.MISC)).setRegistryName("mr_strange_spawn_egg"));
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
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 10);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 64);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 3);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends SlimeEntity {
		private boolean wasOnGroundCustom;
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
			this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, LivingEntity.class, false, false));
			this.goalSelector.addGoal(2, new LookRandomlyGoal(this));
			this.goalSelector.addGoal(3, new SwimGoal(this));
		}

		public void livingTick() {
			super.livingTick();
			if (this.onGround && !this.wasOnGroundCustom) {
				for (LivingEntity livingentity : this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox()
						.grow((float) this.getSlimeSize() * 0.4f, 0, (float) this.getSlimeSize() * 0.4f).contract(0, this.getHeight() * 0.9f, 0))) {
					if (!this.isOnSameTeam((Entity) livingentity)) {
						float dist = MathHelper.clamp(this.getDistance(livingentity), 0.5f, 5);
						float damage = this.func_225512_er_() / dist;
						if (livingentity.attackEntityFrom(DamageSource.causeMobDamage(this), damage)) {
							this.playSound(SoundEvents.ENTITY_SLIME_ATTACK, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
							this.applyEnchantments(this, livingentity);
							livingentity.setMotion(livingentity.getMotion().add(0, (double) (this.getSlimeSize() / dist) * 0.1, 0));
						}
					}
				}
				if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this) && !this.isInWater()) {
					boolean flag = false;
					AxisAlignedBB axisalignedbb = this.getBoundingBox()
							.grow((float) this.getSlimeSize() * 0.4f, 0, (float) this.getSlimeSize() * 0.4f).contract(0, this.getHeight() * 0.9f, 0);
					for (BlockPos blockpos : BlockPos.getAllInBoxMutable(MathHelper.floor(axisalignedbb.minX), MathHelper.floor(axisalignedbb.minY),
							MathHelper.floor(axisalignedbb.minZ), MathHelper.floor(axisalignedbb.maxX), MathHelper.floor(axisalignedbb.maxY),
							MathHelper.floor(axisalignedbb.maxZ))) {
						BlockState blockstate = this.world.getBlockState(blockpos);
						Block block = blockstate.getBlock();
						if (blockstate.getBlockHardness(world, blockpos) <= 2 && blockstate.getBlockHardness(world, blockpos) != -1) {
							flag = this.world.destroyBlock(blockpos, true, this) || flag;
						}
					}
				}
			}
			if (!this.onGround) {
				this.setPose(Pose.FALL_FLYING);
			} else
				this.setPose(Pose.STANDING);
			this.wasOnGroundCustom = this.onGround;
		}

		@Override
		protected int getJumpDelay() {
			return this.getSlimeSize() * 8;
		}

		protected void alterSquishAmount() {
			this.squishAmount *= 0.9F;
		}

		protected void jump() {
			Vector3d vector3d = this.getMotion();
			this.setMotion(vector3d.x, (double) (this.getJumpUpwardsMotion() + (float) this.getSlimeSize() * 0.15F), vector3d.z);
			this.isAirBorne = true;
			net.minecraftforge.common.ForgeHooks.onLivingJump(this);
		}

		public boolean onLivingFall(float distance, float damageMultiplier) {
			return false;
		}

		public boolean isOnSameTeam(Entity entityIn) {
			if (entityIn == null) {
				return false;
			} else if (entityIn == this) {
				return true;
			} else if (super.isOnSameTeam(entityIn)) {
				return true;
			} else if (entityIn instanceof MrStrangeEntity.CustomEntity) {
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
		public void applyEntityCollision(Entity entityIn) {
			super.applyEntityCollision(entityIn);
			if (entityIn instanceof LivingEntity && !entityIn.isOnSameTeam(this)) {
				this.dealDamage((LivingEntity) entityIn);
			}
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.ILLAGER;
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
