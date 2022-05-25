
package net.mcreator.laboratory.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.model.SnowManModel;
import net.minecraft.client.renderer.entity.model.CreeperModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.MobRenderer;

import net.mcreator.laboratory.entity.BeingsEntity;

@OnlyIn(Dist.CLIENT)
public class BeingsRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(BeingsEntity.entity, renderManager -> {
				double d0 = Math.random();
				if (d0 < 0.33)
					return new MobRenderer(renderManager, new CreeperModel(), 0.5f) {
						@Override
						public ResourceLocation getEntityTexture(Entity entity) {
							return new ResourceLocation("laboratory:textures/original_steve.png");
						}
					};
				else if (d0 < 0.66)
					return new MobRenderer(renderManager, new BipedModel(0), 0.5f) {
						@Override
						public ResourceLocation getEntityTexture(Entity entity) {
							return new ResourceLocation("laboratory:textures/original_steve.png");
						}
					};
				else
					return new MobRenderer(renderManager, new SnowManModel(), 0.5f) {
						@Override
						public ResourceLocation getEntityTexture(Entity entity) {
							return new ResourceLocation("laboratory:textures/newer_original_steve.png");
						}
					};
			});
		}
	}
}
