package com.anotherstar.slashblade.client.key;

import org.lwjgl.input.Keyboard;

import com.anotherstar.slashblade.LoliSlashBlade;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyLoader {

	public static final KeyBinding LOLI_SLASH_BLADE_RANGE_CONFIG = new KeyBinding(
			"key." + LoliSlashBlade.MODID + ".range", KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_K,
			"key.category." + LoliSlashBlade.MODID);
	public static final KeyBinding LOLI_SLASH_BLADE_SLASH_BLADE = new KeyBinding(
			"key." + LoliSlashBlade.MODID + ".slashBlade", KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_I,
			"key.category." + LoliSlashBlade.MODID);

	public static void init() {
		ClientRegistry.registerKeyBinding(LOLI_SLASH_BLADE_RANGE_CONFIG);
		ClientRegistry.registerKeyBinding(LOLI_SLASH_BLADE_SLASH_BLADE);
	}

}
