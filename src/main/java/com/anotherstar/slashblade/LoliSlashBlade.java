package com.anotherstar.slashblade;

import com.anotherstar.slashblade.common.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = LoliSlashBlade.MODID, name = LoliSlashBlade.NAME, version = LoliSlashBlade.VERSION, useMetadata = true, dependencies = "required-after:flammpfeil.slashblade")
public enum LoliSlashBlade {

	INSTANCE;

	public static final String MODID = "lolislashblade";
	public static final String NAME = "LoliSlashBlade";
	public static final String VERSION = "1.0.3";

	@SidedProxy(serverSide = "com.anotherstar.slashblade.common.CommonProxy", clientSide = "com.anotherstar.slashblade.client.ClientProxy")
	public static CommonProxy proxy;

	@Mod.InstanceFactory
	public static LoliSlashBlade getInstance() {
		return INSTANCE;
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		proxy.onServerStarting(event);
	}

}
