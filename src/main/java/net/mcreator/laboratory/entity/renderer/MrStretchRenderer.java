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
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.IRenderTypeBuffer;

import net.mcreator.laboratory.entity.MrStretchEntity;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class MrStretchRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(MrStretchEntity.entity, renderManager -> {
				return new MobRenderer(renderManager, new ModelSteveIllager(), 0.5f) {
					{
						this.addLayer(new BullyHead<>(this));
						this.addLayer(new BullyBody<>(this));
						this.addLayer(new BullyLeftArm<>(this));
						this.addLayer(new BullyRightArm<>(this));
						this.addLayer(new BullyLeftLeg<>(this));
						this.addLayer(new BullyRightLeg<>(this));
					}
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("laboratory:textures/nothing.png");
					}
				};
			});
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static class BullyHead<T extends Entity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
		public BullyHead(IEntityRenderer<T, M> er) {
			super(er);
		}

		public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing,
				float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			LivingEntity entity = (LivingEntity) entitylivingbaseIn;
			float a = entity.swingProgress;
			a = 1 - a;
			a = a * a;
			a = a * a;
			if (entity.getHealth() / entity.getMaxHealth() <= 0.2) {
				matrixStackIn.translate(0, 0.75, 0);
			}
			IVertexBuilder ivertexbuilder = bufferIn
					.getBuffer(RenderType.getEntityCutoutNoCull(new ResourceLocation("laboratory:textures/bully_head.png")));
			this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn,
					LivingRenderer.getPackedOverlay((LivingEntity) entitylivingbaseIn, 0.0F), 1, 1, 1, 1);
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static class BullyBody<T extends Entity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
		public BullyBody(IEntityRenderer<T, M> er) {
			super(er);
		}

		public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing,
				float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			LivingEntity entity = (LivingEntity) entitylivingbaseIn;
			if (entity.getHealth() / entity.getMaxHealth() > 0.2) {
				IVertexBuilder ivertexbuilder = bufferIn
						.getBuffer(RenderType.getEntityCutoutNoCull(new ResourceLocation("laboratory:textures/bully_body.png")));
				this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn,
						LivingRenderer.getPackedOverlay((LivingEntity) entitylivingbaseIn, 0), 1, 1, 1, 1);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static class BullyLeftArm<T extends Entity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
		public BullyLeftArm(IEntityRenderer<T, M> er) {
			super(er);
		}

		public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing,
				float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			LivingEntity entity = (LivingEntity) entitylivingbaseIn;
			if (entity.getHealth() / entity.getMaxHealth() > 0.8) {
				IVertexBuilder ivertexbuilder = bufferIn
						.getBuffer(RenderType.getEntityCutoutNoCull(new ResourceLocation("laboratory:textures/bully_left_arm.png")));
				this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn,
						LivingRenderer.getPackedOverlay((LivingEntity) entitylivingbaseIn, 0.0F), 1, 1, 1, 1);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static class BullyRightArm<T extends Entity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
		public BullyRightArm(IEntityRenderer<T, M> er) {
			super(er);
		}

		public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing,
				float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			LivingEntity entity = (LivingEntity) entitylivingbaseIn;
			if (entity.getHealth() / entity.getMaxHealth() > 0.6) {
				IVertexBuilder ivertexbuilder = bufferIn
						.getBuffer(RenderType.getEntityCutoutNoCull(new ResourceLocation("laboratory:textures/bully_right_arm.png")));
				this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn,
						LivingRenderer.getPackedOverlay((LivingEntity) entitylivingbaseIn, 0.0F), 1, 1, 1, 1);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static class BullyLeftLeg<T extends Entity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
		public BullyLeftLeg(IEntityRenderer<T, M> er) {
			super(er);
		}

		public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing,
				float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			LivingEntity entity = (LivingEntity) entitylivingbaseIn;
			if (entity.getHealth() / entity.getMaxHealth() <= 0.2) {
				matrixStackIn.translate(0, -0.75, 0);
			}
			IVertexBuilder ivertexbuilder = bufferIn
					.getBuffer(RenderType.getEntityCutoutNoCull(new ResourceLocation("laboratory:textures/bully_left_leg.png")));
			this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn,
					LivingRenderer.getPackedOverlay((LivingEntity) entitylivingbaseIn, 0.0F), 1, 1, 1, 1);
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static class BullyRightLeg<T extends Entity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
		public BullyRightLeg(IEntityRenderer<T, M> er) {
			super(er);
		}

		public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing,
				float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			LivingEntity entity = (LivingEntity) entitylivingbaseIn;
			IVertexBuilder ivertexbuilder = bufferIn
					.getBuffer(RenderType.getEntityCutoutNoCull(new ResourceLocation("laboratory:textures/bully_right_leg.png")));
			this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn,
					LivingRenderer.getPackedOverlay((LivingEntity) entitylivingbaseIn, 0.0F), 1, 1, 1, 1);
		}
	}

	// Made with Blockbench 3.9.3
	// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
	// Paste this class into your mod and generate all required imports
	public static class ModelSteveIllager extends EntityModel<Entity> {
		private final ModelRenderer body;
		private final ModelRenderer head;
		private final ModelRenderer rightarm;
		private final ModelRenderer leftarm;
		private final ModelRenderer leftLeg;
		private final ModelRenderer rightLeg;
		public ModelSteveIllager() {
			textureWidth = 64;
			textureHeight = 64;
			body = new ModelRenderer(this);
			body.setRotationPoint(0.0F, 0.0F, 0.0F);
			body.setTextureOffset(0, 34).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, 0.0F, false);
			body.setTextureOffset(0, 0).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, 0.5F, false);
			head = new ModelRenderer(this);
			head.setRotationPoint(0.0F, 0.0F, 0.0F);
			body.addChild(head);
			head.setTextureOffset(20, 16).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.0F, false);
			rightarm = new ModelRenderer(this);
			rightarm.setRotationPoint(6.0F, 2.0F, 0.0F);
			body.addChild(rightarm);
			rightarm.setTextureOffset(28, 34).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
			leftarm = new ModelRenderer(this);
			leftarm.setRotationPoint(-6.0F, 2.0F, 0.0F);
			body.addChild(leftarm);
			leftarm.setTextureOffset(28, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
			leftLeg = new ModelRenderer(this);
			leftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
			leftLeg.setTextureOffset(44, 34).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
			rightLeg = new ModelRenderer(this);
			rightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
			rightLeg.setTextureOffset(44, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		}

		@Override
		public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue,
				float alpha) {
			// RenderType.getEntityAlpha(locationIn, refIn)
			body.render(matrixStack, buffer, packedLight, packedOverlay);
			leftLeg.render(matrixStack, buffer, packedLight, packedOverlay);
			rightLeg.render(matrixStack, buffer, packedLight, packedOverlay);
		}

		public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
			modelRenderer.rotateAngleX = x;
			modelRenderer.rotateAngleY = y;
			modelRenderer.rotateAngleZ = z;
		}

		public void setRotationAngles(Entity e, float f, float f1, float f2, float f3, float f4) {
			float a = this.swingProgress;
			a = 1 - a;
			a = a * a;
			a = a * a;
			this.head.rotateAngleY = f3 / (180F / (float) Math.PI);
			this.head.rotateAngleX = f4 / (180F / (float) Math.PI) + MathHelper.sin(a * (float) Math.PI) * 2;
			this.rightLeg.rotateAngleX = MathHelper.cos(f * 1.0F) * 1.0F * f1;
			this.rightarm.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * f1 - MathHelper.sin(a * (float) Math.PI) * 2;
			this.leftLeg.rotateAngleX = MathHelper.cos(f * 1.0F) * -1.0F * f1;
			this.leftarm.rotateAngleX = MathHelper.cos(f * 0.6662F) * f1 - MathHelper.sin(a * (float) Math.PI) * 2;
		}
	}
}
