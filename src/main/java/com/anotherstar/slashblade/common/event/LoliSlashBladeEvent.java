package com.anotherstar.slashblade.common.event;

import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.slashblade.common.item.ItemLoliSlashBlade;
import com.anotherstar.util.LoliPickaxeUtil;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LoliSlashBladeEvent {

	@SubscribeEvent
	@Optional.Method(modid = LoliPickaxe.MODID)
	public void onAttack(AttackEntityEvent event) {
		if (!event.getEntityPlayer().world.isRemote) {
			ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
			if (stack != null) {
				Item item = stack.getItem();
				if (item instanceof ItemLoliSlashBlade) {
					if (event.getTarget() instanceof EntityPlayer) {
						LoliPickaxeUtil.killPlayer((EntityPlayer) event.getTarget(), event.getEntityPlayer());
					} else if (event.getTarget() instanceof EntityLivingBase) {
						LoliPickaxeUtil.killEntityLiving((EntityLivingBase) event.getTarget(), event.getEntityPlayer());
					} else if (ConfigLoader.getBoolean(stack, "loliPickaxeValidToAllEntity")) {
						LoliPickaxeUtil.killEntity(event.getTarget());
					}
					if (ConfigLoader.getBoolean(stack, "loliPickaxeKillFacing")) {
						LoliPickaxeUtil.killFacing(event.getEntityPlayer());
					}
				}
			}
		}
	}

}
