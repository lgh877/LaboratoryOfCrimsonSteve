
package net.mcreator.laboratory.itemgroup;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemGroup;

import net.mcreator.laboratory.item.MrDummySpawningToolItem;
import net.mcreator.laboratory.LaboratoryModElements;

@LaboratoryModElements.ModElement.Tag
public class MobsOfLaboratoryItemGroup extends LaboratoryModElements.ModElement {
	public MobsOfLaboratoryItemGroup(LaboratoryModElements instance) {
		super(instance, 69);
	}

	@Override
	public void initElements() {
		tab = new ItemGroup("tabmobs_of_laboratory") {
			@OnlyIn(Dist.CLIENT)
			@Override
			public ItemStack createIcon() {
				return new ItemStack(MrDummySpawningToolItem.block);
			}

			@OnlyIn(Dist.CLIENT)
			public boolean hasSearchBar() {
				return false;
			}
		};
	}

	public static ItemGroup tab;
}
