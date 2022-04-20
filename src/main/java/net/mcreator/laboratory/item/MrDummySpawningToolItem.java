
package net.mcreator.laboratory.item;

import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.util.ActionResultType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.Rarity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.Entity;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.BlockState;

import net.mcreator.laboratory.itemgroup.ItemsOfLaboratoryItemGroup;
import net.mcreator.laboratory.entity.MrDummyEntity;
import net.mcreator.laboratory.LaboratoryModElements;

@LaboratoryModElements.ModElement.Tag
public class MrDummySpawningToolItem extends LaboratoryModElements.ModElement {
	@ObjectHolder("laboratory:mr_dummy_spawning_tool")
	public static final Item block = null;

	public MrDummySpawningToolItem(LaboratoryModElements instance) {
		super(instance, 11);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemCustom());
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
		DispenserBlock.registerDispenseBehavior(block, new OptionalDispenseBehavior() {
			public ItemStack dispenseStack(IBlockSource blockSource, ItemStack stack) {
				ItemStack itemstack = stack.copy();
				World world = blockSource.getWorld();
				int x = blockSource.getBlockPos().getX();
				int y = blockSource.getBlockPos().getY();
				int z = blockSource.getBlockPos().getZ();
				this.setSuccessful(true);
				boolean success = this.isSuccessful();
				{
					if (world instanceof ServerWorld) {
						Entity entityToSpawn = new MrDummyEntity.CustomEntity(MrDummyEntity.entity, (World) world);
						entityToSpawn.setLocationAndAngles(x + 0.5, y + 1, z + 0.5, world.getRandom().nextFloat() * 360F, 0);
						if (entityToSpawn instanceof MobEntity)
							((MobEntity) entityToSpawn).onInitialSpawn((ServerWorld) world,
									world.getDifficultyForLocation(entityToSpawn.getPosition()), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null,
									(CompoundNBT) null);
						world.addEntity(entityToSpawn);
					}
				}
				if (success)
					itemstack.shrink(1);
				return itemstack;
			}
		});
	}

	public static class ItemCustom extends Item {
		public ItemCustom() {
			super(new Item.Properties().group(ItemsOfLaboratoryItemGroup.tab).maxStackSize(64).rarity(Rarity.COMMON));
			setRegistryName("mr_dummy_spawning_tool");
		}

		@Override
		public int getItemEnchantability() {
			return 0;
		}

		@Override
		public int getUseDuration(ItemStack itemstack) {
			return 0;
		}

		@Override
		public float getDestroySpeed(ItemStack par1ItemStack, BlockState par2Block) {
			return 1F;
		}

		@Override
		public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
			ActionResultType retval = super.onItemUseFirst(stack, context);
			World world = context.getWorld();
			BlockPos pos = context.getPos();
			PlayerEntity entity = context.getPlayer();
			Direction direction = context.getFace();
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();
			ItemStack itemstack = context.getItem();
			{
				if (world instanceof ServerWorld) {
					Entity entityToSpawn = new MrDummyEntity.CustomEntity(MrDummyEntity.entity, (World) world);
					entityToSpawn.setLocationAndAngles(x + 0.5, y + 1, z + 0.5, world.getRandom().nextFloat() * 360F, 0);
					if (entityToSpawn instanceof MobEntity)
						((MobEntity) entityToSpawn).onInitialSpawn((ServerWorld) world, world.getDifficultyForLocation(entityToSpawn.getPosition()),
								SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
					world.addEntity(entityToSpawn);
					itemstack.shrink(1);
				}
			}
			return retval;
		}
	}
}
