
package net.mcreator.laboratory.itemgroup;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemGroup;

import net.mcreator.laboratory.item.SoulSmokeProjectileItem;
import net.mcreator.laboratory.LaboratoryModElements;

@LaboratoryModElements.ModElement.Tag
public class ItemsOfLaboratoryItemGroup extends LaboratoryModElements.ModElement {
	public ItemsOfLaboratoryItemGroup(LaboratoryModElements instance) {
		super(instance, 70);
	}

	@Override
	public void initElements() {
		tab = new ItemGroup("tabitems_of_laboratory") {
			@OnlyIn(Dist.CLIENT)
			@Override
			public ItemStack createIcon() {
				return new ItemStack(SoulSmokeProjectileItem.block);
			}

			@OnlyIn(Dist.CLIENT)
			public boolean hasSearchBar() {
				return false;
			}
		};
	}

	public static ItemGroup tab;
}
