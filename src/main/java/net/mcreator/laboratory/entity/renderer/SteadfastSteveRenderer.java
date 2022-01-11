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
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.BipedRenderer;

import net.mcreator.laboratory.entity.SteadfastSteveEntity;

import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class SteadfastSteveRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(SteadfastSteveEntity.entity, renderManager -> {
				BipedRenderer customRender = new BipedRenderer(renderManager, new BipedModel(0), 0.5f) {
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("laboratory:textures/original_steve.png");
					}

					protected void applyRotations(LivingEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw,
							float partialTicks) {
						matrixStackIn.rotate(Vector3f.XP.rotationDegrees(MathHelper.clamp((float) entityLiving.getMotion().y, -2, 0) * 90));
						matrixStackIn.rotate(Vector3f.YP.rotationDegrees(ageInTicks * 70));
						matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(entityLiving.deathTime * 9));
					}
				};
				customRender.addLayer(new BipedArmorLayer(customRender, new BipedModel(0.5f), new BipedModel(1)));
				return customRender;
			});
		}
	}
}
