
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.DamageSource;
import net.minecraft.network.IPacket;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Item;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
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

import net.mcreator.laboratory.entity.renderer.BateveRenderer;
import net.mcreator.laboratory.LaboratoryModElements;

@LaboratoryModElements.ModElement.Tag
public class BateveEntity extends LaboratoryModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(0.6f, 1.8f)).build("bateve").setRegistryName("bateve");

	public BateveEntity(LaboratoryModElements instance) {
		super(instance, 55);
		FMLJavaModLoadingContext.get().getModEventBus().register(new BateveRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -1, -1, new Item.Properties().group(ItemGroup.MISC)).setRegistryName("bateve_spawn_egg"));
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
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 20);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 64);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 3);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends AbstractRaiderEntity {
		private BlockPos spawnPosition;

		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public boolean onLivingFall(float distance, float damageMultiplier) {
			return false;
		}

		public void applyWaveBonus(int wave, boolean p_213660_2_) {
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.ILLAGER;
		}

		public net.minecraft.util.SoundEvent getRaidLossSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("laboratory:stupid_laugh"));
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

		protected void updateAITasks() {
			super.updateAITasks();
			BlockPos blockpos = this.getPosition();
			BlockPos blockpos1 = blockpos.up();
			if (this.spawnPosition != null && (!this.world.isAirBlock(this.spawnPosition) || this.spawnPosition.getY() < 1)) {
				this.spawnPosition = null;
			}
			if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.withinDistance(this.getPositionVec(), 2.0D)) {
				this.spawnPosition = new BlockPos(this.getPosX() + (double) this.rand.nextInt(7) - (double) this.rand.nextInt(7),
						this.getPosY() + (double) this.rand.nextInt(6) - 2.0D,
						this.getPosZ() + (double) this.rand.nextInt(7) - (double) this.rand.nextInt(7));
			}
			double d2 = (double) this.spawnPosition.getX() + 0.5D - this.getPosX();
			double d0 = (double) this.spawnPosition.getY() + 0.1D - this.getPosY();
			double d1 = (double) this.spawnPosition.getZ() + 0.5D - this.getPosZ();
			Vector3d vector3d = this.getMotion();
			Vector3d vector3d1 = vector3d.add((Math.signum(d2) * 0.5D - vector3d.x) * (double) 0.2F,
					(Math.signum(d0) * (double) 0.7F - vector3d.y) * (double) 0.15F, (Math.signum(d1) * 0.5D - vector3d.z) * (double) 0.2F);
			this.setMotion(vector3d1);
			float f = (float) (MathHelper.atan2(vector3d1.z, vector3d1.x) * (double) (180F / (float) Math.PI)) - 90.0F;
			float f1 = MathHelper.wrapDegrees(f - this.rotationYaw);
			this.moveForward = 0.5F;
			this.rotationYaw += f1;
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
			this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false) {
				protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
					double d0 = this.getAttackReachSqr(enemy);
					Vector3d posVector = new Vector3d(enemy.getPosX() - attacker.getPosX(), enemy.getPosY() - attacker.getPosY(),
							enemy.getPosZ() - attacker.getPosZ());
					posVector.normalize();
					if (distToEnemySqr <= d0 && this.func_234041_j_() <= 0) {
						this.func_234039_g_();
						this.attacker.swingArm(Hand.MAIN_HAND);
						this.attacker.attackEntityAsMob(enemy);
					}
					this.attacker.setMotion(this.attacker.getMotion().add(posVector.scale(0.03)));
				}
			});
			this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setCallsForHelp(MonsterEntity.class));
			this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, LivingEntity.class, false, false));
		}

		public void livingTick() {
			super.livingTick();
			this.setAir(300);
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
