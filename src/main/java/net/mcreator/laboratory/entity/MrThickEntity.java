
package net.mcreator.laboratory.entity;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

import net.minecraft.world.World;
import net.minecraft.world.Explosion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.DamageSource;
import net.minecraft.network.IPacket;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Item;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.Pose;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.CreatureAttribute;

import net.mcreator.laboratory.entity.renderer.MrThickRenderer;
import net.mcreator.laboratory.LaboratoryModElements;
import net.mcreator.laboratory.CustomMathHelper;

@LaboratoryModElements.ModElement.Tag
public class MrThickEntity extends LaboratoryModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(0.6f, 1.8f)).build("mr_thick").setRegistryName("mr_thick");

	public MrThickEntity(LaboratoryModElements instance) {
		super(instance, 60);
		FMLJavaModLoadingContext.get().getModEventBus().register(new MrThickRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -16777165, -16777114, new Item.Properties().group(ItemGroup.MISC))
				.setRegistryName("mr_thick_spawn_egg"));
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
	}

	private static class EntityAttributesRegisterHandler {
		@SubscribeEvent
		public void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
			AttributeModifierMap.MutableAttribute ammma = MobEntity.func_233666_p_();
			ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.15);
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 1000);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 1);
			ammma = ammma.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends MonsterEntity {
		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			experienceValue = 1000;
			setNoAI(false);
			stepHeight = 100;
			enablePersistence();
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1, false) {
				protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
					if (CustomMathHelper.isEntityInBox(enemy, this.attacker, 2) && !this.attacker.isSwingInProgress) {
						this.attacker.swingArm(Hand.MAIN_HAND);
						this.attacker.attackEntityAsMob(enemy);
					}
				}
			});
			this.goalSelector.addGoal(2, new RandomWalkingGoal(this, 1));
			this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
			this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
			this.goalSelector.addGoal(5, new SwimGoal(this));
			this.goalSelector.addGoal(2, new CustomEntity.CustomJumpGoal());
		}

		protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
			return super.getStandingEyeHeight(poseIn, sizeIn) * 2;
		}

		class CustomJumpGoal extends Goal {
			private int coolTime = 200;

			public CustomJumpGoal() {
			}

			public boolean shouldExecute() {
				return CustomEntity.this.getAttackTarget() != null;
			}

			public void tick() {
				if (this.coolTime > 0)
					this.coolTime--;
				else {
					this.coolTime = 200;
					CustomEntity.this.setMotion(0, 5, 0);
				}
			}
		}

		public void livingTick() {
			super.livingTick();
			if (this.getMotion().y > 0) {
				this.setMotion(this.getMotion().mul(0.6, 0.6, 0.6));
			} else
				this.setMotion(this.getMotion().mul(0.6, 1, 0.6));
		}

		protected float getSoundPitch() {
			return 0.5f;
		}

		public boolean onLivingFall(float distance, float damageMultiplier) {
			if (distance * 2 >= 4)
				this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), (float) Math.pow(distance * 2, 0.8),
						Explosion.Mode.DESTROY);
			return super.onLivingFall(distance * 2, damageMultiplier);
		}

		public EntitySize getSize(Pose poseIn) {
			return super.getSize(poseIn).scale(5, 2);
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEFINED;
		}

		@Override
		public boolean canDespawn(double distanceToClosestPlayer) {
			return false;
		}

		public boolean attackEntityFrom(DamageSource source, float amount) {
			return super.attackEntityFrom(source, amount * 0.001f);
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
