package com.anotherstar.slashblade.common.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mods.flammpfeil.slashblade.ItemSlashBlade;
import mods.flammpfeil.slashblade.entity.EntityBladeStand;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

public class SlashBladeColorEvent {

	@SubscribeEvent
	public void onAttack(AttackEntityEvent event) {
		if (!event.entityPlayer.worldObj.isRemote) {
			ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
			if (stack != null) {
				Item item = stack.getItem();
				if (event.target instanceof EntityBladeStand) {
					EntityBladeStand stand = (EntityBladeStand) event.target;
					if (stand.hasBlade()) {
						ItemStack blade = stand.getBlade();
						int level = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, blade);
						if (level > 0) {
							int color = 0;
							boolean changeColor = false;
							if (item instanceof ItemDye) {
								color = ItemDye.field_150922_c[stack.getItemDamage()];
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
								event.entityPlayer.onCriticalHit(stand);
								stack.stackSize--;
								if (stack.stackSize <= 0) {
									event.entityPlayer.destroyCurrentEquippedItem();
									event.entityPlayer.setCurrentItemOrArmor(0, (ItemStack) null);
								}
							}
						}
					}
				}
			}
		}
	}

}
