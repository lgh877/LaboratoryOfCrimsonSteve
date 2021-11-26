
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
import net.minecraft.item.ShieldItem;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Item;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.projectile.AbstractArrowEntity;
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
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.CreatureAttribute;

import net.mcreator.laboratory.entity.renderer.TheMoverRenderer;
import net.mcreator.laboratory.LaboratoryModElements;
import net.mcreator.laboratory.LabMoveAttackGoal;

import javax.annotation.Nullable;

@LaboratoryModElements.ModElement.Tag
public class TheMoverEntity extends LaboratoryModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(0.6f, 1.8f)).build("the_mover").setRegistryName("the_mover");
	public TheMoverEntity(LaboratoryModElements instance) {
		super(instance, 42);
		FMLJavaModLoadingContext.get().getModEventBus().register(new TheMoverRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items
				.add(() -> new SpawnEggItem(entity, -1, -1, new Item.Properties().group(ItemGroup.MISC)).setRegistryName("the_mover_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(entity, 5, 1, 1));
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
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 64);
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 20);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 1);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends MonsterEntity implements IRangedAttackMob {
		public int rush;
		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			experienceValue = 0;
			setNoAI(false);
			this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
			this.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
			this.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.IRON_LEGGINGS));
			this.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.IRON_BOOTS));
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1, false));
			/*
			 * {
			 * 
			 * @Override public boolean shouldExecute() { return super.shouldExecute() &&
			 * !CustomEntity.this.isHandActive(); }
			 * 
			 * @Override public void resetTask() { super.resetTask();
			 * CustomEntity.this.setSprinting(false); }
			 * 
			 * @Override public void tick() { super.tick();
			 * CustomEntity.this.setSprinting(true); } });
			 */
			this.goalSelector.addGoal(2, new LabMoveAttackGoal<>(this, 1.5D, 50, 30));
			this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 5, false, false, (p_234199_0_) -> {
				return !(p_234199_0_ instanceof TheMoverEntity.CustomEntity);
			}));
			this.goalSelector.addGoal(2, new RandomWalkingGoal(this, 1));
			this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
			this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
			this.goalSelector.addGoal(5, new SwimGoal(this));
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
			} else if (entityIn instanceof VexEntity) {
				return this.isOnSameTeam(((VexEntity) entityIn).getOwner());
			} else if (entityIn instanceof LivingEntity && ((LivingEntity) entityIn).getCreatureAttribute() == CreatureAttribute.ILLAGER) {
				return this.getTeam() == null && entityIn.getTeam() == null;
			} else {
				return false;
			}
		}

		@Override
		public boolean isPushedByWater() {
			return this.rush == 0;
		}

		public void attackEntityWithRangedAttack(LivingEntity target, float flval) {
			double d0 = target.getPosX() - this.getPosX();
			double d1 = target.getPosY() - this.getPosYRandom();
			double d2 = target.getPosZ() - this.getPosZ();
			if (this.onGround)
				this.setMotion(d0 * 0.4d, MathHelper.clamp(d1 * 0.4d, 0.4, Double.POSITIVE_INFINITY), d2 * 0.4d);
			else
				this.setMotion(d0 * 0.4d, d1 * 0.4d, d2 * 0.4d);
		}

		public void applyEntityCollision(Entity entityIn) {
			if (entityIn == this.getAttackTarget() && this.rush % 4 == 1) {
				this.attackEntityAsMob(entityIn);
			}
		}

		public boolean attackEntityAsMob(Entity entityIn) {
			this.swing(Hand.MAIN_HAND, true);
			return super.attackEntityAsMob(entityIn);
		}

		public void livingTick() {
			super.livingTick();
			this.setLeftHanded(false);
			if (this.isAlive()) {
				if (this.isHandActive())
					this.rush = 20;
				else if (this.rush > 0) {
					this.rush--;
				}
				if (!(this.getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem() instanceof ShieldItem))
					this.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.SHIELD));
				if (this.isHandActive() || this.rush != 0) {
					this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_AXE));
				} else
					this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
				if (this.rand.nextInt(10) == 0)
					this.setHealth(this.getHealth() + 1);
				if (Math.abs(this.getMotion().z) > 2)
					this.setMotion(this.getMotion().mul(1, 1, 0.8));
				if (Math.abs(this.getMotion().x) > 2)
					this.setMotion(this.getMotion().mul(0.8, 1, 1));
				if (Math.abs(this.getMotion().y) > 3)
					this.setMotion(this.getMotion().mul(1, 0.5, 1));
			}
		}

		private boolean canBlockDamageSource(DamageSource damageSourceIn) {
			Entity entity = damageSourceIn.getImmediateSource();
			boolean flag = false;
			if (entity instanceof AbstractArrowEntity) {
				AbstractArrowEntity abstractarrowentity = (AbstractArrowEntity) entity;
				if (abstractarrowentity.getPierceLevel() > 0) {
					flag = true;
				}
			}
			if (!damageSourceIn.isUnblockable() && this.isActiveItemStackBlocking() && !flag) {
				Vector3d vector3d2 = damageSourceIn.getDamageLocation();
				if (vector3d2 != null) {
					Vector3d vector3d = this.getLook(1.0F);
					Vector3d vector3d1 = vector3d2.subtractReverse(this.getPositionVec()).normalize();
					vector3d1 = new Vector3d(vector3d1.x, 0.0D, vector3d1.z);
					if (vector3d1.dotProduct(vector3d) < 0.0D) {
						return true;
					}
				}
			}
			return false;
		}

		@Nullable
		public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason,
				@Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
			this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_AXE));
			return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
		}

		public boolean attackEntityFrom(DamageSource source, float amount) {
			return super.attackEntityFrom(source, amount);
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.ILLAGER;
		}

		@Override
		public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) {
			return this.canBlockDamageSource(ds) && this.isHandActive()
					? (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.shield.block"))
					: (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("laboratory:steve_hurt"));
		}

		@Override
		public net.minecraft.util.SoundEvent getDeathSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("laboratory:steve_hurt"));
		}
	}
}
