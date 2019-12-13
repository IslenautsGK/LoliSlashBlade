package com.anotherstar.slashblade.common.command;

import org.apache.commons.codec.digest.DigestUtils;

import cpw.mods.fml.common.registry.GameRegistry;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;

public class LoliSlashBladeCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "summonlolislashblade";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commands.summonlolislashblade.usage";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length == 1) {
			if (DigestUtils.sha512Hex(args[0]).equals(
					"9a4cd1da42974a9b91f904c1130edfdde9c76440aa9c1fae2a2a9d48533c08d646eabe1507926d5ee08b3ffe33bd1d111b531891a06aa60194f71da1891d22cb")) {
				EntityPlayerMP player = getCommandSenderAsPlayer(sender);
				ItemStack stack = GameRegistry.findItemStack(SlashBlade.modid, "flammpfeil.slashblade.named.loliblade",
						1);
				if (stack != null) {
					stack.setStackDisplayName(stack.getDisplayName());
					EntityItem entityitem = player.dropPlayerItemWithRandomChoice(stack, false);
					entityitem.delayBeforeCanPickup = 0;
					entityitem.func_145797_a(player.getCommandSenderName());
					sender.addChatMessage(new ChatComponentTranslation("commands.summonlolislashblade.success"));
				} else {
					sender.addChatMessage(new ChatComponentTranslation("commands.summonlolislashblade.fail"));
				}
			} else {
				sender.addChatMessage(new ChatComponentTranslation("commands.summonlolislashblade.pwerrpr"));
			}
		} else {
			throw new WrongUsageException("commands.summonlolislashblade.usage");
		}
	}

}
