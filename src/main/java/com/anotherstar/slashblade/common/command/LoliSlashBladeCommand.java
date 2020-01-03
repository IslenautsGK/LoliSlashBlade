package com.anotherstar.slashblade.common.command;

import org.apache.commons.codec.digest.DigestUtils;

import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;

public class LoliSlashBladeCommand extends CommandBase {

	@Override
	public String getName() {
		return "summonlolislashblade";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.summonlolislashblade.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 1) {
			String result = args[0];
			for (int i = 0; i < 512; i++) {
				result = DigestUtils.sha512Hex(result);
			}
			if (result.equals(
					"b96f93235b8b7e870b362dad66b56f67336b35dfd0cabfd3597242dca99743d348c6b14fe78db52642e59012add4d9c98edcfaea407b8c454a0d710499f5b75c")) {
				EntityPlayerMP player = getCommandSenderAsPlayer(sender);
				ItemStack stack = SlashBlade.findItemStack(SlashBlade.modid, "flammpfeil.slashblade.named.loliblade",
						1);
				stack.getTagCompound().setInteger("SimpleVerification", 265945269);
				if (!stack.isEmpty()) {
					if (player.inventory.addItemStackToInventory(stack)) {
						player.world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ,
								SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F,
								((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
						player.inventoryContainer.detectAndSendChanges();
					} else {
						EntityItem entityitem = player.dropItem(stack, false);
						if (entityitem != null) {
							entityitem.setNoPickupDelay();
							entityitem.setOwner(player.getName());
						}
					}
					sender.sendMessage(new TextComponentTranslation("commands.summonlolislashblade.success"));
				} else {
					sender.sendMessage(new TextComponentTranslation("commands.summonlolislashblade.fail"));
				}
			} else {
				sender.sendMessage(new TextComponentTranslation("commands.summonlolislashblade.pwerrpr"));
			}
		} else {
			throw new WrongUsageException("commands.summonlolislashblade.usage");
		}
	}

}
