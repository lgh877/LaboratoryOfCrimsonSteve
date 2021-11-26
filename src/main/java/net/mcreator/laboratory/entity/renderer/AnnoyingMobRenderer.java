package net.mcreator.laboratory.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.MobRenderer;

import net.mcreator.laboratory.entity.AnnoyingMobEntity;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class AnnoyingMobRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(AnnoyingMobEntity.entity, renderManager -> {
				return new MobRenderer(renderManager, new ModelLMS_Human(), 0.5f) {
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						LivingEntity e = (LivingEntity) entity;
						if (e.getHealth() / e.getMaxHealth() > 0.8)
							return new ResourceLocation("laboratory:textures/purple_steve.png");
						else if (e.getHealth() / e.getMaxHealth() > 0.55)
							return new ResourceLocation("laboratory:textures/purple_steve_damage1.png");
						else if (e.getHealth() / e.getMaxHealth() > 0.3)
							return new ResourceLocation("laboratory:textures/purple_steve_damage2.png");
						else
							return new ResourceLocation("laboratory:textures/purple_steve_damage3.png");
					}
				};
			});
		}
	}

	public static class ModelLMS_Human extends EntityModel<LivingEntity> {
		private final ModelRenderer Body;
		private final ModelRenderer Head;
		private final ModelRenderer RightArm;
		private final ModelRenderer LeftArm;
		private final ModelRenderer RightLeg;
		private final ModelRenderer LeftLeg;
		public ModelLMS_Human() {
			textureWidth = 64;
			textureHeight = 64;
			Body = new ModelRenderer(this);
			Body.setRotationPoint(0.0F, 12.0F, 0.0F);
			Body.setTextureOffset(16, 16).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
			Body.setTextureOffset(16, 32).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.25F, false);
			Head = new ModelRenderer(this);
			Head.setRotationPoint(0.0F, -12.0F, 0.0F);
			Body.addChild(Head);
			Head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
			Head.setTextureOffset(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.5F, false);
			RightArm = new ModelRenderer(this);
			RightArm.setRotationPoint(-5.0F, -10.0F, 0.0F);
			Body.addChild(RightArm);
			RightArm.setTextureOffset(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
			RightArm.setTextureOffset(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.25F, false);
			LeftArm = new ModelRenderer(this);
			LeftArm.setRotationPoint(5.0F, -10.0F, 0.0F);
			Body.addChild(LeftArm);
			LeftArm.mirror = true;
			LeftArm.setTextureOffset(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
			LeftArm.setTextureOffset(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.25F, false);
			RightLeg = new ModelRenderer(this);
			RightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
			RightLeg.setTextureOffset(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
			RightLeg.setTextureOffset(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.25F, false);
			LeftLeg = new ModelRenderer(this);
			LeftLeg.mirror = true;
			LeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
			LeftLeg.setTextureOffset(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
			LeftLeg.setTextureOffset(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.25F, false);
		}

		@Override
		public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue,
				float alpha) {
			float a = 1.0F - this.swingProgress;
			a = a * a;
			a = a * a;
			matrixStack.scale(1 + MathHelper.sin(a * (float) Math.PI) * 2, 1, 1 + MathHelper.sin(a * (float) Math.PI) * 2);
			Body.render(matrixStack, buffer, packedLight, packedOverlay);
			RightLeg.render(matrixStack, buffer, packedLight, packedOverlay);
			LeftLeg.render(matrixStack, buffer, packedLight, packedOverlay);
		}

		public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
			modelRenderer.rotateAngleX = x;
			modelRenderer.rotateAngleY = y;
			modelRenderer.rotateAngleZ = z;
		}

		public void setRotationAngles(LivingEntity e, float f, float f1, float f2, float f3, float f4) {
			float a = 1.0F - this.swingProgress;
			a = a * a;
			a = a * a;
			float b = e.hurtTime;
			if (b > 0) {
				this.Head.rotateAngleZ = MathHelper.sin(f2 * 1.1f) * 1.5f;
				this.LeftArm.rotateAngleY = MathHelper.sin(f2 * 0.7f) * 1.5f;
				this.LeftArm.rotateAngleZ = MathHelper.sin(f2 * 1.3f) * 1.5f;
				this.RightArm.rotateAngleY = MathHelper.sin(f2 * 0.5f) * 1.5f;
				this.RightArm.rotateAngleZ = MathHelper.sin(f2 * 1.21f) * 1.5f;
			} else {
				this.Head.rotateAngleZ = 0;
				this.LeftArm.rotateAngleY = 0;
				this.LeftArm.rotateAngleZ = 0;
				this.RightArm.rotateAngleY = 0;
				this.RightArm.rotateAngleZ = 0;
			}
			this.Body.rotateAngleX = (MathHelper.cos(f * 1.2f) * 0.4f + 1.5f) * f1 * a - MathHelper.sin(a * (float) Math.PI * 2) * 1.5f;
			this.RightArm.rotateAngleX = (MathHelper.cos(f * 0.6f + (float) Math.PI) * 0.4f + 1) * f1 * a
					- MathHelper.sin(a * (float) Math.PI) * 1.5f;
			this.LeftLeg.rotateAngleX = MathHelper.cos(f * 1.0F) * -1.0F * f1;
			this.Head.rotateAngleY = f3 / (180F / (float) Math.PI);
			this.Head.rotateAngleX = f4 / (180F / (float) Math.PI) - (MathHelper.cos(f * 1.2f) * 0.4f + 1.5f) * f1 * a;
			this.LeftArm.rotateAngleX = (MathHelper.cos(f * 0.6f) * 0.4f + 1) * f1 * a - MathHelper.sin(a * (float) Math.PI * 2) * 1.5f;
			this.RightLeg.rotateAngleX = MathHelper.cos(f * 1.0F) * 1.0F * f1;
		}
	}
}
