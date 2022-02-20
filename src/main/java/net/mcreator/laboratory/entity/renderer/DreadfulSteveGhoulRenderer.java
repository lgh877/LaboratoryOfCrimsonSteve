package net.mcreator.laboratory.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.IRenderTypeBuffer;

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
				return new MobRenderer(renderManager, new ModelDreadful_Soul_Ghoul(), 1.2f) {
					{
						this.addLayer(new GlowingLayer<>(this));
					}

					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("laboratory:textures/dreadful_soul_ghoul.png");
					}
				};
			});
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static class GlowingLayer<T extends Entity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
		public GlowingLayer(IEntityRenderer<T, M> er) {
			super(er);
		}

		public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing,
				float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			IVertexBuilder ivertexbuilder = bufferIn
					.getBuffer(RenderType.getEyes(new ResourceLocation("laboratory:textures/dreadful_soul_ghoul_glowing.png")));
			this.getEntityModel().render(matrixStackIn, ivertexbuilder, 15728640, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
		}
	}

	// Made with Blockbench 4.1.1
	// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
	// Paste this class into your mod and generate all required imports
	public static class ModelDreadful_Soul_Ghoul extends EntityModel<Entity> {
		private final ModelRenderer whole;
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
			whole = new ModelRenderer(this);
			whole.setRotationPoint(0.0F, 24.0F, 0.0F);
			lowerBody = new ModelRenderer(this);
			lowerBody.setRotationPoint(0.0F, -30.0F, 0.0F);
			whole.addChild(lowerBody);
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
			rightLeg1.setRotationPoint(-5.0F, -30.0F, 0.0F);
			whole.addChild(rightLeg1);
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
			leftLeg1.setRotationPoint(5.0F, -30.0F, 0.0F);
			whole.addChild(leftLeg1);
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
			whole.render(matrixStack, buffer, packedLight, packedOverlay);
		}

		public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
			modelRenderer.rotateAngleX = x;
			modelRenderer.rotateAngleY = y;
			modelRenderer.rotateAngleZ = z;
		}

		public void setRotationAngles(Entity e, float f, float f1, float f2, float f3, float f4) {
			DreadfulSteveGhoulEntity.CustomEntity entityM = (DreadfulSteveGhoulEntity.CustomEntity) e;
			float pi = (float) Math.PI;
			float pitch = f4 / (180F / pi);
			float headYaw = f3 / (180F / pi);
			float idle = (MathHelper.sin(f2 * 0.05f) + 1) * 0.5f * (1 - f1) + f1;
			float a1 = f2 - (float) entityM.ticksExisted;
			float a2 = entityM.getAnimationScale(a1, 1) / entityM.getAttackProgressEnd();
			float a3 = entityM.getAnimationScale(a1, 0) / 6;
			float meleeAttack = a2 * a2 * a2;
			{
				rightArm.setRotationPoint(-13.0F, -7.0F, 0.0F);
				lowerBody.setRotationPoint(0.0F, -30.0F, 0.0F);
				upperBody.setRotationPoint(0.0F, -13.0F, 0.0F);
			}
			if (entityM.moveForward > 0)
				this.whole.rotateAngleX = (MathHelper.sin(f * 1.2f) + 1) * f1 * 0.1f;
			else
				this.whole.rotateAngleX = (MathHelper.sin(f * 1.2f) - 0.5f) * f1 * 0.1f;
			this.head.rotateAngleZ = MathHelper.clamp(MathHelper.cos(f2 * 0.04f), -0.1f, 0.1f);
			this.head.rotateAngleY = headYaw * 0.6f - meleeAttack;
			this.head.rotateAngleX = pitch * 0.6f - idle * 0.3f;
			this.upperBody.rotateAngleX = pitch * 0.2f + idle * 0.15f;
			this.upperBody.rotateAngleY = headYaw * 0.2f + meleeAttack * 0.5f;
			this.leftArm.rotateAngleX = MathHelper.cos(f * 0.6f) * f1 * 0.4f - idle * 0.3f - meleeAttack * 3;
			this.leftArm.rotateAngleZ = 0;
			this.rightArm.rotateAngleX = (MathHelper.cos(f * 0.6f + pi) * f1 * 0.4f - idle * 0.3f) * (1 - a3) + (-pi / 2 + pitch * 0.6f) * a3;
			this.rightArm.rotateAngleY = -meleeAttack + (headYaw * 0.6f) * a3;
			this.rightArm.rotateAngleZ = 0;
			if (entityM.isSwingInProgress) {
				float s1 = 1 - this.swingProgress;
				s1 = (float) Math.pow((double) s1, 3);
				this.rightArm.rotateAngleX += -MathHelper.sin(s1 * pi) * 0.2f;
				this.rightArm.rotationPointZ += MathHelper.sin(s1 * pi) * 10;
				this.lowerBody.rotationPointZ += MathHelper.sin(s1 * pi) * 5;
				this.upperBody.rotationPointZ += MathHelper.sin(s1 * pi) * 5;
			}
			this.lowerBody.rotateAngleX = pitch * 0.2f + idle * 0.15f;
			this.lowerBody.rotateAngleY = headYaw * 0.2f + meleeAttack * 0.5f;
			this.leftLeg1.rotateAngleX = (MathHelper.cos(f * 0.6f + pi) - 0.3f) * f1 * 0.3f;
			this.leftLeg2.rotateAngleX = (MathHelper.cos(f * 0.6f + pi * 0.6f) + 1) * f1 * 0.15f;
			this.leftLeg3.rotateAngleX = (MathHelper.cos(f * 0.6f + pi * 0.6f) + 1) * f1 * 0.15f;
			this.rightLeg1.rotateAngleX = (MathHelper.cos(f * 0.6F) - 0.3f) * f1 * 0.3f;
			this.rightLeg2.rotateAngleX = (MathHelper.cos(f * 0.6F - pi * 0.4f) + 1) * f1 * 0.15f;
			this.rightLeg3.rotateAngleX = (MathHelper.cos(f * 0.6F - pi * 0.4f) + 1) * f1 * 0.15f;
			ModelHelper.func_239101_a_(this.rightArm, this.leftArm, f2);
		}
	}
}
