package com.anotherstar.slashblade.client;

import com.anotherstar.slashblade.common.CommonProxy;
import com.anotherstar.slashblade.common.entity.EntityLoliBlisteringSwords;
import com.anotherstar.slashblade.common.entity.EntityLoliHeavyRainSwords;
import com.anotherstar.slashblade.common.entity.EntityLoliPhantomSwordBase;
import com.anotherstar.slashblade.common.entity.EntityLoliSA;
import com.anotherstar.slashblade.common.entity.EntityLoliSpiralSwords;
import com.anotherstar.slashblade.common.entity.EntityLoliStormSwords;
import com.anotherstar.slashblade.common.entity.EntityLoliSummonedBlade;
import com.anotherstar.slashblade.common.entity.EntityLoliSuperSA;
import com.anotherstar.slashblade.common.item.ItemLoader;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import mods.flammpfeil.slashblade.RenderDrive;
import mods.flammpfeil.slashblade.client.renderer.RenderPhantomSwordBase;
import mods.flammpfeil.slashblade.client.renderer.entity.RenderSlashDimension;
import mods.flammpfeil.slashblade.client.renderer.entity.RenderSummonedBlade;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		if (Loader.isModLoaded("AnotherStar")) {
			preInitAnotherStar(event);
		}
	}

	@Optional.Method(modid = "AnotherStar")
	private void preInitAnotherStar(FMLPreInitializationEvent event) {
		ItemLoader.renderInit();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		if (Loader.isModLoaded("AnotherStar")) {
			initAnotherStar(event);
		}
	}

	@Optional.Method(modid = "AnotherStar")
	private void initAnotherStar(FMLInitializationEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityLoliSA.class, new RenderSlashDimension());
		RenderingRegistry.registerEntityRenderingHandler(EntityLoliSuperSA.class, new RenderDrive());
		RenderingRegistry.registerEntityRenderingHandler(EntityLoliPhantomSwordBase.class,
				new RenderPhantomSwordBase());
		RenderingRegistry.registerEntityRenderingHandler(EntityLoliSummonedBlade.class, new RenderSummonedBlade());
		RenderingRegistry.registerEntityRenderingHandler(EntityLoliBlisteringSwords.class,
				new RenderPhantomSwordBase());
		RenderingRegistry.registerEntityRenderingHandler(EntityLoliSpiralSwords.class, new RenderPhantomSwordBase());
		RenderingRegistry.registerEntityRenderingHandler(EntityLoliStormSwords.class, new RenderPhantomSwordBase());
		RenderingRegistry.registerEntityRenderingHandler(EntityLoliHeavyRainSwords.class, new RenderPhantomSwordBase());
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}

	@Override
	public void onServerStarting(FMLServerStartingEvent event) {
		super.onServerStarting(event);
	}

}
