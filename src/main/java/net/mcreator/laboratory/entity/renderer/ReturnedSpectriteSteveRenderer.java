package net.mcreator.laboratory.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.IRenderTypeBuffer;

import net.mcreator.laboratory.entity.ReturnedSpectriteSteveEntity;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class ReturnedSpectriteSteveRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(ReturnedSpectriteSteveEntity.entity, renderManager -> {
				BipedRenderer customRender = new BipedRenderer(renderManager, new BipedModel(0), 0.5f) {
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("laboratory:textures/nothing.png");
					}
				};
				customRender.addLayer(new BipedArmorLayer(customRender, new BipedModel(0.5f), new BipedModel(1)));
				customRender.addLayer(new GlowingLayer1<>(customRender));
				for (int i = 0; i < 4; i++) {
					customRender.addLayer(new GlowingLayer2<>(customRender));
				}
				for (int i = 0; i < 2; i++) {
					customRender.addLayer(new GlowingLayer3<>(customRender));
				}
				return customRender;
			});
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static class GlowingLayer1<T extends Entity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
		public GlowingLayer1(IEntityRenderer<T, M> er) {
			super(er);
		}

		public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing,
				float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			LivingEntity entity = (LivingEntity) entitylivingbaseIn;
			if (!entity.isAlive()) {
				matrixStackIn.rotate(Vector3f.XP.rotationDegrees(360 * (float) Math.random()));
				matrixStackIn.rotate(Vector3f.YP.rotationDegrees(360 * (float) Math.random()));
				matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(360 * (float) Math.random()));
			} else {
				matrixStackIn.rotate(Vector3f.XP.rotationDegrees(0));
				matrixStackIn.rotate(Vector3f.YP.rotationDegrees(0));
				matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(0));
			}
			if (entity.swingProgress == 0) {
				IVertexBuilder ivertexbuilder = bufferIn
						.getBuffer(RenderType.getEntityCutoutNoCull(new ResourceLocation("laboratory:textures/white_steve.png")));
				this.getEntityModel().render(matrixStackIn, ivertexbuilder, 15728640, LivingRenderer.getPackedOverlay(entity, 0.0F),
						(MathHelper.sin(entity.ticksExisted * 1.3f) + 1) / 2, (MathHelper.sin(entity.ticksExisted * 0.7f) + 1) / 2,
						(MathHelper.sin(entity.ticksExisted * 0.5f) + 1) / 2, 1);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static class GlowingLayer3<T extends Entity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
		public GlowingLayer3(IEntityRenderer<T, M> er) {
			super(er);
		}

		public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing,
				float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			LivingEntity entity = (LivingEntity) entitylivingbaseIn;
			float a = 1.0F - entity.swingProgress;
			a = a * a;
			a = a * a;
			if (!entity.isAlive()) {
				matrixStackIn.scale((float) Math.random() * 3, (float) Math.random() * 3, (float) Math.random() * 3);
				matrixStackIn.rotate(Vector3f.XP.rotationDegrees(360 * (float) Math.random()));
				matrixStackIn.rotate(Vector3f.YP.rotationDegrees(360 * (float) Math.random()));
				matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(360 * (float) Math.random()));
				matrixStackIn.translate(0, 0, 0);
				IVertexBuilder ivertexbuilder = bufferIn
						.getBuffer(RenderType.getEnergySwirl(new ResourceLocation("laboratory:textures/white_steve.png"), 44, 44));
				this.getEntityModel().render(matrixStackIn, ivertexbuilder, 15728640, OverlayTexture.NO_OVERLAY, (float) Math.random(),
						(float) Math.random() * 3, (float) Math.random() * 3, (float) Math.random() * 3);
			}
			matrixStackIn.scale(1, 1, 1);
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static class GlowingLayer2<T extends Entity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
		public GlowingLayer2(IEntityRenderer<T, M> er) {
			super(er);
		}

		public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing,
				float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			LivingEntity entity = (LivingEntity) entitylivingbaseIn;
			float a = 1.0F - entity.swingProgress;
			a = a * a;
			a = a * a;
			if (entity.swingProgress > 0) {
				matrixStackIn.scale((float) Math.random() + 0.5f, (float) Math.random() + 0.5f, (float) Math.random() + 0.5f);
				matrixStackIn.rotate(Vector3f.XP.rotationDegrees(360 * (float) Math.random()));
				matrixStackIn.rotate(Vector3f.YP.rotationDegrees(360 * (float) Math.random()));
				matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(360 * (float) Math.random()));
				matrixStackIn.translate((Math.random() - 0.5) * (1 + (1 - a) * 2), (Math.random() - 0.5) * (1 + (1 - a) * 2),
						(Math.random() - 0.5) * (1 + (1 - a) * 2));
				IVertexBuilder ivertexbuilder = bufferIn
						.getBuffer(RenderType.getEnergySwirl(new ResourceLocation("laboratory:textures/white_steve.png"), 44, 44));
				this.getEntityModel().render(matrixStackIn, ivertexbuilder, 15728640, OverlayTexture.NO_OVERLAY, (float) Math.random(),
						(float) Math.random() * 3, (float) Math.random() * 3, (float) Math.random() * 3);
				matrixStackIn.translate(0, 0, 0);
			}
			matrixStackIn.scale(1, 1, 1);
		}
	}
}
