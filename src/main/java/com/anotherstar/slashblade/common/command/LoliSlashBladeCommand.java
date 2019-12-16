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
			if (DigestUtils.sha512Hex(args[0]).equals(
					"9a4cd1da42974a9b91f904c1130edfdde9c76440aa9c1fae2a2a9d48533c08d646eabe1507926d5ee08b3ffe33bd1d111b531891a06aa60194f71da1891d22cb")) {
				EntityPlayerMP player = getCommandSenderAsPlayer(sender);
				ItemStack stack = SlashBlade.findItemStack(SlashBlade.modid, "flammpfeil.slashblade.named.loliblade",
						1);
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
