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

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;

import java.util.EnumSet;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class LaboratoryWatchTargetGoal extends Goal {
	private MobEntity mob;
	public LaboratoryWatchTargetGoal(MobEntity entity) {
		mob = entity;
		this.setMutexFlags(EnumSet.of(Goal.Flag.LOOK));
	}

	public boolean shouldExecute() {
		LivingEntity livingentity = mob.getAttackTarget();
		if (livingentity != null && livingentity.isAlive()) {
			return true;
		} else {
			return false;
		}
	}

	public void tick() {
		LivingEntity livingentity = mob.getAttackTarget();
		mob.getLookController().setLookPositionWithEntity(livingentity, 360.0F, 360.0F);
		mob.rotationYaw = mob.getYaw(1);
		super.tick();
	}
}
