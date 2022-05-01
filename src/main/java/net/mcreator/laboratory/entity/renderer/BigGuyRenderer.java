
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

import net.mcreator.laboratory.entity.BigGuyEntity;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class BigGuyRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(BigGuyEntity.entity, renderManager -> {
				return new MobRenderer(renderManager, new ModelHosungLee(), 0.7f) {
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("laboratory:textures/hosunglee.png");
					}
				};
			});
		}
	}

	// Made with Blockbench 3.8.4
	// Exported for Minecraft version 1.15 - 1.16
	// Paste this class into your mod and generate all required imports
	public static class ModelHosungLee<T extends Entity> extends EntityModel<T> {
		public final ModelRenderer Whole;
		public final ModelRenderer lowerBody;
		public final ModelRenderer upperBody;
		public final ModelRenderer neck;
		public final ModelRenderer head;
		public final ModelRenderer rightShoulder;
		public final ModelRenderer rightWrist;
		public final ModelRenderer bat;
		public final ModelRenderer leftShoulder;
		public final ModelRenderer leftWrist;
		public final ModelRenderer rightThigh;
		public final ModelRenderer rightShank;
		public final ModelRenderer leftThigh;
		public final ModelRenderer leftShank;

		public ModelHosungLee() {
			textureWidth = 128;
			textureHeight = 128;
			Whole = new ModelRenderer(this);
			Whole.setRotationPoint(0.0F, 24.0F, 0.0F);
			lowerBody = new ModelRenderer(this);
			lowerBody.setRotationPoint(0.0F, -23.0F, 1.0F);
			Whole.addChild(lowerBody);
			lowerBody.setTextureOffset(34, 39).addBox(-5.0F, -8.0F, -5.0F, 10.0F, 12.0F, 10.0F, 0.0F, false);
			lowerBody.setTextureOffset(0, 102).addBox(5.0F, -8.0F, -4.0F, 3.0F, 6.0F, 8.0F, 0.0F, false);
			lowerBody.setTextureOffset(45, 97).addBox(-8.0F, -8.0F, -4.0F, 3.0F, 6.0F, 8.0F, 0.0F, false);
			upperBody = new ModelRenderer(this);
			upperBody.setRotationPoint(0.0F, -8.0F, 0.0F);
			lowerBody.addChild(upperBody);
			upperBody.setTextureOffset(55, 61).addBox(6.0F, -13.0F, -6.0F, 6.0F, 9.0F, 11.0F, 0.0F, false);
			upperBody.setTextureOffset(21, 61).addBox(-12.0F, -13.0F, -6.0F, 6.0F, 9.0F, 11.0F, 0.0F, false);
			upperBody.setTextureOffset(96, 34).addBox(-9.0F, -4.0F, -5.0F, 3.0F, 4.0F, 10.0F, 0.0F, false);
			upperBody.setTextureOffset(96, 34).addBox(6.0F, -4.0F, -5.0F, 3.0F, 4.0F, 10.0F, 0.0F, true);
			upperBody.setTextureOffset(0, 0).addBox(-6.0F, -14.0F, -7.0F, 12.0F, 14.0F, 13.0F, 0.0F, false);
			upperBody.setTextureOffset(78, 61).addBox(-5.0F, -15.0F, -4.0F, 10.0F, 1.0F, 7.0F, 0.0F, false);
			neck = new ModelRenderer(this);
			neck.setRotationPoint(0.0F, -13.0F, 0.0F);
			upperBody.addChild(neck);
			neck.setTextureOffset(44, 61).addBox(-2.5F, -4.0F, -2.5F, 5.0F, 3.0F, 4.0F, 0.0F, false);
			head = new ModelRenderer(this);
			head.setRotationPoint(0.0F, -4.0F, -1.0F);
			neck.addChild(head);
			head.setTextureOffset(30, 81).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
			head.setTextureOffset(39, 22).addBox(-4.0F, -11.0F, -7.0F, 8.0F, 4.0F, 11.0F, 0.5F, false);
			rightShoulder = new ModelRenderer(this);
			rightShoulder.setRotationPoint(-12.0F, -9.0F, 1.0F);
			upperBody.addChild(rightShoulder);
			rightShoulder.setTextureOffset(50, 0).addBox(-8.0F, -3.0F, -4.0F, 8.0F, 14.0F, 8.0F, 0.0F, false);
			rightWrist = new ModelRenderer(this);
			rightWrist.setRotationPoint(-4.0F, 11.0F, 0.0F);
			rightShoulder.addChild(rightWrist);
			rightWrist.setTextureOffset(82, 0).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 17.0F, 5.0F, 0.0F, false);
			bat = new ModelRenderer(this);
			bat.setRotationPoint(0.0F, 14.0F, 0.0F);
			rightWrist.addChild(bat);
			bat.setTextureOffset(84, 83).addBox(-2.0F, -2.0F, -6.0F, 4.0F, 4.0F, 10.0F, 0.0F, false);
			bat.setTextureOffset(0, 27).addBox(-3.0F, -3.0F, -34.0F, 6.0F, 6.0F, 16.0F, 0.0F, false);
			bat.setTextureOffset(0, 27).addBox(-2.5F, -2.5F, -36.0F, 5.0F, 5.0F, 2.0F, 0.0F, false);
			bat.setTextureOffset(65, 25).addBox(-2.5F, -2.5F, -18.0F, 5.0F, 5.0F, 12.0F, 0.0F, false);
			leftShoulder = new ModelRenderer(this);
			leftShoulder.setRotationPoint(12.0F, -9.0F, 1.0F);
			upperBody.addChild(leftShoulder);
			leftShoulder.setTextureOffset(0, 49).addBox(0.0F, -3.0F, -4.0F, 8.0F, 14.0F, 8.0F, 0.0F, false);
			leftWrist = new ModelRenderer(this);
			leftWrist.setRotationPoint(4.0F, 11.0F, 0.0F);
			leftShoulder.addChild(leftWrist);
			leftWrist.setTextureOffset(62, 81).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 17.0F, 5.0F, 0.0F, false);
			rightThigh = new ModelRenderer(this);
			rightThigh.setRotationPoint(-4.0F, -24.0F, 1.0F);
			Whole.addChild(rightThigh);
			rightThigh.setTextureOffset(0, 81).addBox(-4.0F, 0.0F, -4.0F, 7.0F, 11.0F, 8.0F, 0.0F, false);
			rightShank = new ModelRenderer(this);
			rightShank.setRotationPoint(1.0F, 11.0F, 0.0F);
			rightThigh.addChild(rightShank);
			rightShank.setTextureOffset(104, 0).addBox(-4.0F, 0.0F, -2.0F, 5.0F, 11.0F, 5.0F, 0.0F, false);
			rightShank.setTextureOffset(87, 22).addBox(-4.0F, 11.0F, -7.0F, 5.0F, 2.0F, 10.0F, 0.0F, false);
			leftThigh = new ModelRenderer(this);
			leftThigh.setRotationPoint(4.0F, -24.0F, 1.0F);
			Whole.addChild(leftThigh);
			leftThigh.setTextureOffset(74, 42).addBox(-3.0F, 0.0F, -4.0F, 7.0F, 11.0F, 8.0F, 0.0F, false);
			leftShank = new ModelRenderer(this);
			leftShank.setRotationPoint(-1.0F, 11.0F, 0.0F);
			leftThigh.addChild(leftShank);
			leftShank.setTextureOffset(84, 97).addBox(-1.0F, 0.0F, -2.0F, 5.0F, 11.0F, 5.0F, 0.0F, false);
			leftShank.setTextureOffset(79, 71).addBox(-1.0F, 11.0F, -7.0F, 5.0F, 2.0F, 10.0F, 0.0F, false);
		}

		@Override
		public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue,
				float alpha) {
			Whole.render(matrixStack, buffer, packedLight, packedOverlay);
		}

		public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
			modelRenderer.rotateAngleX = x;
			modelRenderer.rotateAngleY = y;
			modelRenderer.rotateAngleZ = z;
		}

		public void setRotationAngles(T e, float f, float f1, float f2, float f3, float f4) {
			float num1 = f4 / (180F * 12 / (float) Math.PI);
			MobEntity entityMob = (MobEntity) e;
			float a = 1.0F - this.swingProgress;
			a = a * a;
			a = a * a;
			if (this.swingProgress > 0) {
				this.rightShoulder.rotateAngleX = 4 * (MathHelper.sin(a * (float) Math.PI)) - 2;
				this.rightShoulder.rotateAngleY = -MathHelper.sin(a * (float) Math.PI);
				this.rightWrist.rotateAngleX = -1 + MathHelper.sin(a * (float) Math.PI);
				this.lowerBody.rotateAngleX = num1 + MathHelper.sin(a * (float) Math.PI) / 3;
				this.upperBody.rotateAngleX = num1 + MathHelper.sin(a * (float) Math.PI) / 3;
			} else {
				if (entityMob.isAggressive()) {
					this.rightShoulder.rotateAngleX = -2 + MathHelper.sin(f2 * 0.7f) * MathHelper.sin(f2 * 1.7f) * 0.2f;
					this.rightShoulder.rotateAngleY = MathHelper.sin(f2 * 0.66f) * MathHelper.sin(f2 * 1.33f) * 0.2f;
					this.rightWrist.rotateAngleX = -1f + MathHelper.sin(f2 * 0.3f) * MathHelper.sin(f2 * 2.3f) * 0.2f;;
					this.head.rotateAngleZ = MathHelper.sin(f2 * 0.7f) * MathHelper.sin(f2 * 1.7f) * 0.2f;
					this.neck.rotateAngleZ = MathHelper.sin(f2 * 0.3f) * MathHelper.sin(f2 * 2.3f) * 0.2f;
				} else {
					this.rightWrist.rotateAngleX = (MathHelper.cos(f * 0.4f + (float) Math.PI) - 1) * f1 / 3;
					this.rightShoulder.rotateAngleX = MathHelper.cos(f * 0.4f + (float) Math.PI) * f1 / 3;
					this.rightShoulder.rotateAngleY = 0;
					this.head.rotateAngleZ = 0;
					this.neck.rotateAngleZ = 0;
				}
				this.lowerBody.rotateAngleX = num1;
				this.upperBody.rotateAngleX = num1;
			}
			this.Whole.rotateAngleX = (MathHelper.cos(f * 0.8F) + 2) * f1 / 6;
			this.leftWrist.rotateAngleX = (MathHelper.cos(f * 0.4f) - 1) * f1 / 3;
			this.leftShoulder.rotateAngleX = MathHelper.cos(f * 0.4f) * f1 / 3;
			this.neck.rotateAngleX = num1 * 4;
			this.head.rotateAngleY = num1 * 6;
			this.head.rotateAngleX = f4 / (180F / (float) Math.PI);
			this.rightThigh.rotateAngleX = (MathHelper.cos(f * 0.4f) - 0.8f) * f1 / 2;
			this.rightShank.rotateAngleX = (MathHelper.cos(f * 0.4F) + 1) * 1.0F * f1 / 3;
			this.leftThigh.rotateAngleX = (MathHelper.cos(f * 0.4F + (float) Math.PI) - 0.8f) * f1 / 2;
			this.leftShank.rotateAngleX = (MathHelper.cos(f * 0.4F + (float) Math.PI) + 1) * f1 / 3;
		}
	}
}
