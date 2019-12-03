package com.anotherstar.slashblade;

import com.anotherstar.slashblade.common.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = LoliSlashBlade.MODID, name = LoliSlashBlade.NAME, version = LoliSlashBlade.VERSION, acceptedMinecraftVersions = "1.7.10", dependencies = "required-after:flammpfeil.slashblade")
public class LoliSlashBlade {

	public static final String MODID = "LoliSlashBlade";
	public static final String NAME = "LoliSlashBlade Mod";
	public static final String VERSION = "1.0.2";

	@SidedProxy(clientSide = "com.anotherstar.slashblade.client.ClientProxy", serverSide = "com.anotherstar.slashblade.common.CommonProxy")
	public static CommonProxy proxy;

	@Instance(LoliSlashBlade.MODID)
	public static LoliSlashBlade instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		proxy.onServerStarting(event);
	}

}
