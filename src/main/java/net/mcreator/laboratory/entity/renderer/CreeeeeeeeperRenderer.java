
package net.mcreator.laboratory.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.MobRenderer;

import net.mcreator.laboratory.entity.CreeeeeeeeperEntity;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class CreeeeeeeeperRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(CreeeeeeeeperEntity.entity, renderManager -> {
				return new MobRenderer(renderManager, new ModelChainAnimationTestMob(), 0.5f) {
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("laboratory:textures/chainanimationtestmob.png");
					}
				};
			});
		}
	}

	// Made with Blockbench 4.2.4
	// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
	// Paste this class into your mod and generate all required imports
	public static class ModelChainAnimationTestMob extends EntityModel<Entity> {
		private float movementScale = 1;
		private final ModelRenderer whole;
		private final ModelRenderer body;
		private final ModelRenderer neck1;
		private final ModelRenderer neck2;
		private final ModelRenderer neck3;
		private final ModelRenderer neck4;
		private final ModelRenderer neck5;
		private final ModelRenderer neck6;
		private final ModelRenderer neck7;
		private final ModelRenderer head;
		private final ModelRenderer leftRib;
		private final ModelRenderer rightRib;
		private final ModelRenderer leftArm;
		private final ModelRenderer rightArm;
		private final ModelRenderer leftLeg;
		private final ModelRenderer rightLeg;
		private final ModelRenderer[] headParts;

		public ModelChainAnimationTestMob() {
			textureWidth = 128;
			textureHeight = 128;
			whole = new ModelRenderer(this);
			whole.setRotationPoint(0.0F, 24.0F, 0.0F);
			body = new ModelRenderer(this);
			body.setRotationPoint(0.0F, -9.0F, 0.0F);
			whole.addChild(body);
			body.setTextureOffset(0, 0).addBox(-10.0F, -24.0F, -7.0F, 20.0F, 24.0F, 14.0F, 0.0F, false);
			neck1 = new ModelRenderer(this);
			neck1.setRotationPoint(0.0F, -23.0F, 0.0F);
			body.addChild(neck1);
			neck1.setTextureOffset(74, 82).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
			neck2 = new ModelRenderer(this);
			neck2.setRotationPoint(0.0F, -8.0F, 0.0F);
			neck1.addChild(neck2);
			neck2.setTextureOffset(74, 82).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
			neck3 = new ModelRenderer(this);
			neck3.setRotationPoint(0.0F, -8.0F, 0.0F);
			neck2.addChild(neck3);
			neck3.setTextureOffset(74, 82).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
			neck4 = new ModelRenderer(this);
			neck4.setRotationPoint(0.0F, -8.0F, 0.0F);
			neck3.addChild(neck4);
			neck4.setTextureOffset(74, 82).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
			neck5 = new ModelRenderer(this);
			neck5.setRotationPoint(0.0F, -8.0F, 0.0F);
			neck4.addChild(neck5);
			neck5.setTextureOffset(74, 82).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
			neck6 = new ModelRenderer(this);
			neck6.setRotationPoint(0.0F, -8.0F, 0.0F);
			neck5.addChild(neck6);
			neck6.setTextureOffset(74, 82).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
			neck7 = new ModelRenderer(this);
			neck7.setRotationPoint(0.0F, -8.0F, 0.0F);
			neck6.addChild(neck7);
			neck7.setTextureOffset(74, 82).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
			head = new ModelRenderer(this);
			head.setRotationPoint(0.0F, -8.0F, 0.0F);
			neck7.addChild(head);
			head.setTextureOffset(0, 38).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, false);
			leftRib = new ModelRenderer(this);
			leftRib.setRotationPoint(7.0F, -13.0F, -7.0F);
			body.addChild(leftRib);
			leftRib.setTextureOffset(108, 0).addBox(-7.0F, -11.0F, -0.1F, 10.0F, 24.0F, 0.0F, 0.0F, false);
			rightRib = new ModelRenderer(this);
			rightRib.mirror = true;
			rightRib.setRotationPoint(-7.0F, -13.0F, -7.0F);
			body.addChild(rightRib);
			rightRib.setTextureOffset(108, 0).addBox(-3.0F, -11.0F, -0.1F, 10.0F, 24.0F, 0.0F, 0.0F, true);
			leftArm = new ModelRenderer(this);
			leftArm.setRotationPoint(13.0F, -21.0F, 0.0F);
			body.addChild(leftArm);
			leftArm.setTextureOffset(64, 32).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 20.0F, 6.0F, 0.0F, false);
			rightArm = new ModelRenderer(this);
			rightArm.setRotationPoint(-13.0F, -21.0F, 0.0F);
			body.addChild(rightArm);
			rightArm.setTextureOffset(64, 32).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 20.0F, 6.0F, 0.0F, false);
			leftLeg = new ModelRenderer(this);
			leftLeg.setRotationPoint(6.0F, -9.0F, 0.0F);
			whole.addChild(leftLeg);
			leftLeg.setTextureOffset(0, 87).addBox(-4.0F, 0.0F, -3.0F, 7.0F, 9.0F, 7.0F, 0.0F, false);
			rightLeg = new ModelRenderer(this);
			rightLeg.setRotationPoint(-6.0F, -9.0F, 0.0F);
			whole.addChild(rightLeg);
			rightLeg.setTextureOffset(0, 87).addBox(-3.0F, 0.0F, -3.0F, 7.0F, 9.0F, 7.0F, 0.0F, false);
			this.headParts = new ModelRenderer[]{this.neck1, this.neck2, this.neck3, this.neck4, this.neck5, this.neck6, this.neck7, this.head};
		}

		public void chainSwingX(ModelRenderer[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount) {
			float offset = this.calculateChainOffset(rootOffset, boxes);
			for (int index = 0; index < boxes.length; index++) {
				boxes[index].rotateAngleX += this.calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
			}
		}

		public void chainSwingY(ModelRenderer[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount) {
			float offset = this.calculateChainOffset(rootOffset, boxes);
			for (int index = 0; index < boxes.length; index++) {
				boxes[index].rotateAngleY += this.calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
			}
		}

		private float calculateChainOffset(double rootOffset, ModelRenderer... boxes) {
			return (float) ((rootOffset * Math.PI) / (2 * boxes.length));
		}

		private float calculateChainRotation(float speed, float degree, float swing, float swingAmount, float offset, int boxIndex) {
			return MathHelper.cos(swing * (speed * this.movementScale) + offset * boxIndex) * swingAmount * (degree * this.movementScale);
		}

		@Override
		public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue,
				float alpha) {
			whole.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		}

		public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
			modelRenderer.rotateAngleX = x;
			modelRenderer.rotateAngleY = y;
			modelRenderer.rotateAngleZ = z;
		}

		public void setRotationAngles(Entity e, float f, float f1, float f2, float f3, float f4) {
			for (int index = 0; index < headParts.length; index++) {
				headParts[index].rotateAngleX = MathHelper.sin(f2 * (13 + index) / 200 + (float) Math.PI * (index / 6 + 0.3f))
						* Math.abs(MathHelper.cos(this.swingProgress * (float) Math.PI)) / 3
						+ MathHelper.sin(this.swingProgress * (float) Math.PI) / 2.5f
						+ (f4 / (180F / (float) Math.PI)) * MathHelper.sin(this.swingProgress * (float) Math.PI) / 8;
				headParts[index].rotateAngleY = MathHelper.sin(f2 * (17 + index) / 300 + (float) Math.PI * (index / 6 + 0.6f))
						* Math.abs(MathHelper.cos(this.swingProgress * (float) Math.PI)) / 3
						+ (f3 / (180F / (float) Math.PI)) * MathHelper.sin(this.swingProgress * (float) Math.PI) / 8;
				headParts[index].rotateAngleZ = MathHelper.sin(f2 * (23 + index) / 100 + (float) Math.PI * index / 6) / 3;
			}
			this.rightArm.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * f1;
			this.leftLeg.rotateAngleX = MathHelper.cos(f * 1.0F) * -1.0F * f1;
			this.rightLeg.rotateAngleX = MathHelper.cos(f * 1.0F) * 1.0F * f1;
			this.leftArm.rotateAngleX = MathHelper.cos(f * 0.6662F) * f1;
		}
	}
}
