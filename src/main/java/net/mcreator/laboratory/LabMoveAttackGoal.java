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

import net.minecraft.util.math.MathHelper;
import net.minecraft.item.ShieldItem;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.IRangedAttackMob;

import java.util.EnumSet;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class LabMoveAttackGoal<T extends MonsterEntity & IRangedAttackMob> extends Goal {
	private final T entity;
	private final double moveSpeedAmp;
	private int attackTime = -1;
	private final float attackRadius;
	private int attackCooldown;
	private final float maxAttackDistance;
	private int seeTime;
	private boolean strafingClockwise;
	private boolean strafingBackwards;
	private int strafingTime = -1;
	public LabMoveAttackGoal(T mob, double moveSpeedAmpIn, int attackCooldownIn, float maxAttackDistanceIn) {
		this.entity = mob;
		this.moveSpeedAmp = moveSpeedAmpIn;
		this.attackCooldown = attackCooldownIn;
		this.attackRadius = maxAttackDistanceIn;
		this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
		this.setMutexFlags(EnumSet.of(Goal.Flag.LOOK));
	}

	public void setAttackCooldown(int attackCooldownIn) {
		this.attackCooldown = attackCooldownIn;
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state
	 * necessary for execution in this method as well.
	 */
	public boolean shouldExecute() {
		return this.entity.getAttackTarget() == null ? false : true;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean shouldContinueExecuting() {
		return (this.shouldExecute() || !this.entity.getNavigator().noPath());
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		super.startExecuting();
		this.entity.setAggroed(true);
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by
	 * another one
	 */
	public void resetTask() {
		super.resetTask();
		this.entity.setAggroed(true);
		this.seeTime = 0;
		this.attackTime = -1;
		this.entity.resetActiveHand();
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void tick() {
		LivingEntity livingentity = this.entity.getAttackTarget();
		if (livingentity != null) {
			if (this.entity.isHandActive()) {
				double d0 = this.entity.getDistanceSq(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());
				boolean flag = this.entity.getEntitySenses().canSee(livingentity);
				boolean flag1 = this.seeTime > 0;
				if (flag != flag1) {
					this.seeTime = 0;
				}
				if (flag) {
					++this.seeTime;
				} else {
					--this.seeTime;
				}
				if (!(d0 > (double) this.maxAttackDistance) && this.seeTime >= 20) {
					this.entity.getNavigator().clearPath();
					++this.strafingTime;
				} else {
					this.entity.getNavigator().tryMoveToEntityLiving(livingentity, this.moveSpeedAmp);
					this.strafingTime = -1;
				}
				if (this.strafingTime >= 20) {
					if ((double) this.entity.getRNG().nextFloat() < 0.3D) {
						this.strafingClockwise = !this.strafingClockwise;
					}
					if ((double) this.entity.getRNG().nextFloat() < 0.3D) {
						this.strafingBackwards = !this.strafingBackwards;
					}
					this.strafingTime = 0;
				}
				if (this.strafingTime > -1) {
					if (d0 > (double) (this.maxAttackDistance * 0.75F)) {
						this.strafingBackwards = false;
					} else {
						this.strafingBackwards = true;
					}
					this.entity.getMoveHelper().strafe(this.strafingBackwards ? -0.8F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
					this.entity.faceEntity(livingentity, 180F, 180F);
				} else {
					this.entity.getLookController().setLookPositionWithEntity(livingentity, 180F, 180F);
				}
				if (!flag && this.seeTime < -60) {
					this.entity.resetActiveHand();
				} else if (flag) {
					int i = this.entity.getItemInUseMaxCount();
					if (i >= 80) {
						float f = MathHelper.sqrt(d0) / this.attackRadius;
						float lvt_5_1_ = MathHelper.clamp(f, 0.1F, 1.0F);
						this.entity.resetActiveHand();
						this.entity.attackEntityWithRangedAttack(livingentity, lvt_5_1_);
						this.attackTime = this.attackCooldown;
					}
				}
			} else if (--this.attackTime <= 0 && this.seeTime >= -60) {
				this.entity.setActiveHand(ProjectileHelper.getWeaponHoldingHand(this.entity, item -> item instanceof ShieldItem));
			}
		}
	}
}
