
package net.mcreator.laboratory.item;

import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ActionResult;
import net.minecraft.network.IPacket;
import net.minecraft.item.UseAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.Entity;

import net.mcreator.laboratory.procedures.SoulSmokeProjectileWhileBulletFlyingTickProcedure;
import net.mcreator.laboratory.particle.SoulSmokeParticleParticle;
import net.mcreator.laboratory.itemgroup.ItemsOfLaboratoryItemGroup;
import net.mcreator.laboratory.entity.renderer.SoulSmokeProjectileRenderer;
import net.mcreator.laboratory.LaboratoryModElements;

import java.util.Random;
import java.util.Map;
import java.util.HashMap;

@LaboratoryModElements.ModElement.Tag
public class SoulSmokeProjectileItem extends LaboratoryModElements.ModElement {
	@ObjectHolder("laboratory:soul_smoke_projectile")
	public static final Item block = null;
	public static final EntityType arrow = (EntityType.Builder.<ArrowCustomEntity>create(ArrowCustomEntity::new, EntityClassification.MISC)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).setCustomClientFactory(ArrowCustomEntity::new)
			.size(0.5f, 0.5f)).build("entitybulletsoul_smoke_projectile").setRegistryName("entitybulletsoul_smoke_projectile");

	public SoulSmokeProjectileItem(LaboratoryModElements instance) {
		super(instance, 37);
		FMLJavaModLoadingContext.get().getModEventBus().register(new SoulSmokeProjectileRenderer.ModelRegisterHandler());
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemRanged());
		elements.entities.add(() -> arrow);
	}

	public static class ItemRanged extends Item {
		public ItemRanged() {
			super(new Item.Properties().group(ItemsOfLaboratoryItemGroup.tab).maxDamage(100));
			setRegistryName("soul_smoke_projectile");
		}

		@Override
		public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity entity, Hand hand) {
			entity.setActiveHand(hand);
			return new ActionResult(ActionResultType.SUCCESS, entity.getHeldItem(hand));
		}

		@Override
		public UseAction getUseAction(ItemStack itemstack) {
			return UseAction.BOW;
		}

		@Override
		public int getUseDuration(ItemStack itemstack) {
			return 72000;
		}

		@Override
		public void onPlayerStoppedUsing(ItemStack itemstack, World world, LivingEntity entityLiving, int timeLeft) {
			if (!world.isRemote && entityLiving instanceof ServerPlayerEntity) {
				ServerPlayerEntity entity = (ServerPlayerEntity) entityLiving;
				double x = entity.getPosX();
				double y = entity.getPosY();
				double z = entity.getPosZ();
				if (true) {
					ArrowCustomEntity entityarrow = shoot(world, entity, random, 0.5f, 2, 0);
					itemstack.damageItem(1, entity, e -> e.sendBreakAnimation(entity.getActiveHand()));
					entityarrow.pickupStatus = AbstractArrowEntity.PickupStatus.DISALLOWED;
				}
			}
		}
	}

	@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
	public static class ArrowCustomEntity extends AbstractArrowEntity implements IRendersAsItem {
		private int lifeTime;

		public ArrowCustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			super(arrow, world);
		}

		public ArrowCustomEntity(EntityType<? extends ArrowCustomEntity> type, World world) {
			super(type, world);
		}

		public ArrowCustomEntity(EntityType<? extends ArrowCustomEntity> type, double x, double y, double z, World world) {
			super(type, x, y, z, world);
		}

		public ArrowCustomEntity(EntityType<? extends ArrowCustomEntity> type, LivingEntity entity, World world) {
			super(type, entity, world);
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		protected float getWaterDrag() {
			return 0;
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack getItem() {
			return null;
		}

		@Override
		protected ItemStack getArrowStack() {
			return null;
		}

		@Override
		protected void arrowHit(LivingEntity entity) {
			super.arrowHit(entity);
			double x = this.getPosX();
			double y = this.getPosY();
			double z = this.getPosZ();
			if (world instanceof ServerWorld) {
				((ServerWorld) world).spawnParticle(SoulSmokeParticleParticle.particle, x, y, z, (int) 15, 0, 0, 0, 0.1);
			}
			entity.setArrowCountInEntity(entity.getArrowCountInEntity() - 1);
		}

		@Override
		public void tick() {
			super.tick();
			double x = this.getPosX();
			double y = this.getPosY();
			double z = this.getPosZ();
			World world = this.world;
			Entity entity = this.func_234616_v_();
			Entity imediatesourceentity = this;
			this.lifeTime++;
			this.setNoGravity(true);
			this.setMotion(this.getMotion().mul(1.02f, 1.02f, 1.02f));
			{
				Map<String, Object> $_dependencies = new HashMap<>();
				$_dependencies.put("x", x);
				$_dependencies.put("y", y);
				$_dependencies.put("z", z);
				$_dependencies.put("world", world);
				SoulSmokeProjectileWhileBulletFlyingTickProcedure.executeProcedure($_dependencies);
			}
			if (this.lifeTime > 20) {
				if (world instanceof ServerWorld) {
					((ServerWorld) world).spawnParticle(SoulSmokeParticleParticle.particle, x, y, z, (int) 15, 0, 0, 0, 0.1);
				}
				this.remove();
			}
		}
	}

	public static ArrowCustomEntity shoot(World world, LivingEntity entity, Random random, float power, double damage, int knockback) {
		ArrowCustomEntity entityarrow = new ArrowCustomEntity(arrow, entity, world);
		entityarrow.shoot(entity.getLookVec().x, entity.getLookVec().y, entity.getLookVec().z, power * 2, 0);
		entityarrow.setSilent(true);
		entityarrow.setIsCritical(false);
		entityarrow.setDamage(damage);
		entityarrow.setKnockbackStrength(knockback);
		world.addEntity(entityarrow);
		double x = entity.getPosX();
		double y = entity.getPosY();
		double z = entity.getPosZ();
		world.playSound((PlayerEntity) null, (double) x, (double) y, (double) z,
				(net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.soul_sand.break")),
				SoundCategory.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
		return entityarrow;
	}

	public static ArrowCustomEntity shoot(LivingEntity entity, LivingEntity target, float error) {
		ArrowCustomEntity entityarrow = new ArrowCustomEntity(arrow, entity, entity.world);
		double x = entity.getPosX();
		double y = entity.getPosY() + entity.getEyeHeight();
		double z = entity.getPosZ();
		float er1 = (float) (new Random().nextFloat() - 0.5) * error;
		float er2 = (float) (new Random().nextFloat() - 0.5) * error;
		float er3 = (float) (new Random().nextFloat() - 0.5) * error;
		double d0 = (target.getPosY() + (double) target.getEyeHeight() - y) * 0.1;
		double d1 = (target.getPosX() - x) * 0.1;
		double d3 = (target.getPosZ() - z) * 0.1;
		entityarrow.setMotion(d1 + er1, d0 + er2, d3 + er3);
		entityarrow.setLocationAndAngles(x, y, z, 0, 0);
		entityarrow.setSilent(true);
		entityarrow.setDamage(1);
		entityarrow.setKnockbackStrength(0);
		entityarrow.setIsCritical(false);
		entityarrow.setHitSound(SoundEvents.BLOCK_SOUL_SOIL_BREAK);
		entity.world.addEntity(entityarrow);
		entity.world.playSound((PlayerEntity) null, (double) x, (double) y, (double) z,
				(net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.soul_sand.break")),
				SoundCategory.PLAYERS, 1, 1f / (new Random().nextFloat() * 0.5f + 1));
		return entityarrow;
	}
}
