package com.anotherstar.slashblade.client;

import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.slashblade.client.event.LoliKeyEvent;
import com.anotherstar.slashblade.client.key.KeyLoader;
import com.anotherstar.slashblade.common.CommonProxy;
import com.anotherstar.slashblade.common.entity.EntityLoliBlisteringSwords;
import com.anotherstar.slashblade.common.entity.EntityLoliHeavyRainSwords;
import com.anotherstar.slashblade.common.entity.EntityLoliSA;
import com.anotherstar.slashblade.common.entity.EntityLoliSpinningSword;
import com.anotherstar.slashblade.common.entity.EntityLoliSpiralSwords;
import com.anotherstar.slashblade.common.entity.EntityLoliStormSwords;
import com.anotherstar.slashblade.common.entity.EntityLoliSummonedBlade;
import com.anotherstar.slashblade.common.entity.EntityLoliSummonedSwordBase;
import com.anotherstar.slashblade.common.entity.EntityLoliSuperSA;

import mods.flammpfeil.slashblade.client.renderer.entity.InvisibleRender;
import mods.flammpfeil.slashblade.client.renderer.entity.RenderPhantomSwordBase;
import mods.flammpfeil.slashblade.client.renderer.entity.RenderSlashDimension;
import mods.flammpfeil.slashblade.client.renderer.entity.RenderSpinningSword;
import mods.flammpfeil.slashblade.client.renderer.entity.RenderSummonedBlade;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		if (Loader.isModLoaded(LoliPickaxe.MODID)) {
			initLoliPickaxe(event);
		}
	}

	@Optional.Method(modid = LoliPickaxe.MODID)
	private void initLoliPickaxe(FMLInitializationEvent event) {
		KeyLoader.init();
		MinecraftForge.EVENT_BUS.register(new LoliKeyEvent());
		RenderingRegistry.registerEntityRenderingHandler(EntityLoliSA.class,
				manager -> new RenderSlashDimension(manager));
		RenderingRegistry.registerEntityRenderingHandler(EntityLoliSuperSA.class,
				manager -> new InvisibleRender(manager));
		RenderingRegistry.registerEntityRenderingHandler(EntityLoliSpinningSword.class,
				manager -> new RenderSpinningSword(manager));
		RenderingRegistry.registerEntityRenderingHandler(EntityLoliSummonedSwordBase.class,
				manager -> new RenderPhantomSwordBase(manager));
		RenderingRegistry.registerEntityRenderingHandler(EntityLoliSummonedBlade.class,
				manager -> new RenderSummonedBlade(manager));
		RenderingRegistry.registerEntityRenderingHandler(EntityLoliBlisteringSwords.class,
				manager -> new RenderPhantomSwordBase(manager));
		RenderingRegistry.registerEntityRenderingHandler(EntityLoliSpiralSwords.class,
				manager -> new RenderPhantomSwordBase(manager));
		RenderingRegistry.registerEntityRenderingHandler(EntityLoliStormSwords.class,
				manager -> new RenderPhantomSwordBase(manager));
		RenderingRegistry.registerEntityRenderingHandler(EntityLoliHeavyRainSwords.class,
				manager -> new RenderPhantomSwordBase(manager));
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
