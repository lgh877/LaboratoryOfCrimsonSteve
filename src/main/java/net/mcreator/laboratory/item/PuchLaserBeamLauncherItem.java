
package net.mcreator.laboratory.item;

import net.minecraftforge.registries.ObjectHolder;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.IItemTier;
import net.minecraft.item.AxeItem;
import net.minecraft.entity.LivingEntity;

import net.mcreator.laboratory.itemgroup.ItemsOfLaboratoryItemGroup;
import net.mcreator.laboratory.entity.ElectricExplosionEntity;
import net.mcreator.laboratory.LaboratoryModElements;

@LaboratoryModElements.ModElement.Tag
public class PuchLaserBeamLauncherItem extends LaboratoryModElements.ModElement {
	@ObjectHolder("laboratory:puch_laser_beam_launcher")
	public static final Item block = null;

	public PuchLaserBeamLauncherItem(LaboratoryModElements instance) {
		super(instance, 56);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new AxeItem(new IItemTier() {
			public int getMaxUses() {
				return 100;
			}

			public float getEfficiency() {
				return 4f;
			}

			public float getAttackDamage() {
				return 2f;
			}

			public int getHarvestLevel() {
				return 1;
			}

			public int getEnchantability() {
				return 2;
			}

			public Ingredient getRepairMaterial() {
				return Ingredient.EMPTY;
			}
		}, 1, -3f, new Item.Properties().group(ItemsOfLaboratoryItemGroup.tab)) {
			@Override
			public boolean onEntitySwing(ItemStack itemstack, LivingEntity entity) {
				boolean retval = super.onEntitySwing(itemstack, entity);
				double x = entity.getPosX();
				double y = entity.getPosY() + entity.getEyeHeight();
				double z = entity.getPosZ();
				World world = entity.world;
				Vector3d vector1 = entity.getLookVec();
				vector1.normalize();
				double i = 0;
				entity.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, 1, 0.5f);
				for (int index0 = 0; index0 < 30; index0++) {
					if (index0 % 2 == 1) {
						double tx = (x + index0 * vector1.x * 2);
						double ty = (y + index0 * vector1.y * 2);
						double tz = (z + index0 * vector1.z * 2);
						if (world instanceof ServerWorld) {
							ElectricExplosionEntity.CustomEntity entityToSpawn = new ElectricExplosionEntity.CustomEntity(
									ElectricExplosionEntity.entity, (World) world);
							entityToSpawn.setCaster(entity);
							entityToSpawn.setLocationAndAngles(tx, ty, tz, 0, 0);
							entityToSpawn.setDelayTime(index0);
							world.addEntity(entityToSpawn);
						}
					}
				}
				/*PuchLaserBeamLauncherEntitySwingsItemProcedure.executeProcedure(Stream
						.of(new AbstractMap.SimpleEntry<>("world", world), new AbstractMap.SimpleEntry<>("x", x),
								new AbstractMap.SimpleEntry<>("y", y), new AbstractMap.SimpleEntry<>("z", z))
						.collect(HashMap::new, (_m, _e) -> _m.put(_e.getKey(), _e.getValue()), Map::putAll));
				*/
				return super.onEntitySwing(itemstack, entity);
			}
		}.setRegistryName("puch_laser_beam_launcher"));
	}
}
