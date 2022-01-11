package net.mcreator.laboratory.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.HandSide;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.block.Blocks;

import net.mcreator.laboratory.entity.MrBlockEntity;

import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class MrBlockRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(MrBlockEntity.entity, renderManager -> {
				BipedRenderer customRender = new BipedRenderer(renderManager, new BipedModel(0), 0.5f) {
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("laboratory:textures/original_steve.png");
					}

					@Override
					public void render(Entity e, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
							int packedLightIn) {
						MobEntity entityL = (MobEntity) e;
						matrixStackIn.scale(2, 2, 2);
						if (entityL.swingProgress > 0) {
							if (!entityL.isLeftHanded()) {
								((BipedModel) this.getEntityModel()).rightArmPose = BipedModel.ArmPose.BLOCK;
								((BipedModel) this.getEntityModel()).leftArmPose = BipedModel.ArmPose.EMPTY;
							} else {
								((BipedModel) this.getEntityModel()).rightArmPose = BipedModel.ArmPose.EMPTY;
								((BipedModel) this.getEntityModel()).leftArmPose = BipedModel.ArmPose.BLOCK;
							}
						} else {
							((BipedModel) this.getEntityModel()).rightArmPose = BipedModel.ArmPose.EMPTY;
							((BipedModel) this.getEntityModel()).leftArmPose = BipedModel.ArmPose.EMPTY;
						}
						super.render(e, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
					}

					protected void applyRotations(LivingEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw,
							float partialTicks) {
						super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
						if (!((double) entityLiving.limbSwingAmount < 0.01D)) {
							float f = 13.0F;
							float f1 = entityLiving.limbSwing - entityLiving.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
							float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
							matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(6.5F * f2));
						}
					}
				};
				customRender.addLayer(new BipedArmorLayer(customRender, new BipedModel(0.5f), new BipedModel(1)));
				customRender.addLayer(new GlowingLayer<>(customRender));
				return customRender;
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
			LivingEntity livingEntityIn = (LivingEntity) entitylivingbaseIn;
			MobEntity entityM = (MobEntity) entitylivingbaseIn;
			float a = 1.0F - livingEntityIn.swingProgress;
			a = a * a;
			a = a * a;
			if (!livingEntityIn.isInvisible()) {
				for (int i = 0; i < 5; i++) {
					this.renderFire(matrixStackIn, bufferIn, packedLightIn, livingEntityIn, HandSide.RIGHT, ((double) i / 4) - 0.1,
							(entityM.isLeftHanded() ? 0 : MathHelper.sin(a * (float) Math.PI * 2) * i * 0.3), ageInTicks);
					this.renderFire(matrixStackIn, bufferIn, packedLightIn, livingEntityIn, HandSide.LEFT, ((double) i / 4) - 0.1,
							(!entityM.isLeftHanded() ? 0 : MathHelper.sin(a * (float) Math.PI * 2) * i * 0.3), ageInTicks);
				}
			}
			// this.getEntityModel().render(matrixStackIn, ivertexbuilder, 15728640,
			// OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
		}

		private void renderFire(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity e, HandSide hand, double height,
				double handprogress, float ageInTicks) {
			LivingEntity livingEntityIn = (LivingEntity) e;
			matrixStackIn.push();
			((BipedModel) this.getEntityModel()).translateHand(hand, matrixStackIn);
			float f = 0.4375F;
			matrixStackIn.translate((hand == HandSide.LEFT ? 0.245D : -0.15D) + 0.21875D, height + handprogress, -0.21875D);
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
			matrixStackIn.scale(f * 1.2f, -f * 1.2f - (float) handprogress, -f * 1.2f);
			Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(Blocks.SOUL_CAMPFIRE.getDefaultState(), matrixStackIn, bufferIn,
					packedLightIn, LivingRenderer.getPackedOverlay(livingEntityIn, 0.0F), EmptyModelData.INSTANCE);
			matrixStackIn.pop();
		}
	}
}
