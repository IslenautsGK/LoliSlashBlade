package com.anotherstar.slashblade.common;

import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.slashblade.LoliSlashBlade;
import com.anotherstar.slashblade.common.command.LoliSlashBladeCommand;
import com.anotherstar.slashblade.common.entity.EntityLoliBlisteringSwords;
import com.anotherstar.slashblade.common.entity.EntityLoliHeavyRainSwords;
import com.anotherstar.slashblade.common.entity.EntityLoliSA;
import com.anotherstar.slashblade.common.entity.EntityLoliSpinningSword;
import com.anotherstar.slashblade.common.entity.EntityLoliSpiralSwords;
import com.anotherstar.slashblade.common.entity.EntityLoliStormSwords;
import com.anotherstar.slashblade.common.entity.EntityLoliSummonedBlade;
import com.anotherstar.slashblade.common.entity.EntityLoliSummonedSwordBase;
import com.anotherstar.slashblade.common.entity.EntityLoliSuperSA;
import com.anotherstar.slashblade.common.event.LoliLoadEvent;
import com.anotherstar.slashblade.common.event.LoliSlashBladeEvent;
import com.anotherstar.slashblade.common.event.SlashBladeColorEvent;
import com.anotherstar.slashblade.common.item.ItemLoader;

import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		if (Loader.isModLoaded(LoliPickaxe.MODID)) {
			preInitLoliPickaxe(event);
		}
	}

	@Optional.Method(modid = LoliPickaxe.MODID)
	private void preInitLoliPickaxe(FMLPreInitializationEvent event) {
		SlashBlade.InitEventBus.register(new LoliLoadEvent());
		MinecraftForge.EVENT_BUS.register(new ItemLoader());
	}

	public void init(FMLInitializationEvent event) {
		if (Loader.isModLoaded(LoliPickaxe.MODID)) {
			initLoliPickaxe(event);
		}
		MinecraftForge.EVENT_BUS.register(new SlashBladeColorEvent());
	}

	@Optional.Method(modid = LoliPickaxe.MODID)
	private void initLoliPickaxe(FMLInitializationEvent event) {
		int entityId = 1;
		EntityRegistry.registerModEntity(new ResourceLocation(LoliSlashBlade.MODID, "LoliSA"), EntityLoliSA.class,
				"LoliSA", entityId++, LoliSlashBlade.INSTANCE, 250, 200, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LoliSlashBlade.MODID, "LoliSuperSA"),
				EntityLoliSuperSA.class, "LoliSuperSA", entityId++, LoliSlashBlade.INSTANCE, 250, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LoliSlashBlade.MODID, "LoliSpinningSword"),
				EntityLoliSpinningSword.class, "LoliSpinningSword", entityId++, LoliSlashBlade.INSTANCE, 250, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LoliSlashBlade.MODID, "LoliSummonedSwordBase"),
				EntityLoliSummonedSwordBase.class, "LoliSummonedSwordBase", entityId++, LoliSlashBlade.INSTANCE, 250,
				10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LoliSlashBlade.MODID, "LoliSummonedBlade"),
				EntityLoliSummonedBlade.class, "LoliSummonedBlade", entityId++, LoliSlashBlade.INSTANCE, 250, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LoliSlashBlade.MODID, "LoliBlisteringSwords"),
				EntityLoliBlisteringSwords.class, "LoliBlisteringSwords", entityId++, LoliSlashBlade.INSTANCE, 250, 200,
				true);
		EntityRegistry.registerModEntity(new ResourceLocation(LoliSlashBlade.MODID, "LoliSpiralSwords"),
				EntityLoliSpiralSwords.class, "LoliSpiralSwords", entityId++, LoliSlashBlade.INSTANCE, 250, 200, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LoliSlashBlade.MODID, "LoliStormSwords"),
				EntityLoliStormSwords.class, "LoliStormSwords", entityId++, LoliSlashBlade.INSTANCE, 250, 200, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LoliSlashBlade.MODID, "LoliHeavyRainSwords"),
				EntityLoliHeavyRainSwords.class, "LoliHeavyRainSwords", entityId++, LoliSlashBlade.INSTANCE, 250, 200,
				true);
		MinecraftForge.EVENT_BUS.register(new LoliSlashBladeEvent());
	}

	public void postInit(FMLPostInitializationEvent event) {
	}

	public void onServerStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new LoliSlashBladeCommand());
	}

}
