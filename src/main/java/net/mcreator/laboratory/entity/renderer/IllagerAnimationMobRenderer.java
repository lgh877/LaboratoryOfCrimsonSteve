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
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.MobRenderer;

import net.mcreator.laboratory.entity.IllagerAnimationMobEntity;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class IllagerAnimationMobRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(IllagerAnimationMobEntity.entity, renderManager -> {
				return new MobRenderer(renderManager, new Modeliron_golem_with_joints(), 0.7f) {
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("laboratory:textures/test.png");
					}
				};
			});
		}
	}

	// Made with Blockbench 3.9.3
	// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
	// Paste this class into your mod and generate all required imports
	public static class Modeliron_golem_with_joints extends EntityModel<Entity> {
		private final ModelRenderer Whole;
		private final ModelRenderer lowerBody;
		private final ModelRenderer upperBody;
		private final ModelRenderer head;
		private final ModelRenderer leftShoulder;
		private final ModelRenderer leftWrist;
		private final ModelRenderer rightShoulder;
		private final ModelRenderer rightWrist;
		private final ModelRenderer leftThigh;
		private final ModelRenderer leftShank;
		private final ModelRenderer rightThigh;
		private final ModelRenderer rightShank;

		public Modeliron_golem_with_joints() {
			textureWidth = 128;
			textureHeight = 128;
			Whole = new ModelRenderer(this);
			Whole.setRotationPoint(0.0F, 24.0F, 0.0F);
			lowerBody = new ModelRenderer(this);
			lowerBody.setRotationPoint(0.0F, -16.0F, 0.0F);
			Whole.addChild(lowerBody);
			lowerBody.setTextureOffset(47, 0).addBox(-4.5F, -5.0F, -3.0F, 9.0F, 5.0F, 6.0F, 0.5F, false);
			upperBody = new ModelRenderer(this);
			upperBody.setRotationPoint(0.0F, -5.0F, 0.0F);
			lowerBody.addChild(upperBody);
			upperBody.setTextureOffset(0, 0).addBox(-9.0F, -12.0F, -6.0F, 18.0F, 12.0F, 11.0F, 0.0F, false);
			head = new ModelRenderer(this);
			head.setRotationPoint(0.0F, -10.0F, -2.0F);
			upperBody.addChild(head);
			head.setTextureOffset(0, 23).addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F, 0.0F, false);
			head.setTextureOffset(0, 0).addBox(-1.0F, -5.0F, -7.5F, 2.0F, 4.0F, 2.0F, 0.0F, false);
			leftShoulder = new ModelRenderer(this);
			leftShoulder.setRotationPoint(9.0F, -10.0F, 0.0F);
			upperBody.addChild(leftShoulder);
			leftShoulder.setTextureOffset(40, 44).addBox(0.0F, -2.5F, -3.0F, 4.0F, 15.0F, 6.0F, 0.0F, false);
			leftWrist = new ModelRenderer(this);
			leftWrist.setRotationPoint(2.0F, 12.0F, 0.0F);
			leftShoulder.addChild(leftWrist);
			leftWrist.setTextureOffset(20, 44).addBox(-2.0F, 0.5F, -3.0F, 4.0F, 15.0F, 6.0F, 0.0F, false);
			rightShoulder = new ModelRenderer(this);
			rightShoulder.setRotationPoint(-10.0F, -10.0F, 0.0F);
			upperBody.addChild(rightShoulder);
			rightShoulder.setTextureOffset(0, 41).addBox(-3.0F, -2.5F, -3.0F, 4.0F, 15.0F, 6.0F, 0.0F, false);
			rightWrist = new ModelRenderer(this);
			rightWrist.setRotationPoint(-1.0F, 12.0F, 0.0F);
			rightShoulder.addChild(rightWrist);
			rightWrist.setTextureOffset(32, 23).addBox(-2.0F, 0.5F, -3.0F, 4.0F, 15.0F, 6.0F, 0.0F, false);
			leftThigh = new ModelRenderer(this);
			leftThigh.setRotationPoint(4.0F, -13.0F, 0.0F);
			Whole.addChild(leftThigh);
			leftThigh.setTextureOffset(0, 62).addBox(-2.5F, -3.0F, -3.0F, 6.0F, 8.0F, 5.0F, 0.0F, false);
			leftShank = new ModelRenderer(this);
			leftShank.setRotationPoint(1.0F, 5.0F, 0.0F);
			leftThigh.addChild(leftShank);
			leftShank.setTextureOffset(60, 49).addBox(-3.5F, 0.0F, -3.0F, 6.0F, 8.0F, 5.0F, 0.0F, false);
			rightThigh = new ModelRenderer(this);
			rightThigh.setRotationPoint(-4.0F, -13.0F, 0.0F);
			Whole.addChild(rightThigh);
			rightThigh.setTextureOffset(54, 36).addBox(-3.5F, -3.0F, -3.0F, 6.0F, 8.0F, 5.0F, 0.0F, false);
			rightShank = new ModelRenderer(this);
			rightShank.setRotationPoint(-1.0F, 5.0F, 0.0F);
			rightThigh.addChild(rightShank);
			rightShank.setTextureOffset(52, 23).addBox(-2.5F, 0.0F, -3.0F, 6.0F, 8.0F, 5.0F, 0.0F, false);
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

		public void setRotationAngles(Entity e, float f, float f1, float f2, float f3, float f4) {
			this.head.rotateAngleY = f3 / (180F / (float) Math.PI);
			this.head.rotateAngleX = f4 / (180F / (float) Math.PI);
			this.rightThigh.rotateAngleX = MathHelper.cos(f * 1.0F) * 1.0F * f1;
			this.leftThigh.rotateAngleX = MathHelper.cos(f * 1.0F) * -1.0F * f1;
			ModelHelper.func_239105_a_(this.leftShoulder, this.rightShoulder, ((MobEntity) e).isAggressive(), -this.swingProgress, f2);
			ModelHelper.func_239105_a_(this.leftWrist, this.rightWrist, ((MobEntity) e).isAggressive(), this.swingProgress, f2);
			this.leftShoulder.rotateAngleX += (float) Math.PI / 3;
			this.rightShoulder.rotateAngleX += (float) Math.PI / 3;
			this.leftWrist.rotateAngleX += (float) Math.PI / 3;
			this.rightWrist.rotateAngleX += (float) Math.PI / 3;
		}
	}
}
