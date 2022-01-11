package net.mcreator.laboratory.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.MobRenderer;

import net.mcreator.laboratory.entity.DreadfulSteveGhoulEntity;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class DreadfulSteveGhoulRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(DreadfulSteveGhoulEntity.entity, renderManager -> {
				return new MobRenderer(renderManager, new ModelDreadful_Soul_Ghoul(), 0.7f) {
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("laboratory:textures/dreadful_soul_ghoul.png");
					}
				};
			});
		}
	}

	// Made with Blockbench 4.1.1
	// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
	// Paste this class into your mod and generate all required imports
	public static class ModelDreadful_Soul_Ghoul extends EntityModel<Entity> {
		private final ModelRenderer lowerBody;
		private final ModelRenderer upperBody;
		private final ModelRenderer head;
		private final ModelRenderer rightArm;
		private final ModelRenderer leftArm;
		private final ModelRenderer rightLeg1;
		private final ModelRenderer rightLeg2;
		private final ModelRenderer rightLeg3;
		private final ModelRenderer leftLeg1;
		private final ModelRenderer leftLeg2;
		private final ModelRenderer leftLeg3;

		public ModelDreadful_Soul_Ghoul() {
			textureWidth = 128;
			textureHeight = 128;
			lowerBody = new ModelRenderer(this);
			lowerBody.setRotationPoint(0.0F, -6.0F, 0.0F);
			lowerBody.setTextureOffset(64, 0).addBox(-6.0F, -13.0F, -4.0F, 12.0F, 13.0F, 8.0F, 0.0F, false);
			upperBody = new ModelRenderer(this);
			upperBody.setRotationPoint(0.0F, -13.0F, 0.0F);
			lowerBody.addChild(upperBody);
			upperBody.setTextureOffset(0, 0).addBox(-9.0F, -11.0F, -7.0F, 18.0F, 11.0F, 14.0F, 0.0F, false);
			head = new ModelRenderer(this);
			head.setRotationPoint(0.0F, -10.0F, -7.0F);
			upperBody.addChild(head);
			head.setTextureOffset(64, 70).addBox(-4.0F, -5.0F, -4.0F, 8.0F, 12.0F, 5.0F, 0.0F, false);
			rightArm = new ModelRenderer(this);
			rightArm.setRotationPoint(-13.0F, -7.0F, 0.0F);
			upperBody.addChild(rightArm);
			rightArm.setTextureOffset(32, 70).addBox(-4.0F, -5.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.7F, false);
			rightArm.setTextureOffset(24, 86).addBox(-3.0F, 3.0F, -3.0F, 6.0F, 10.0F, 6.0F, 0.0F, false);
			rightArm.setTextureOffset(40, 25).addBox(-5.0F, 13.0F, -5.0F, 10.0F, 35.0F, 10.0F, 0.0F, false);
			leftArm = new ModelRenderer(this);
			leftArm.setRotationPoint(13.0F, -7.0F, 0.0F);
			upperBody.addChild(leftArm);
			leftArm.setTextureOffset(0, 70).addBox(-4.0F, -5.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.7F, false);
			leftArm.setTextureOffset(0, 86).addBox(-3.0F, 3.0F, -3.0F, 6.0F, 10.0F, 6.0F, 0.0F, false);
			leftArm.setTextureOffset(0, 25).addBox(-5.0F, 13.0F, -5.0F, 10.0F, 35.0F, 10.0F, 0.0F, false);
			rightLeg1 = new ModelRenderer(this);
			rightLeg1.setRotationPoint(-5.0F, -6.0F, 0.0F);
			rightLeg1.setTextureOffset(85, 57).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 12.0F, 6.0F, 0.0F, false);
			rightLeg2 = new ModelRenderer(this);
			rightLeg2.setRotationPoint(0.0F, 11.0F, 0.0F);
			rightLeg1.addChild(rightLeg2);
			rightLeg2.setTextureOffset(64, 87).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);
			rightLeg3 = new ModelRenderer(this);
			rightLeg3.setRotationPoint(0.0F, 8.0F, 0.0F);
			rightLeg2.addChild(rightLeg3);
			rightLeg3.setTextureOffset(84, 81).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 12.0F, 6.0F, 0.0F, false);
			leftLeg1 = new ModelRenderer(this);
			leftLeg1.setRotationPoint(5.0F, -6.0F, 0.0F);
			leftLeg1.setTextureOffset(80, 39).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 12.0F, 6.0F, 0.0F, false);
			leftLeg2 = new ModelRenderer(this);
			leftLeg2.setRotationPoint(0.0F, 11.0F, 0.0F);
			leftLeg1.addChild(leftLeg2);
			leftLeg2.setTextureOffset(48, 86).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);
			leftLeg3 = new ModelRenderer(this);
			leftLeg3.setRotationPoint(0.0F, 8.0F, 0.0F);
			leftLeg2.addChild(leftLeg3);
			leftLeg3.setTextureOffset(80, 21).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 12.0F, 6.0F, 0.0F, false);
		}

		@Override
		public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue,
				float alpha) {
			lowerBody.render(matrixStack, buffer, packedLight, packedOverlay);
			rightLeg1.render(matrixStack, buffer, packedLight, packedOverlay);
			leftLeg1.render(matrixStack, buffer, packedLight, packedOverlay);
		}

		public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
			modelRenderer.rotateAngleX = x;
			modelRenderer.rotateAngleY = y;
			modelRenderer.rotateAngleZ = z;
		}

		public void setRotationAngles(Entity e, float f, float f1, float f2, float f3, float f4) {
			MobEntity entityM = (MobEntity) e;
			float customHeadYaw = f3 / (180F / (float) Math.PI);
			float customHeadPitch = f4 / (180F / (float) Math.PI);
			float spA1 = this.swingProgress * 2;
			float spA2 = (this.swingProgress - 0.5f) * 2;
			this.head.rotateAngleY = customHeadYaw * 0.5f;
			this.head.rotateAngleX = customHeadPitch * 0.35f;
			this.lowerBody.rotateAngleY = 0;
			this.lowerBody.rotateAngleX = customHeadPitch * 0.3f;
			this.upperBody.rotateAngleY = customHeadYaw * 0.5f;
			this.upperBody.rotateAngleX = customHeadPitch * 0.35f;
			if (entityM.isSwingInProgress) {
			} else {
				this.leftArm.rotateAngleX = MathHelper.cos(f * 0.3662F) * f1;
				this.rightArm.rotateAngleX = MathHelper.cos(f * 0.3662F + (float) Math.PI) * f1;
			}
			this.leftLeg1.rotateAngleX = MathHelper.cos(f * 0.5F) * -1.0F * f1;
			this.rightLeg1.rotateAngleX = MathHelper.cos(f * 0.5F) * 1.0F * f1;
		}
	}
}
