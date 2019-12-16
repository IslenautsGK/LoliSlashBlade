package com.anotherstar.slashblade.common.event;

import com.anotherstar.slashblade.LoliSlashBlade;

import mods.flammpfeil.slashblade.entity.EntityBladeStand;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SlashBladeColorEvent {

	@SubscribeEvent
	public void onAttack(AttackEntityEvent event) {
		if (!event.getEntityPlayer().world.isRemote) {
			ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
			if (stack != null) {
				Item item = stack.getItem();
				if (event.getTarget() instanceof EntityBladeStand) {
					EntityBladeStand stand = (EntityBladeStand) event.getTarget();
					if (stand.hasBlade()) {
						ItemStack blade = stand.getBlade();
						int level = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(48), blade);
						if (level > 0) {
							int color = 0;
							boolean changeColor = false;
							if (item instanceof ItemDye) {
								color = ItemDye.DYE_COLORS[stack.getItemDamage()];
								changeColor = true;
							} else if (item instanceof ItemNameTag) {
								String name = stack.getDisplayName();
								try {
									if (name.startsWith("0x") || name.startsWith("0X")) {
										color = Integer.parseInt(name.substring(2), 16);
										changeColor = true;
									} else {
										try {
											color = Integer.parseInt(name);
											changeColor = true;
										} catch (NumberFormatException e) {
											color = Integer.parseInt(name, 16);
											changeColor = true;
										}
									}
								} catch (NumberFormatException e) {
								}
							}
							if (changeColor) {
								color &= 0x00FFFFFF;
								NBTTagCompound bladeTag = ItemSlashBlade.getItemTagCompound(blade);
								if (ItemSlashBlade.SummonedSwordColor.exists(bladeTag)
										&& ItemSlashBlade.SummonedSwordColor.get(bladeTag) == color) {
									ItemSlashBlade.SummonedSwordColor.remove(bladeTag);
								} else {
									ItemSlashBlade.SummonedSwordColor.set(bladeTag, color);
								}
								event.getEntityPlayer().onCriticalHit(stand);
								stack.shrink(1);
								if (stack.getCount() <= 0) {
									event.getEntityPlayer().setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
								}
							}
						}
					}
				}
			}
		}
	}

}
