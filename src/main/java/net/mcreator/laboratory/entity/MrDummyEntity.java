
package net.mcreator.laboratory.entity;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

import net.minecraft.world.World;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ActionResultType;
import net.minecraft.network.IPacket;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.CreatureAttribute;

import net.mcreator.laboratory.itemgroup.MobsOfLaboratoryItemGroup;
import net.mcreator.laboratory.item.MrDummySpawningToolItem;
import net.mcreator.laboratory.entity.renderer.MrDummyRenderer;
import net.mcreator.laboratory.LaboratoryModElements;

@LaboratoryModElements.ModElement.Tag
public class MrDummyEntity extends LaboratoryModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(0.6f, 1.8f)).build("mr_dummy").setRegistryName("mr_dummy");

	public MrDummyEntity(LaboratoryModElements instance) {
		super(instance, 10);
		FMLJavaModLoadingContext.get().getModEventBus().register(new MrDummyRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -16724788, -52480, new Item.Properties().group(MobsOfLaboratoryItemGroup.tab))
				.setRegistryName("mr_dummy_spawn_egg"));
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
	}

	private static class EntityAttributesRegisterHandler {
		@SubscribeEvent
		public void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
			AttributeModifierMap.MutableAttribute ammma = MobEntity.func_233666_p_();
			ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0);
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 50);
			ammma = ammma.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 0);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends MonsterEntity {
		public void livingTick() {
			super.livingTick();
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

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.goalSelector.addGoal(1, new LookRandomlyGoal(this));
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEFINED;
		}

		@Override
		public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("laboratory:steve_hurt"));
		}

		@Override
		public net.minecraft.util.SoundEvent getDeathSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("laboratory:steve_hurt"));
		}

		//This trigger is for remove dummy mob.
		public ActionResultType func_230254_b_(PlayerEntity sourceentity, Hand hand) {
			if (sourceentity.isSneaking()) {
				//Indicates whether you are sneaking
				this.remove();
				ItemStack _setstack = new ItemStack(MrDummySpawningToolItem.block);
				_setstack.setCount((int) 1);
				ItemHandlerHelper.giveItemToPlayer((sourceentity), _setstack);
			}
			return ActionResultType.func_233537_a_(this.world.isRemote());
		}

		@Override
		public boolean attackEntityFrom(DamageSource source, float amount) {
			double x = this.getPosX();
			double y = this.getPosY();
			double z = this.getPosZ();
			ItemEntity entityToSpawn = new ItemEntity((World) world, x, y + 2, z, new ItemStack(Items.PLAYER_HEAD));
			entityToSpawn.lifespan = 40;
			entityToSpawn.setCustomNameVisible(true);
			entityToSpawn.setMotion((Math.random() - 0.5) * 0.5, 0.3, (Math.random() - 0.5) * 0.5);
			entityToSpawn.setPickupDelay(-1);
			entityToSpawn.setCustomName(new StringTextComponent(String.format("%.1f", amount)));
			world.addEntity(entityToSpawn);
			if (amount < 10000)
				return super.attackEntityFrom(source, 0);
			return super.attackEntityFrom(source, amount);
		}
	}
}
