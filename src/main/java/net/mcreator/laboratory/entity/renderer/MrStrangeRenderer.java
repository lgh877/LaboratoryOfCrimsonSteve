package net.mcreator.laboratory.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;

import net.mcreator.laboratory.entity.MrStrangeEntity;

import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class MrStrangeRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(MrStrangeEntity.entity, renderManager -> {
				BipedRenderer customRender = new BipedRenderer(renderManager, new BipedModel(0), 0.5f) {
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("laboratory:textures/mr_strange.png");
					}

					@Override
					public void render(Entity e, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
							int packedLightIn) {
						MrStrangeEntity.CustomEntity entitylivingbaseIn = (MrStrangeEntity.CustomEntity) e;
						float f = 0.999F;
						matrixStackIn.scale(0.999F, 0.999F, 0.999F);
						matrixStackIn.translate(0.0D, (double) 0.001F, 0.0D);
						float f1 = (float) entitylivingbaseIn.getSlimeSize();
						float f2 = MathHelper.lerp(partialTicks, entitylivingbaseIn.prevSquishFactor, entitylivingbaseIn.squishFactor) / (0.4F);
						float f3 = 1.0F / (f2 + 1.0F);
						matrixStackIn.scale(f3 * f1 * 0.3f, 1.0F / f3 * f1 * 0.3f, f3 * f1 * 0.3f);
						this.shadowSize = 0.09F * (float) entitylivingbaseIn.getSlimeSize();
						super.render(entitylivingbaseIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
					}
				};
				customRender.addLayer(new BipedArmorLayer(customRender, new BipedModel(0.5f), new BipedModel(1)));
				return customRender;
			});
		}
	}
}
