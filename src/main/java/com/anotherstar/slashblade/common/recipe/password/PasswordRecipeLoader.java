package com.anotherstar.slashblade.common.recipe.password;

import org.apache.commons.codec.digest.DigestUtils;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.item.tool.ILoli;
import com.anotherstar.common.registry.recipe.IPasswordRecipe;
import com.anotherstar.slashblade.LoliSlashBlade;

import mods.flammpfeil.slashblade.ItemSlashBladeNamed;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class PasswordRecipeLoader extends IForgeRegistryEntry.Impl<IPasswordRecipe> implements IPasswordRecipe {

	@SubscribeEvent
	public void registerPasswordRecipe(RegistryEvent.Register<IPasswordRecipe> event) {
		event.getRegistry().register(this.setRegistryName(LoliSlashBlade.MODID, "loli_slash_blade"));
	}

	@Override
	public ItemStack getResult(InventoryCrafting inv, EntityPlayer player, String password) {
		if (inv.getSizeInventory() != 9) {
			return ItemStack.EMPTY;
		}
		for (int i = 0; i < 9; i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.isEmpty()) {
				return ItemStack.EMPTY;
			}
			switch (i) {
			case 4:
				if (!(stack.getItem() instanceof ILoli)) {
					return ItemStack.EMPTY;
				}
				break;
			case 0:
			case 2:
			case 3:
			case 5:
			case 6:
			case 8:
				if (stack.getItem() != SlashBlade.bladeWood || !stack.hasTagCompound()
						|| ItemSlashBlade.KillCount.get(stack.getTagCompound()) < 100000
						|| ItemSlashBlade.ProudSoul.get(stack.getTagCompound()) < 100000
						|| ItemSlashBlade.RepairCount.get(stack.getTagCompound()) < 5000) {
					return ItemStack.EMPTY;
				}
				break;
			case 1:
				if (stack.getItem() != SlashBlade.bladeNamed || !stack.hasTagCompound()
						|| ItemSlashBlade.KillCount.get(stack.getTagCompound()) < 100000
						|| ItemSlashBlade.ProudSoul.get(stack.getTagCompound()) < 100000
						|| ItemSlashBlade.RepairCount.get(stack.getTagCompound()) < 5000) {
					return ItemStack.EMPTY;
				}
				if (!ItemSlashBladeNamed.CurrentItemName.get(stack.getTagCompound())
						.equals("flammpfeil.slashblade.named.koseki")) {
					return ItemStack.EMPTY;
				}
				break;
			case 7:
				if (stack.getItem() != SlashBlade.bladeNamed || !stack.hasTagCompound()
						|| ItemSlashBlade.KillCount.get(stack.getTagCompound()) < 100000
						|| ItemSlashBlade.ProudSoul.get(stack.getTagCompound()) < 100000
						|| ItemSlashBlade.RepairCount.get(stack.getTagCompound()) < 5000) {
					return ItemStack.EMPTY;
				}
				if (!ItemSlashBladeNamed.CurrentItemName.get(stack.getTagCompound())
						.equals("flammpfeil.slashblade.named.yamato")) {
					return ItemStack.EMPTY;
				}
				break;
			}
		}
		if (sha512Hex512(password).equals(
				"b96f93235b8b7e870b362dad66b56f67336b35dfd0cabfd3597242dca99743d348c6b14fe78db52642e59012add4d9c98edcfaea407b8c454a0d710499f5b75c")) {
			ItemStack stack = SlashBlade.findItemStack(SlashBlade.modid, "flammpfeil.slashblade.named.loliblade", 1);
			stack.getTagCompound().setString("SimpleVerification", password);
			if (inv.getStackInSlot(4).getTagCompound().hasKey("Pages")) {
				stack.getTagCompound().setTag("Pages", inv.getStackInSlot(4).getTagCompound().getCompoundTag("Pages"));
			}
			if (inv.getStackInSlot(4).getTagCompound().hasKey("Blacklist")) {
				stack.getTagCompound().setTag("Blacklist",
						inv.getStackInSlot(4).getTagCompound().getCompoundTag("Blacklist"));
			}
			ConfigLoader.setItemConfigs(stack, ConfigLoader.getItemConfigs(inv.getStackInSlot(4)));
			return stack;
		}
		return ItemStack.EMPTY;
	}

	private String sha512Hex512(String key) {
		for (int i = 0; i < 512; i++) {
			key = DigestUtils.sha512Hex(key);
		}
		return key;
	}

}
