package com.anotherstar.slashblade.common.item;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.flammpfeil.slashblade.ItemRendererBaseWeapon;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

@Optional.Interface(iface = "com.anotherstar.common.item.tool.ILoli", modid = "AnotherStar")
public class ItemLoader {

	public static Item loliSlashBlade;

	public static void init() {
		loliSlashBlade = new ItemLoliSlashBlade();
		GameRegistry.registerItem(loliSlashBlade, "loliSlashBlade");
	}

	@SideOnly(Side.CLIENT)
	public static void renderInit() {
		MinecraftForgeClient.registerItemRenderer(loliSlashBlade, new ItemRendererBaseWeapon());
	}

}
