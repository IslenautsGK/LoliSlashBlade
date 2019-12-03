package com.anotherstar.slashblade.common;

import com.anotherstar.slashblade.LoliSlashBlade;
import com.anotherstar.slashblade.common.entity.EntityLoliBlisteringSwords;
import com.anotherstar.slashblade.common.entity.EntityLoliHeavyRainSwords;
import com.anotherstar.slashblade.common.entity.EntityLoliPhantomSwordBase;
import com.anotherstar.slashblade.common.entity.EntityLoliSA;
import com.anotherstar.slashblade.common.entity.EntityLoliSpiralSwords;
import com.anotherstar.slashblade.common.entity.EntityLoliStormSwords;
import com.anotherstar.slashblade.common.entity.EntityLoliSummonedBlade;
import com.anotherstar.slashblade.common.entity.EntityLoliSuperSA;
import com.anotherstar.slashblade.common.event.LoliLoadEvent;
import com.anotherstar.slashblade.common.event.LoliSlashBladeEvent;
import com.anotherstar.slashblade.common.event.SlashBladeColorEvent;
import com.anotherstar.slashblade.common.item.ItemLoader;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		if (Loader.isModLoaded("AnotherStar")) {
			preInitAnotherStar(event);
		}
	}

	@Optional.Method(modid = "AnotherStar")
	private void preInitAnotherStar(FMLPreInitializationEvent event) {
		SlashBlade.InitEventBus.register(new LoliLoadEvent());
		ItemLoader.init();
	}

	public void init(FMLInitializationEvent event) {
		if (Loader.isModLoaded("AnotherStar")) {
			initAnotherStar(event);
		}
		MinecraftForge.EVENT_BUS.register(new SlashBladeColorEvent());
	}

	@Optional.Method(modid = "AnotherStar")
	private void initAnotherStar(FMLInitializationEvent event) {
		EntityRegistry.registerModEntity(EntityLoliSA.class, "LoliSA", 1, LoliSlashBlade.instance, 250, 200, true);
		EntityRegistry.registerModEntity(EntityLoliSuperSA.class, "LoliSuperSA", 2, LoliSlashBlade.instance, 250, 10,
				true);
		EntityRegistry.registerModEntity(EntityLoliPhantomSwordBase.class, "LoliPhantomSwordBase", 3,
				LoliSlashBlade.instance, 250, 1, true);
		EntityRegistry.registerModEntity(EntityLoliSummonedBlade.class, "LoliSummonedBlade", 4, LoliSlashBlade.instance,
				250, 10, true);
		EntityRegistry.registerModEntity(EntityLoliBlisteringSwords.class, "LoliBlisteringSwords", 5,
				LoliSlashBlade.instance, 250, 200, true);
		EntityRegistry.registerModEntity(EntityLoliSpiralSwords.class, "LoliSpiralSwords", 6, LoliSlashBlade.instance,
				250, 200, true);
		EntityRegistry.registerModEntity(EntityLoliStormSwords.class, "LoliStormSwords", 7, LoliSlashBlade.instance,
				250, 200, true);
		EntityRegistry.registerModEntity(EntityLoliHeavyRainSwords.class, "LoliHeavyRainSwords", 8,
				LoliSlashBlade.instance, 250, 200, true);
		MinecraftForge.EVENT_BUS.register(new LoliSlashBladeEvent());
	}

	public void postInit(FMLPostInitializationEvent event) {
	}

	public void onServerStarting(FMLServerStartingEvent event) {
	}

}
