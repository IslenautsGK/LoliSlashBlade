package com.anotherstar.slashblade.common.item;

import com.anotherstar.slashblade.LoliSlashBlade;

import mods.flammpfeil.slashblade.tileentity.DummyTileEntity;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLoader {

	public final static Item loliSlashBlade = new ItemLoliSlashBlade();

	@SubscribeEvent
	public void registerItem(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(loliSlashBlade.setRegistryName(LoliSlashBlade.MODID, "loli_slash_blade"));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerModel(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(loliSlashBlade, 0,
				new ModelResourceLocation("flammpfeil.slashblade:model/named/blade.obj"));
		ForgeHooksClient.registerTESRItemStack(loliSlashBlade, 0, DummyTileEntity.class);
	}

}
