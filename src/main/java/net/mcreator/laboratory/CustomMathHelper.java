/**
 * The code of this mod element is always locked.
 *
 * You can register new events in this class too.
 *
 * If you want to make a plain independent class, create it using
 * Project Browser -> New... and make sure to make the class
 * outside net.mcreator.laboratory as this package is managed by MCreator.
 *
 * If you change workspace package, modid or prefix, you will need
 * to manually adapt this file to these changes or remake it.
 *
 * This class will be added in the mod root package.
*/
package net.mcreator.laboratory;

import net.minecraftforge.fml.common.Mod;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.LivingEntity;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CustomMathHelper {
	public static boolean isEntityInBox(LivingEntity entityIn, LivingEntity caster, double sizeup) {
		for (LivingEntity entity : caster.world.getEntitiesWithinAABB(LivingEntity.class, caster.getBoundingBox().grow(sizeup))) {
			if (entity == entityIn) {
				return true;
			}
		}
		return false;
	}

	public static AxisAlignedBB makeSphericalAttackRange(LivingEntity entityIn, double height, double distTo, double sizeX, double sizeY,
			double sizeZ) {
		double x = entityIn.getPosX() - Math.sin((Math.toRadians((entityIn.getYaw(1)))))
				* Math.cos((float) Math.PI / 2 - (Math.toRadians((entityIn.getPitch(1))))) * distTo;
		double y = entityIn.getPosY() + height - distTo * Math.sin((float) Math.PI / 2 - (Math.toRadians((entityIn.getPitch(1)))))
				* Math.sin((Math.toRadians((entityIn.getYaw(1)))));
		double z = entityIn.getPosZ() + Math.cos((Math.toRadians((entityIn.getYaw(1))))) * distTo;
		AxisAlignedBB attackRange = new AxisAlignedBB(x - (sizeX / 2d), y - (sizeY / 2), z - (sizeZ / 2d), x + (sizeX / 2d), y + (sizeY / 2),
				z + (sizeZ / 2d));
		return attackRange;
	}

	public static AxisAlignedBB makeHorizontalAttackRange(LivingEntity entityIn, double height, double distTo, double sizeX, double sizeY,
			double sizeZ) {
		double x = entityIn.getPosX() - Math.sin((Math.toRadians((entityIn.getYaw(1))))) * distTo;
		double y = entityIn.getPosY() + height;
		double z = entityIn.getPosZ() + Math.cos((Math.toRadians((entityIn.getYaw(1))))) * distTo;
		AxisAlignedBB attackRange = new AxisAlignedBB(x - (sizeX / 2d), y - (sizeY / 2), z - (sizeZ / 2d), x + (sizeX / 2d), y + (sizeY / 2),
				z + (sizeZ / 2d));
		return attackRange;
	}
}
