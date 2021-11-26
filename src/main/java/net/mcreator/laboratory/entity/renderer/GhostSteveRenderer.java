package net.mcreator.laboratory.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.IRenderTypeBuffer;

import net.mcreator.laboratory.entity.GhostSteveEntity;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class GhostSteveRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(GhostSteveEntity.entity, renderManager -> {
				BipedRenderer customRender = new BipedRenderer(renderManager, new BipedModel(0), 0.5f) {
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("laboratory:textures/nothing.png");
					}
				};
				customRender.addLayer(new BipedArmorLayer(customRender, new BipedModel(0.5f), new BipedModel(1)));
				customRender.addLayer(new GhostSteveBoneLayer<>(customRender));
				customRender.addLayer(new GhostSteveSoulLayer<>(customRender));
				return customRender;
			});
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static class GhostSteveSoulLayer<T extends Entity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
		public GhostSteveSoulLayer(IEntityRenderer<T, M> er) {
			super(er);
		}

		public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing,
				float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			LivingEntity entity = (LivingEntity) entitylivingbaseIn;
			this.getEntityModel().setRotationAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw / 2, headPitch / 2);
			IVertexBuilder ivertexbuilder = bufferIn
					.getBuffer(RenderType.getEntityTranslucent(new ResourceLocation("laboratory:textures/original_steve_half_translucent.png")));
			this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, LivingRenderer.getPackedOverlay(entity, 0.0F), 1, 1, 1, 1);
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static class GhostSteveBoneLayer<T extends Entity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
		private final EntityModel<T> boneModel = new SkeletonModel(0, false);
		public GhostSteveBoneLayer(IEntityRenderer<T, M> er) {
			super(er);
		}

		public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing,
				float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			LivingEntity entity = (LivingEntity) entitylivingbaseIn;
			matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-netHeadYaw / 2));
			matrixStackIn.rotate(Vector3f.XP.rotationDegrees(headPitch / 2));
			this.getEntityModel().copyModelAttributesTo(this.boneModel);
			boneModel.setRotationAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw / 2, headPitch / 2);
			IVertexBuilder ivertexbuilder = bufferIn
					.getBuffer(RenderType.getEntityCutoutNoCull(new ResourceLocation("textures/entity/skeleton/skeleton.png")));
			boneModel.render(matrixStackIn, ivertexbuilder, packedLightIn, LivingRenderer.getPackedOverlay(entity, 0.0F), 1f, 1f, 1, 1);
		}
	}
}
