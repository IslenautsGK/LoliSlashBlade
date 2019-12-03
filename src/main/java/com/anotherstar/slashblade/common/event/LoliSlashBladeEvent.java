package com.anotherstar.slashblade.common.event;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.item.tool.ILoli;
import com.anotherstar.util.LoliPickaxeUtil;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

@Optional.InterfaceList(value = {
		@Interface(iface = "com.anotherstar.common.config.ConfigLoader", modid = "AnotherStar"),
		@Interface(iface = "com.anotherstar.common.item.tool.ILoli", modid = "AnotherStar"),
		@Interface(iface = "com.anotherstar.util.LoliPickaxeUtil", modid = "AnotherStar") })
public class LoliSlashBladeEvent {

	@SubscribeEvent
	public void onAttack(AttackEntityEvent event) {
		if (!event.entityPlayer.worldObj.isRemote) {
			ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
			if (stack != null) {
				Item item = stack.getItem();
				if (item instanceof ILoli) {
					if (event.target instanceof EntityPlayer) {
						LoliPickaxeUtil.killPlayer((EntityPlayer) event.target, event.entityPlayer);
					} else if (event.target instanceof EntityLivingBase) {
						LoliPickaxeUtil.killEntityLiving((EntityLivingBase) event.target, event.entityPlayer);
					} else if (ConfigLoader.loliPickaxeValidToAllEntity) {
						LoliPickaxeUtil.killEntity(event.target);
					}
				}
			}
		}
	}

}
