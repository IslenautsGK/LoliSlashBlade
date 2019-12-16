package com.anotherstar.slashblade.common.event;

import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.slashblade.common.item.ItemLoader;
import com.anotherstar.slashblade.common.sa.LoliSword;

import mods.flammpfeil.slashblade.ItemSlashBladeNamed;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.named.event.LoadEvent;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Optional.InterfaceList(value = {
		@Interface(iface = "com.anotherstar.common.config.ConfigLoader", modid = LoliPickaxe.MODID),
		@Interface(iface = "com.anotherstar.common.item.tool.ILoli", modid = LoliPickaxe.MODID),
		@Interface(iface = "com.anotherstar.util.LoliPickaxeUtil", modid = LoliPickaxe.MODID) })
public class LoliLoadEvent {

	@SubscribeEvent
	public void init(LoadEvent.InitEvent event) {
		int i = 40;
		while (ItemSlashBlade.specialAttacks.containsKey(i)) {
			i++;
		}
		ItemSlashBlade.specialAttacks.put(i, new LoliSword());
		String name = "flammpfeil.slashblade.named.loliblade";
		ItemStack loliBlade = new ItemStack(ItemLoader.loliSlashBlade, 1, 0);
		NBTTagCompound tag = new NBTTagCompound();
		ItemSlashBladeNamed.CurrentItemName.set(tag, name);
		ItemSlashBladeNamed.CustomMaxDamage.set(tag, Integer.valueOf(100));
		ItemSlashBladeNamed.IsDefaultBewitched.set(tag, true);
		ItemSlashBlade.setBaseAttackModifier(tag, 100.0F);
		ItemSlashBlade.SpecialAttackType.set(tag, i);
		ItemSlashBlade.TextureName.set(tag, "named/loliblade/texture");
		ItemSlashBlade.ModelName.set(tag, "named/loliblade/model");
		ItemSlashBlade.RepairCount.set(tag, 10000);
		ItemSlashBlade.KillCount.set(tag, 100000);
		ItemSlashBlade.ProudSoul.set(tag, 1000000);
		tag.setBoolean("Unbreakable", true);
		loliBlade.setTagCompound(tag);
		loliBlade.addEnchantment(Enchantments.POWER, 5);
		SlashBlade.registerCustomItemStack(name, loliBlade);
		if (false) {
			ItemSlashBladeNamed.NamedBlades.add(name);
		}
	}

}
