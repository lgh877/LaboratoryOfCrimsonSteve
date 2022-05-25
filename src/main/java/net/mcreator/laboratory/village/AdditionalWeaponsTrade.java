
package net.mcreator.laboratory.village;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.common.BasicTrade;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.block.Blocks;

import net.mcreator.laboratory.item.SoulSmokeProjectileItem;
import net.mcreator.laboratory.item.SoulEnergyLauncherItem;

import java.util.List;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AdditionalWeaponsTrade {
	@SubscribeEvent
	public static void registerTrades(VillagerTradesEvent event) {
		Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();
		if (event.getType() == VillagerProfession.WEAPONSMITH) {
			trades.get(5).add(new BasicTrade(new ItemStack(Blocks.EMERALD_BLOCK, (int) (10)), new ItemStack(Blocks.EMERALD_BLOCK, (int) (10)),
					new ItemStack(SoulSmokeProjectileItem.block), 10, 5, 0.05f));
			trades.get(1).add(new BasicTrade(new ItemStack(Blocks.EMERALD_BLOCK, (int) (10)), new ItemStack(Blocks.EMERALD_BLOCK, (int) (10)),
					new ItemStack(SoulEnergyLauncherItem.block), 10, 5, 0.05f));
		}
	}
}
