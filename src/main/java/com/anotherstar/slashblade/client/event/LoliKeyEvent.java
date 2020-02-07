package com.anotherstar.slashblade.client.event;

import com.anotherstar.slashblade.client.key.KeyLoader;
import com.anotherstar.slashblade.common.item.ItemLoliSlashBlade;
import com.anotherstar.slashblade.network.LoliSlashBladeChangeRangePacket;
import com.anotherstar.slashblade.network.LoliSlashBladeChangeSlashBladePacket;
import com.anotherstar.slashblade.network.NetworkHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class LoliKeyEvent {

	@SubscribeEvent
	public void onKeyPressed(KeyInputEvent event) {
		if (KeyLoader.LOLI_SLASH_BLADE_RANGE_CONFIG.isPressed()) {
			ItemStack stack = Minecraft.getMinecraft().player.getHeldItemMainhand();
			if (!stack.isEmpty() && stack.getItem() instanceof ItemLoliSlashBlade) {
				NetworkHandler.INSTANCE.sendMessageToServer(new LoliSlashBladeChangeRangePacket());
			}
		}
		if (KeyLoader.LOLI_SLASH_BLADE_SLASH_BLADE.isPressed()) {
			ItemStack stack = Minecraft.getMinecraft().player.getHeldItemMainhand();
			if (!stack.isEmpty() && stack.getItem() instanceof ItemLoliSlashBlade) {
				NetworkHandler.INSTANCE.sendMessageToServer(new LoliSlashBladeChangeSlashBladePacket());
			}
		}
	}

}
