package net.mcreator.laboratory.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;

import net.mcreator.laboratory.entity.IMMORTALEntity;

import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class IMMORTALRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(IMMORTALEntity.entity, renderManager -> {
				BipedRenderer customRender = new BipedRenderer(renderManager, new BipedModel(0), 0.5f) {
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("laboratory:textures/original_steve.png");
					}

					@Override
					public void render(Entity e, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
							int packedLightIn) {
						LivingEntity entityL = (LivingEntity) e;
						if (Math.random() < 0.2)
							((BipedModel) this.getEntityModel()).isSneak = !((BipedModel) this.getEntityModel()).isSneak;
						else if (Math.random() < 0.2) {
							boolean flag1 = ((BipedModel) this.getEntityModel()).leftArmPose == BipedModel.ArmPose.BLOCK ? true : false;
							if (flag1)
								((BipedModel) this.getEntityModel()).leftArmPose = BipedModel.ArmPose.THROW_SPEAR;
							else
								((BipedModel) this.getEntityModel()).leftArmPose = BipedModel.ArmPose.BLOCK;
						} else if (Math.random() < 0.2) {
							boolean flag2 = ((BipedModel) this.getEntityModel()).rightArmPose == BipedModel.ArmPose.BLOCK ? true : false;
							if (flag2)
								((BipedModel) this.getEntityModel()).rightArmPose = BipedModel.ArmPose.THROW_SPEAR;
							else
								((BipedModel) this.getEntityModel()).rightArmPose = BipedModel.ArmPose.BLOCK;
						}
						super.render(e, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
					}
				};
				customRender.addLayer(new BipedArmorLayer(customRender, new BipedModel(0.5f), new BipedModel(1)));
				return customRender;
			});
		}
	}
}
